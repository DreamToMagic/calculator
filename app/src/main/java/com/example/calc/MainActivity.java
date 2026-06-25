package com.example.calc;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;

public class MainActivity extends Activity {

    private TextView display;
    private String current = "0";
    private String operator = "";
    private double operand = 0;
    private boolean clearOnNext = false;
    private boolean isResult = false;

    private static final String[][] BUTTONS = {
        {"C", "1", "3"}, {"÷", "1", "1"}, {"×", "1", "1"}, {"←", "1", "1"},
        {"7", "1", "0"}, {"8", "1", "0"}, {"9", "1", "0"}, {"−", "1", "1"},
        {"4", "1", "0"}, {"5", "1", "0"}, {"6", "1", "0"}, {"+", "1", "1"},
        {"1", "1", "0"}, {"2", "1", "0"}, {"3", "1", "0"}, {"=", "1", "2"},
        {"0", "3", "0"}, {".", "1", "0"},
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.parseColor("#1A1D24"));
        root.setPadding(16, 32, 16, 16);

        display = new TextView(this);
        display.setTextSize(TypedValue.COMPLEX_UNIT_SP, 42);
        display.setTypeface(Typeface.MONOSPACE);
        display.setTextColor(Color.WHITE);
        display.setGravity(Gravity.END | Gravity.BOTTOM);
        display.setPadding(16, 80, 16, 16);
        display.setText("0");
        root.addView(display, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 2.5f));

        GridLayout grid = new GridLayout(this);
        grid.setColumnCount(4);
        grid.setRowCount(5);
        root.addView(grid, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 6f));

        for (String[] btn : BUTTONS) {
            Button b = new Button(this);
            b.setText(btn[0]);
            b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
            b.setTypeface(Typeface.DEFAULT_BOLD);
            b.setAllCaps(false);
            b.setOnClickListener(this::onClick);

            int bg, fg = Color.WHITE;
            switch (Integer.parseInt(btn[2])) {
                case 1: bg = Color.parseColor("#E8913A"); break;
                case 2: bg = Color.parseColor("#3C8CDC"); break;
                case 3: bg = Color.parseColor("#E0554A"); break;
                default: bg = Color.parseColor("#2D3340"); fg = Color.parseColor("#E0E0E0");
            }
            b.setBackgroundColor(bg);
            b.setTextColor(fg);

            GridLayout.LayoutParams p = new GridLayout.LayoutParams();
            p.width = 0; p.height = 0;
            p.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, Integer.parseInt(btn[1]));
            p.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1);
            p.setMargins(4, 4, 4, 4);
            grid.addView(b, p);
        }

        setContentView(root);
    }

    public void onClick(View v) {
        String key = ((Button) v).getText().toString();
        switch (key) {
            case "C": current = "0"; operator = ""; operand = 0; clearOnNext = false; isResult = false; break;
            case "←":
                if (!isResult && current.length() > 1) current = current.substring(0, current.length() - 1);
                else if (!isResult) current = "0";
                isResult = false; break;
            case "+": case "−": case "×": case "÷":
                if (!operator.isEmpty() && !clearOnNext) calc();
                operand = Double.parseDouble(current); operator = key; clearOnNext = true; isResult = false; break;
            case "=":
                if (!operator.isEmpty()) { calc(); operator = ""; }
                isResult = true; break;
            case ".":
                if (clearOnNext) { current = "0"; clearOnNext = false; }
                if (!current.contains(".")) current += "."; isResult = false; break;
            default:
                if (clearOnNext || isResult) { current = "0"; clearOnNext = false; isResult = false; }
                current = current.equals("0") ? key : current + key; break;
        }
        String t = current.length() > 12 ? current.substring(0, 12) + "…" : current;
        display.setText(t);
    }

    private void calc() {
        double b = Double.parseDouble(current);
        switch (operator) {
            case "+": operand += b; break;
            case "−": operand -= b; break;
            case "×": operand *= b; break;
            case "÷": operand = b != 0 ? operand / b : 0; break;
        }
        current = operand == (long) operand ? String.valueOf((long) operand) : String.valueOf(operand);
        clearOnNext = true;
    }
}
