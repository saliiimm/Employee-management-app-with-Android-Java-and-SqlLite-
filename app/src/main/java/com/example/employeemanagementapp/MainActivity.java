package com.example.employeemanagementapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addButton = findViewById(R.id.button_add_employee);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEmployeeActivity.class);
                startActivity(intent);
            }
        });

        dbHelper = new DatabaseHelper(this);

        displayEmployees();
    }

    @SuppressLint("SetTextI18n")
    private void displayEmployees() {
        LinearLayout employeeLayout = findViewById(R.id.employee_layout);
        employeeLayout.removeAllViews();

        try {
            Cursor cursor = dbHelper.getAllEmployees();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String firstName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FIRST_NAME));
                    @SuppressLint("Range") String lastName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LAST_NAME));
                    @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE_NUMBER));
                    @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL));
                    @SuppressLint("Range") String jobTitle = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_JOB));
                    @SuppressLint("Range") String residence = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_RESIDENCE));

                    Log.d("Employee Details", "First Name: " + firstName);
                    Log.d("Employee Details", "Last Name: " + lastName);
                    Log.d("Employee Details", "Phone Number: " + phoneNumber);
                    Log.d("Employee Details", "Email: " + email);
                    Log.d("Employee Details", "Job Title: " + jobTitle);
                    Log.d("Employee Details", "Residence: " + residence);

                    TextView textView = new TextView(this);
                    textView.setText("First Name: " + firstName + "\n" +
                            "Last Name: " + lastName + "\n" +
                            "Phone Number: " + phoneNumber + "\n" +
                            "Email: " + email + "\n" +
                            "Job Title: " + jobTitle + "\n" +
                            "Residence: " + residence + "\n");

                    textView.setTextColor(Color.RED);
                    employeeLayout.addView(textView);
                } while (cursor.moveToNext());

                cursor.close();
            } else {
                Log.d("Employee Details", "No employees found in the database.");
            }
        } catch (Exception e) {
            Log.e("Employee Details", "Error accessing database: " + e.getMessage());
        }
    }
}
