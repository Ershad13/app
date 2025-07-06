/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.androidthings.myproject;

// Updated to androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// Changed from Activity to AppCompatActivity
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText amountEditText;
    private DatePicker datePicker;
    private Spinner categorySpinner;
    private Button addTransactionButton;
    private TextView transactionsTextView; // Added TextView for displaying transactions

    // In-memory storage for transactions (replace with database later)
    private List<Transaction> transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Link to the layout file

        transactions = new ArrayList<>();

        amountEditText = findViewById(R.id.amountEditText);
        datePicker = findViewById(R.id.datePicker);
        categorySpinner = findViewById(R.id.categorySpinner);
        addTransactionButton = findViewById(R.id.addTransactionButton);
        transactionsTextView = findViewById(R.id.transactionsTextView); // Initialize TextView
        transactionsTextView.setMovementMethod(new android.text.method.ScrollingMovementMethod()); // Enable scrolling

        // Populate spinner with categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.transaction_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        addTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTransaction();
            }
        });

        Log.d(TAG, "onCreate");
    }

    private void addTransaction() {
        String amountStr = amountEditText.getText().toString();
        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        long dateInMillis = calendar.getTimeInMillis();

        String category = categorySpinner.getSelectedItem().toString();

        Transaction transaction = new Transaction(amount, dateInMillis, category);
        transactions.add(transaction);

        // For now, just show a toast message. Later, update UI or database.
        Toast.makeText(this, "Transaction added: " + transaction.toString(), Toast.LENGTH_LONG).show();

        // Clear input fields
        amountEditText.setText("");
        // Reset DatePicker to current date (optional)
        // Reset Spinner to default (optional)

        Log.d(TAG, "Transaction added: " + transaction.toString());
        updateTransactionsView(); // Refresh the TextView
    }

    private void updateTransactionsView() {
        StringBuilder sb = new StringBuilder();
        for (Transaction t : transactions) {
            sb.append(t.toString()).append("\n\n");
        }
        if (sb.length() == 0) {
            transactionsTextView.setText("No transactions yet.");
        } else {
            transactionsTextView.setText(sb.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    // Simple Transaction data class (move to a separate file later)
    private static class Transaction {
        double amount;
        long date; // Store date as milliseconds
        String category;

        public Transaction(double amount, long date, String category) {
            this.amount = amount;
            this.date = date;
            this.category = category;
        }

        @Override
        public String toString() {
            // Simple string representation for logging/toast
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(date);
            return "Amount: " + amount + ", Date: " + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR) + ", Category: " + category;
        }
    }
}
