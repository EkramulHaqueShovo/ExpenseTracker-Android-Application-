package com.example.expensestracker.expensetracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTotalBalance;
    private EditText editTextText;
    private Button submitButton;
    private Button cleanButton;
    private TextView resultLabel;
    private TextView developerInfoTextView;

    private double totalExpenses = 0.0;
    private double totalBalance = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTotalBalance = findViewById(R.id.editTextTotalBalance);
        editTextText = findViewById(R.id.editTextText);
        submitButton = findViewById(R.id.button);
        cleanButton = findViewById(R.id.cleanButton);
        resultLabel = findViewById(R.id.resultLabel);
        developerInfoTextView = findViewById(R.id.developerInfoTextView);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSubmission();
            }
        });

        cleanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllFields();
            }
        });

        developerInfoTextView.setText("Developer: Ekramul Haque Shovo\nGitHub: https://github.com/EkramulHaqueShovo");
    }

    private void handleSubmission() {
        String totalBalanceText = editTextTotalBalance.getText().toString();
        String expenseText = editTextText.getText().toString();

        StringBuilder resultMessage = new StringBuilder();

        if (!totalBalanceText.isEmpty()) {
            totalBalance = Double.parseDouble(totalBalanceText);
            resultMessage.append(String.format("Total Balance(Taka): %.2f\n", totalBalance));
        }

        if (!expenseText.isEmpty()) {
            double expenseAmount = Double.parseDouble(expenseText);
            totalExpenses += expenseAmount;

            double remainingBalance = totalBalance - totalExpenses;

            resultMessage.append(String.format("Expense: %.2f\n", expenseAmount));
            resultMessage.append(String.format("Total Expenses(Taka): %.2f\n", totalExpenses));
            resultMessage.append(String.format("Remaining Balance(Taka): %.2f\n", remainingBalance));

            clearInputFields();
        }

        resultLabel.setText(resultMessage.toString());
    }

    private void clearInputFields() {
        editTextTotalBalance.getText().clear();
        editTextText.getText().clear();
    }

    private void clearAllFields() {
        totalExpenses = 0.0;
        totalBalance = 0.0;
        resultLabel.setText("");
        clearInputFields();
    }
}
