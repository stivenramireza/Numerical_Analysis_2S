package com.example.sacrew.numericov4.fragments.interpolationFragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.sacrew.numericov4.R;
import com.example.sacrew.numericov4.fragments.MainActivityTable;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpBisection;
import com.example.sacrew.numericov4.fragments.customPopUps.popUpNewtonDifferences;


import org.matheclipse.core.basic.Config;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.eval.TeXUtilities;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.eval.DoubleEvaluator;

import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class newtonInterpolator extends baseInterpolationMethods {

    private List<List<Double>> derivs;
    private boolean errorDivision = true;

    public newtonInterpolator() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_newton_interpolator, container, false);
        Button runHelp = view.findViewById(R.id.runHelp);
        Button run = view.findViewById(R.id.run);
        run.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                calc = false;
                cleanGraph();
                execute();

            }
        });
        Button showEquations = view.findViewById(R.id.showEquations);
        showEquations.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(calc) {
                    Intent i = new Intent(getContext(), mathExpressions.class);
                    Bundle b = new Bundle();
                    b.putString("key", function); //Your id
                    i.putExtras(b); //Put your id to your next Intent
                    startActivity(i);
                }
            }
        });
        runHelp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                executeHelp();
            }
        });
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void executeHelp() {
        Intent i = new Intent(getContext().getApplicationContext(), popUpNewtonDifferences.class);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void execute() {
        bootStrap();
        derivs = new LinkedList<>();
        newtonInterpolation(fxn, 0, 1, new LinkedList<Double>());
        if (errorDivision) {
            StringBuilder uglyFunction = new StringBuilder(String.valueOf(fxn.get(0)));
            StringBuilder prev = new StringBuilder("");
            for (int i = 1; i < derivs.size(); i++) {
                System.out.println("xn[" + i + "]=" + xn.get(i - 1));
                System.out.println("deriv[" + i + "]=" + derivs.get(i).get(0));
                prev.append("(x-").append(String.valueOf(xn.get(i - 1))).append(")");
                uglyFunction.append("+").append(String.valueOf(derivs.get(i).get(0))).append(prev);
            }
            ExprEvaluator util = new ExprEvaluator(false, 100);
            System.out.println("ugly " + uglyFunction.toString());
            IExpr result = util.evaluate(uglyFunction.toString());

            System.out.println("pretty " + result.toString());

            Config.PARSER_USE_LOWERCASE_SYMBOLS = false;
            EvalEngine engine = new EvalEngine(false);
            IExpr outLatex = engine.evaluate(F.Simplify(result));
            IExpr expanded = engine.evaluate(F.FullSimplify(result));
            System.out.println("simplify : " +outLatex.toString());
            System.out.println("full : " +expanded.toString());
            TeXUtilities texUtil = new TeXUtilities(engine, false);

            StringWriter stw = new StringWriter();
            texUtil.toTeX(outLatex, stw);

            System.out.println("latex: " + stw.toString());
            updateGraph(uglyFunction.toString(), getContext());
            function = stw.toString();
            calc = true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void newtonInterpolation(List<Double> fxn, int difference, int i, List<Double> auxFxn) {
        if (fxn.size() == 1) {
            derivs.add(fxn);
        } else if (i == fxn.size()) {
            derivs.add(fxn);
            newtonInterpolation(auxFxn, difference + 1, 1, new LinkedList<Double>());
        } else {
            System.out.println("difference " + difference + " i " + i + " size " + xn.size());
            double denominator = (xn.get(i + difference) - xn.get(i - 1));
            if (denominator == 0) {
                Toast.makeText(getContext(), "Error division by 0", Toast.LENGTH_SHORT).show();
                errorDivision = false;
            }
            double newFxn = (fxn.get(i) - fxn.get(i - 1)) / denominator;
            auxFxn.add(newFxn);
            newtonInterpolation(fxn, difference, i + 1, auxFxn);
        }
    }
}
