package com.example.sacrew.numericov4.fragments.systemEquationsFragment;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpDoolittle;

import java.util.LinkedList;
import java.util.Objects;

import static com.example.sacrew.numericov4.fragments.systemEquations.animations;
import static com.example.sacrew.numericov4.fragments.systemEquations.animatorSet;
import static com.example.sacrew.numericov4.fragments.systemEquations.bValuesText;
import static com.example.sacrew.numericov4.fragments.systemEquations.matrixAText;
import static com.example.sacrew.numericov4.fragments.systemEquations.times;

/**
 * A simple {@link Fragment} subclass.
 */
public class doolittle extends baseFactorizationMethods {
    private TableLayout matrixLText;
    private TableLayout matrixUText;
    private TextView sum;

    public doolittle() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doolittle, container, false);
        matrixLText = view.findViewById(R.id.matrixL);
        matrixUText = view.findViewById(R.id.matrixU);
        Button run = view.findViewById(R.id.run);
        Button pivoter = view.findViewById(R.id.pivoting);
        sum = view.findViewById(R.id.sum);
        Button runHelp = view.findViewById(R.id.runHelp);
        runHelp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                executeHelp();
            }
        });
        run.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                animatorSet.removeAllListeners();
                animatorSet.end();
                animatorSet.cancel();
                begin();
                animatorSet.playSequentially(animations);
                animatorSet.start();
            }

        });
        pivoter.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                securePivot();
            }
        });
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void executeHelp() {
        Intent i = new Intent(Objects.requireNonNull(getContext()).getApplicationContext(), popUpDoolittle.class);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void bootStrap(double[][] expandedMatrix) {

        matrixLText.removeAllViews();
        matrixUText.removeAllViews();
        matrixL = new double[expandedMatrix.length][expandedMatrix.length + 1];
        matrixU = new double[expandedMatrix.length][expandedMatrix.length + 1];
        for (int i = 0; i < expandedMatrix.length; i++) {
            matrixL[i][i] = 1;
        }
        for (int i = 0; i < matrixL.length; i++) {
            TableRow rowU = new TableRow(getContext());
            TableRow rowL = new TableRow(getContext());
            for (int j = 0; j <= matrixL.length; j++) {
                rowU.addView(defaultTextView(matrixU[i][j] + ""));
                rowL.addView(defaultTextView(matrixL[i][j] + ""));
            }
            matrixLText.addView(rowL);
            matrixUText.addView(rowU);
        }
        doolittleMethod(expandedMatrix);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void doolittleMethod(double[][] expandedMatrix) {
        animatorSet = new AnimatorSet();
        animations = new LinkedList<>();
        for (int k = 0; k < expandedMatrix.length; k++) {

            double sum1 = 0;
            ValueAnimator zero = ValueAnimator.ofObject(new ArgbEvaluator(), Color.YELLOW,
                    defaultColor).setDuration(times.getProgress() * 500);
            zero.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    sum.setText("sum = 0");
                }
            });
            zero.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (!animations.isEmpty()) animations.remove(0);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            animations.add(zero);
            final int auxk = k;
            for (int p = 0; p < k; p++) {
                sum1 = sum1 + matrixL[k][p] * matrixU[p][k];

                final int auxp = p;
                final double auxSum = sum1;
                ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.YELLOW,
                        defaultColor).setDuration(times.getProgress() * 500);
                colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        try {
                            ((TableRow) matrixLText.getChildAt(auxk)).getChildAt(auxp).setBackgroundColor((Integer) animator.getAnimatedValue());
                            ((TableRow) matrixUText.getChildAt(auxp)).getChildAt(auxk).setBackgroundColor((Integer) animator.getAnimatedValue());
                            sum.setText("sum = " + auxSum);
                        } catch (Exception e) {
                            matrixLText.removeAllViews();
                            matrixUText.removeAllViews();
                        }
                    }
                });
                colorAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (!animations.isEmpty()) animations.remove(0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        ((TableRow) matrixLText.getChildAt(auxk)).getChildAt(auxp).setBackgroundColor(defaultColor);
                        ((TableRow) matrixUText.getChildAt(auxp)).getChildAt(auxk).setBackgroundColor(defaultColor);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                animations.add(colorAnimator);
            }
            matrixU[k][k] = expandedMatrix[k][k] - sum1;
            final double temp = matrixL[k][k];
            ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.YELLOW,
                    defaultColor).setDuration(times.getProgress() * 500);
            colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    try {
                        TextView cell = (TextView) ((TableRow) matrixUText.getChildAt(auxk)).getChildAt(auxk);
                        cell.setBackgroundColor(operativeColor);
                        cell.setText((temp + "      ").substring(0, 6));
                        ((TableRow) matrixAText.getChildAt(auxk)).getChildAt(auxk).setBackgroundColor((Integer) animator.getAnimatedValue());
                        sum.setBackgroundColor(Color.YELLOW);
                    } catch (Exception e) {
                        matrixLText.removeAllViews();
                        matrixUText.removeAllViews();
                    }
                }
            });
            colorAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    try {
                        ((TableRow) matrixUText.getChildAt(auxk)).getChildAt(auxk)
                                .setBackgroundColor(defaultColor);
                        if (!animations.isEmpty()) animations.remove(0);
                    } catch (Exception e) {
                        matrixLText.removeAllViews();
                    }
                    sum.setBackgroundColor(0);
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    ((TableRow) matrixAText.getChildAt(auxk)).getChildAt(auxk).setBackgroundColor(defaultColor);

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animations.add(colorAnimator);
            for (int i = k + 1; i < expandedMatrix.length; i++) {
                double sum2 = 0;

                final int auxi = i;
                for (int p = 0; p < k; p++) {
                    final int auxp = p;
                    sum2 = sum2 + matrixL[i][p] * matrixU[p][k];
                    final double auxSum = sum2;
                    ValueAnimator animatronix = ValueAnimator.ofObject(new ArgbEvaluator(), Color.YELLOW,
                            defaultColor).setDuration(times.getProgress() * 500);
                    animatronix.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            try {
                                ((TableRow) matrixLText.getChildAt(auxi)).getChildAt(auxp).setBackgroundColor((Integer) animator.getAnimatedValue());
                                ((TableRow) matrixUText.getChildAt(auxp)).getChildAt(auxk).setBackgroundColor((Integer) animator.getAnimatedValue());
                                sum.setText("sum = " + auxSum);
                            } catch (Exception e) {
                                matrixLText.removeAllViews();
                                matrixUText.removeAllViews();
                            }
                        }
                    });
                    animatronix.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (!animations.isEmpty()) animations.remove(0);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                            ((TableRow) matrixLText.getChildAt(auxi)).getChildAt(auxp).setBackgroundColor(defaultColor);
                            ((TableRow) matrixUText.getChildAt(auxp)).getChildAt(auxk).setBackgroundColor(defaultColor);
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    animations.add(animatronix);
                }
                if (matrixU[k][k] == 0) {
                    styleWrongMessage("Error division 0");
                    return;
                }

                matrixL[i][k] = (expandedMatrix[i][k] - sum2) / matrixU[k][k];
                final double temp1 = matrixL[i][k];
                ValueAnimator animatronix2 = ValueAnimator.ofObject(new ArgbEvaluator(), Color.YELLOW,
                        defaultColor).setDuration(times.getProgress() * 500);
                animatronix2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        try {
                            TextView cell = (TextView) ((TableRow) matrixLText.getChildAt(auxi)).getChildAt(auxk);
                            cell.setBackgroundColor(operativeColor);
                            cell.setText((temp1 + "      ").substring(0, 6));
                            ((TableRow) matrixAText.getChildAt(auxi)).getChildAt(auxk).setBackgroundColor((Integer) animator.getAnimatedValue());
                            ((TableRow) matrixUText.getChildAt(auxk)).getChildAt(auxk).setBackgroundColor((Integer) animator.getAnimatedValue());
                            sum.setBackgroundColor(Color.YELLOW);
                        } catch (Exception e) {
                            matrixLText.removeAllViews();
                            matrixUText.removeAllViews();
                        }
                    }
                });
                animatronix2.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        try {
                            ((TableRow) matrixLText.getChildAt(auxi)).getChildAt(auxk)
                                    .setBackgroundColor(defaultColor);
                            if (!animations.isEmpty()) animations.remove(0);
                        } catch (Exception e) {
                            matrixLText.removeAllViews();
                        }
                        sum.setBackgroundColor(0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        ((TableRow) matrixAText.getChildAt(auxi)).getChildAt(auxk).setBackgroundColor(defaultColor);
                        ((TableRow) matrixUText.getChildAt(auxk)).getChildAt(auxk).setBackgroundColor(defaultColor);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                animations.add(animatronix2);
            }
            for (int j = k + 1; j < expandedMatrix.length; j++) {
                double sum3 = 0;
                final int auxj = j;
                for (int p = 0; p < k; p++) {
                    final int auxp = p;
                    sum3 = sum3 + matrixL[k][p] * matrixU[p][j];
                    final double auxSum = sum3;
                    ValueAnimator animatronix = ValueAnimator.ofObject(new ArgbEvaluator(), Color.YELLOW,
                            defaultColor).setDuration(times.getProgress() * 500);
                    animatronix.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            try {
                                ((TableRow) matrixLText.getChildAt(auxk)).getChildAt(auxp).setBackgroundColor((Integer) animator.getAnimatedValue());
                                ((TableRow) matrixUText.getChildAt(auxp)).getChildAt(auxj).setBackgroundColor((Integer) animator.getAnimatedValue());
                                sum.setText("sum = " + auxSum);
                            } catch (Exception e) {
                                matrixLText.removeAllViews();
                                matrixUText.removeAllViews();
                            }
                        }
                    });
                    animatronix.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (!animations.isEmpty()) animations.remove(0);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                            ((TableRow) matrixLText.getChildAt(auxk)).getChildAt(auxp).setBackgroundColor(defaultColor);
                            ((TableRow) matrixUText.getChildAt(auxp)).getChildAt(auxj).setBackgroundColor(defaultColor);
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    animations.add(animatronix);
                }
                if (matrixL[k][k] == 0) {
                    //Toast.makeText(getContext(), "Error division 0", Toast.LENGTH_SHORT).show();
                    styleWrongMessage("Error division 0");
                    return;
                }
                matrixU[k][j] = (expandedMatrix[k][j] - sum3) / matrixL[k][k];
                final double temp2 = matrixU[k][j];
                ValueAnimator animatronix2 = ValueAnimator.ofObject(new ArgbEvaluator(), Color.YELLOW,
                        defaultColor).setDuration(times.getProgress() * 500);
                animatronix2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        try {
                            ((TableRow) matrixLText.getChildAt(auxk)).getChildAt(auxk).setBackgroundColor((Integer) animator.getAnimatedValue());
                            ((TableRow) matrixAText.getChildAt(auxk)).getChildAt(auxj).setBackgroundColor((Integer) animator.getAnimatedValue());
                            TextView cell = (TextView) ((TableRow) matrixUText.getChildAt(auxk)).getChildAt(auxj);
                            cell.setBackgroundColor(operativeColor);
                            cell.setText((temp2 + "     ").substring(0, 6));
                            sum.setBackgroundColor(Color.YELLOW);
                        } catch (Exception e) {
                            matrixLText.removeAllViews();
                            matrixUText.removeAllViews();
                        }
                    }
                });
                animatronix2.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        try {
                            ((TableRow) matrixUText.getChildAt(auxk)).getChildAt(auxj)
                                    .setBackgroundColor(defaultColor);
                            if (!animations.isEmpty()) animations.remove(0);
                        } catch (Exception e) {
                            matrixUText.removeAllViews();
                        }
                        sum.setBackgroundColor(0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        ((TableRow) matrixLText.getChildAt(auxk)).getChildAt(auxk).setBackgroundColor(defaultColor);
                        ((TableRow) matrixAText.getChildAt(auxk)).getChildAt(auxj).setBackgroundColor(defaultColor);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                animations.add(animatronix2);
            }
            matrixL[k][matrixL.length] = expandedMatrix[k][expandedMatrix.length];
            ValueAnimator animatronco = ValueAnimator.ofObject(new ArgbEvaluator(), Color.YELLOW,
                    defaultColor).setDuration(times.getProgress() * 500);
            animatronco.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    try {
                        String aux = ((EditText) bValuesText.getChildAt(auxk)).getText().toString();
                        ((EditText) ((TableRow) matrixLText.getChildAt(auxk)).getChildAt(matrixL.length)).setText(aux);
                    } catch (Exception e) {
                        matrixLText.removeAllViews();
                    }
                }
            });
            animatronco.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (!animations.isEmpty()) animations.remove(0);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animations.add(animatronco);
        }

        double[] x = progresiveSubstitution(matrixL);
        for (int i = 0; i < x.length; i++) {
            final int auxi = i;
            final double val = x[i];
            ValueAnimator animatronco = ValueAnimator.ofObject(new ArgbEvaluator(), Color.YELLOW,
                    defaultColor).setDuration(times.getProgress() * 500);
            animatronco.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    try {
                        ((EditText) ((TableRow) matrixUText.getChildAt(auxi)).getChildAt(matrixL.length)).setText((val + "     ").substring(0, 6));
                    } catch (Exception e) {
                        matrixLText.removeAllViews();
                    }
                }
            });
            animatronco.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (!animations.isEmpty()) animations.remove(0);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animations.add(animatronco);
        }
        substitution(matrixU);

    }


}
