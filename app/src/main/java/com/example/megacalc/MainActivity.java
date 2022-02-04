package com.example.megacalc;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.TextView;

import java.util.*;
class Calc {
    public Calc(){
    }
    public static int endingParentheses(String s, int start) {
        int count = 0;
        for (int i = start; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                count++;
            } else if (s.charAt(i) == ')') {
                count--;
            }
            if (count == 0) {
                return i;
            }
        }
        return s.length() - 1;
    }

    public static String formatEq(String eq, String operations) {
        String[] valid = operations.split("");
        // 2*3+6/3.14
        for (int i = 0; i < eq.length(); i++) {
            String ch = String.valueOf(eq.charAt(i));
            if (Arrays.asList(valid).contains(ch)) {
                int p = getPreviousOperation(eq, i);
                int n = getNextOperation(eq, i);
                String s1 = eq.substring(0, p);
                String s2 = eq.substring(n, eq.length());
                String s3 = eq.substring(p, n);
                double toAdd = solve(s3);
                System.out.println(s3 + " is " + toAdd);
                String tA = String.valueOf(toAdd);
                eq = s1 + tA + s2;
                System.out.println("Formatted: " + eq);

            }
        }

        return eq;
    }

    public static int totalOperations(String s) {
        int count = 0;
        String operations = "^+-*/^%";
        for (int x = 0; x < s.length(); x++) {
            if (operations.contains(String.valueOf(s.charAt(x)))) {
                count++;
            }
        }
        return count;
    }

    public static int getPreviousOperation(String s, int i) {
        String legalOperations = "^*/%+-";
        for (int j = i - 1; j > -1; j--) {
            if (legalOperations.contains(String.valueOf(s.charAt(j)))) {
                return j + 1;
            }
        }
        return 0;
    }

    public static double solve(String s) {
        // 6/3.14
        String legalOperations = "^*/%+-";
        double a;
        for (int j = 0; j < s.length(); j++) {
            if (legalOperations.contains(String.valueOf(s.charAt(j)))) {
                String first = s.substring(0, j);
                String second = s.substring(j + 1, s.length());
                a = Double.parseDouble(first);
                double b = Double.parseDouble(second);
                String op = String.valueOf(s.charAt(j));
                Equation e = new Equation(a, op, b);
                return e.answer();
            }
        }
        a = Double.parseDouble(s);
        return a;
    }

    public static double formatStr(String basic) {
        // 1+2*1.3+6/(1+(2+3*2))+2^3
        String[] legalOperations = { "^", "*/%", "+-" };
        if (basic.contains("(")) {
            int start = basic.indexOf("(");
            int end = endingParentheses(basic, start);
            String ne = basic.substring(start + 1, end);
            double newOne = formatStr(ne);
            String addi = String.valueOf(newOne);
            System.out.println(addi);
            basic = basic.substring(0, start) + addi + basic.substring(end + 1, basic.length());
        }
        System.out.println(basic);
        while (totalOperations(basic) > 0) {
            for (String l : legalOperations) {
                basic = formatEq(basic, l);
            }
        }

        return Double.parseDouble(basic);
    }

    public static int getNextOperation(String s, int i) {
        String legalOperations = "^*/%+-";
        for (int j = i + 1; j < s.length(); j++) {
            if (legalOperations.contains(String.valueOf(s.charAt(j)))) {
                return j;
            }
        }
        return s.length();
    }

    public double calculate(String input){
        return formatStr(input);
    }
}
class Equation {
    double a;
    String operator;
    double b;

    public Equation(double x) {
        a = x;
    }

    public Equation(double x, String op, double y) {
        a = x;
        b = y;
        operator = op;
    }

    public double answer() {
        if (operator.equals("+")) {
            return a + b;
        } else if (operator.equals("-")) {
            return a - b;
        } else if (operator.equals("*")) {
            return a * b;
        } else if (operator.equals("/")) {
            return a / b;
        } else if (operator.equals("%")) {
            return a % b;
        } else if (operator.equals("^")) {
            return Math.pow(a, b);
        } else {
            return a;
        }
    }
}

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public int indexToEdit = 0;
    public String textToDisplay = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void allClear(View v){
        textToDisplay = "";
        indexToEdit = 0;
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(textToDisplay);
    }
    public void delete(View v){
        if(indexToEdit > 1){
            String newText = "";
            for(int i = 0; i < textToDisplay.length(); i++){
                if(i!= indexToEdit){
                    newText = newText + textToDisplay.charAt(i);
                }
            }
            textToDisplay = newText;
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(textToDisplay);
        }
    }
    public void move_left(View v){
        if(indexToEdit > 0){
            indexToEdit--;
        }
    }
    public void move_right(View v){
        if(indexToEdit < textToDisplay.length()){
            indexToEdit++;
        }
    }
    public void addToDispl(View v){
        Button btn = (Button) findViewById(v.getId());
        String text = btn.getText().toString();
        textToDisplay = textToDisplay + text;
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(textToDisplay);
        indexToEdit++;
    }
    public void equals(View v){
        Calc c = new Calc();
        double val = c.calculate(textToDisplay);
        textToDisplay = String.valueOf(val);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(textToDisplay);
        indexToEdit = textToDisplay.length();
    }
    @Override
    public void onClick(View v) {

    }
}