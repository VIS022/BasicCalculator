package com.example.basiccalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.google.android.material.button.MaterialButton;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView data_input, data_result;

    MaterialButton btn_clear, btn_dot, btn_back, btn_equal;
    MaterialButton btn_divide, btn_plus, btn_minus, btn_multiply, btn_per;
    MaterialButton button_0, button_1, button_2, button_3, button_4, button_5, button_6, button_7, button_8, button_9;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initialize TextViews for input and result
        data_input = findViewById(R.id.datainput);
        data_result = findViewById(R.id.result);

        // Assign click listeners to MaterialButtons using the assignId method
        assignId(btn_equal, R.id.btnequal);
        assignId(btn_clear, R.id.btnclear);
        assignId(btn_dot, R.id.btndot);
        assignId(btn_back, R.id.btnback);
        assignId(btn_divide, R.id.btndivide);
        assignId(btn_plus, R.id.btnplus);
        assignId(btn_minus, R.id.btnminus);
        assignId(btn_multiply, R.id.btnmultiply);
        assignId(btn_per, R.id.btnper);
        assignId(button_0, R.id.button0);
        assignId(button_1, R.id.button1);
        assignId(button_2, R.id.button2);
        assignId(button_3, R.id.button3);
        assignId(button_4, R.id.button4);
        assignId(button_5, R.id.button5);
        assignId(button_6, R.id.button6);
        assignId(button_7, R.id.button7);
        assignId(button_8, R.id.button8);
        assignId(button_9, R.id.button9);

    }
    //Assigns a click listener to a MaterialButton and associates it with a given resource ID.
    //btn The MaterialButton to which the click listener will be assigned.
    //id  The resource ID of the MaterialButton.
    void assignId(MaterialButton btn, int id) {
        btn = findViewById(id); // Find the MaterialButton by its resource ID
        btn.setOnClickListener(this); // Assign the click listener to the MaterialButton


    }



    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        String dataToCalculate = data_input.getText().toString();

        // Clear the input and result fields if the "C" button is clicked
        if (buttonText.equals("C")) {
            data_input.setText("");
            data_result.setText("0");
            return;
        }

        // Calculate and display the result when the "=" button is clicked
        if (buttonText.equals("=")) {
            // Check if there's a valid expression to calculate
            if (!dataToCalculate.isEmpty()) {
                // Ensure the last character is not an operator
                while (isOperator(dataToCalculate.charAt(dataToCalculate.length() - 1))) {
                    dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
                }
                dataToCalculate = getResult(dataToCalculate);
                data_result.setText("=  " + dataToCalculate); // Include '=' before the answer
            }
            return;
        } else if (buttonText.equals("back")) {
            // Remove the last character from the input if the "back" button is clicked
            if (!dataToCalculate.isEmpty()) {
                dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
            }
        } else if (isOperator(buttonText.charAt(0))) {
            // Check if the last character is an operator and replace it
            if (!dataToCalculate.isEmpty() && isOperator(dataToCalculate.charAt(dataToCalculate.length() - 1))) {
                dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
            }
            dataToCalculate += buttonText;
        } else if (buttonText.equals("%")) {
            // Check if the last character is a number and interpret it as a percentage
            if (!dataToCalculate.isEmpty() && Character.isDigit(dataToCalculate.charAt(dataToCalculate.length() - 1))) {
                double value = Double.parseDouble(dataToCalculate) / 100.0;
                dataToCalculate = String.valueOf(value);
            }
        } else {
            dataToCalculate += buttonText;
        }

        data_input.setText(dataToCalculate); // Update data_input for non-equal buttons
    }





    // Helper function to remove the last operator from the input string
    private String removeLastOperator(String input) {
        if (input.length() > 0 && isOperator(input.charAt(input.length() - 1))) {
            return input.substring(0, input.length() - 1);
        }
        return input;
    }



    // Helper function to check if a character is an operator (+, -, *, /, %)
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    String getResult(String data) {
        try {
            // Check if the input expression is empty
            if (data.isEmpty()) {
                return "0"; // Return "0" for empty expressions
            }
            Context context = Context.enter(); // Create a new JavaScript context
            context.setOptimizationLevel(-1); // Disable JavaScript optimization for safe execution
            Scriptable scriptable = context.initSafeStandardObjects();

            // Evaluate the expression and store the result in 'result'
            Object result = context.evaluateString(scriptable, data, "Javascript", 1, null);

            // Check if the result is null or undefined
            if (result == null || result == Context.getUndefinedValue() || result.toString().equals("Infinity")) {
                return "Undefined"; // Return "Undefined" for division by zero or undefined result
            }

            String finalResult = Context.toString(result); // Convert the result to a string and remove trailing ".0" if present
            return finalResult;
        } catch (Exception e) {
            return "Syntax Error";
        } finally {
            Context.exit();
        }
    }
}