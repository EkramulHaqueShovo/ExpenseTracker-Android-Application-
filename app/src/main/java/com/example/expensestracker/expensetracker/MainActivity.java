package com.example.expensestracker.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.text.Html;


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

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    private double totalBalance = 0.0;
    private double totalExpenses = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

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

        // Load initial data from the database
        loadInitialData();

        // Show stored data
        showStoredData();
    }

    private void handleSubmission() {
        String totalBalanceText = editTextTotalBalance.getText().toString();
        String expenseText = editTextText.getText().toString();

        ContentValues values = new ContentValues();

        if (!totalBalanceText.isEmpty()) {
            double additionalBalance = Double.parseDouble(totalBalanceText);
            values.put("amount", additionalBalance);
            database.insert("balance", null, values);
        }

        if (!expenseText.isEmpty()) {
            double expenseAmount = Double.parseDouble(expenseText);
            values.clear();
            values.put("amount", expenseAmount);
            database.insert("expenses", null, values);

            totalExpenses += expenseAmount;

            clearInputFields();
        } else {
            // If no expense amount is entered, clear only the balance field
            editTextTotalBalance.getText().clear();
        }

        // Calculate total balance by summing up all stored balances
        calculateTotalBalance();

        // Show updated data
        showStoredData();
    }


    private void calculateTotalBalance() {
        try {
            // Query the database for the sum of all stored balances
            Cursor balanceCursor = database.rawQuery("SELECT COALESCE(SUM(amount), 0) FROM balance", null);

            if (balanceCursor.moveToFirst()) {
                totalBalance = balanceCursor.getDouble(0);
            }

            balanceCursor.close();
        } catch (Exception e) {
            Log.e("calculateTotalBalance", "Error calculating total balance", e);
        }
    }

    private void showStoredData() {
        double remainingBalance = totalBalance - totalExpenses;

        StringBuilder resultMessage = new StringBuilder();
        resultMessage.append("<br/><b>Data Summary:</b><br/><br/>");
        resultMessage.append("<b>Total Balance(Taka):</b> ").append(String.format("%.2f<br/>", totalBalance));
        resultMessage.append("<b>Total Expenses (Taka):</b> ").append(String.format("%.2f<br/>", totalExpenses));
        resultMessage.append("<b>Remaining Balance(Taka):</b> ").append(String.format("%.2f<br/>", remainingBalance));




        resultLabel.setText(Html.fromHtml(resultMessage.toString(), Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
    }




    private void clearInputFields() {
        editTextTotalBalance.getText().clear();
        editTextText.getText().clear();
    }

    private void clearAllFields() {
        totalBalance = 0.0;
        totalExpenses = 0.0;
        resultLabel.setText("");
        clearInputFields();

        // Clear all records from the "expenses" and "balance" tables
        database.delete("expenses", null, null);
        database.delete("balance", null, null);
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    private static class DBHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "expenses.db";
        private static final int DATABASE_VERSION = 1;

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL("CREATE TABLE expenses (_id INTEGER PRIMARY KEY AUTOINCREMENT, amount REAL);");
                db.execSQL("CREATE TABLE balance (_id INTEGER PRIMARY KEY AUTOINCREMENT, amount REAL);");
            } catch (Exception e) {
                Log.e("DBHelper", "Error creating tables", e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Upgrade the database if needed
        }
    }

    private void loadInitialData() {
        try {
            // Query the database for initial values
            Cursor expenseCursor = database.rawQuery("SELECT COALESCE(SUM(amount), 0) FROM expenses", null);

            if (expenseCursor.moveToFirst()) {
                totalExpenses = expenseCursor.getDouble(0);
            }

            // Load total balance if it exists
            Cursor balanceCursor = database.rawQuery("SELECT COALESCE(SUM(amount), 0) FROM balance", null);

            if (balanceCursor.moveToFirst()) {
                totalBalance = balanceCursor.getDouble(0);
            }

            expenseCursor.close();
            balanceCursor.close();
        } catch (Exception e) {
            Log.e("loadInitialData", "Error loading initial data", e);
        }
    }
}
