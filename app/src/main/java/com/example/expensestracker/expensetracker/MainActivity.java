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
    private TextView resultLabel;

    private double totalExpenses = 0.0;
    private double totalBalance = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTotalBalance = findViewById(R.id.editTextTotalBalance);
        editTextText = findViewById(R.id.editTextText);
        submitButton = findViewById(R.id.button);
        resultLabel = findViewById(R.id.resultLabel);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String totalBalanceText = editTextTotalBalance.getText().toString();
                String expenseText = editTextText.getText().toString();

                if (!totalBalanceText.isEmpty()) {
                    totalBalance = Double.parseDouble(totalBalanceText);
                }

                if (!expenseText.isEmpty()) {
                    double expenseAmount = Double.parseDouble(expenseText);
                    totalExpenses += expenseAmount;

                    double remainingBalance = totalBalance - totalExpenses;

                    StringBuilder resultStringBuilder = new StringBuilder();
                    resultStringBuilder.append("Total Balance(Taka): ").append(totalBalance)
                            .append("\nTotal Expenses(Taka): ").append(totalExpenses)
                            .append("\nRemaining Balance(Taka): ").append(remainingBalance);

                    resultLabel.setText(resultStringBuilder.toString());

                    editTextTotalBalance.getText().clear();
                    editTextText.getText().clear();
                }
            }
        });
    }
}
