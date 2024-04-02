package com.example.employeemanagementapp;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;



public class EmployeeDetails extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);

        dbHelper = new DatabaseHelper(this);

        long employeeId = getIntent().getLongExtra("employeeId", -1);

        if (employeeId != -1) {
            Cursor cursor = dbHelper.getEmployeeById(employeeId);
            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") String firstName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FIRST_NAME));
                @SuppressLint("Range") String lastName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LAST_NAME));
                @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE_NUMBER));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL));
                @SuppressLint("Range") String job = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_JOB));
                @SuppressLint("Range") String residence = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_RESIDENCE));

                TextView firstNameTextView = findViewById(R.id.edittext_first_name);
                TextView lastNameTextView = findViewById(R.id.edittext_last_name);
                TextView phoneNumberTextView = findViewById(R.id.edittext_phone_number);
                TextView emailTextView = findViewById(R.id.edittext_email);
                TextView jobTitleTextView = findViewById(R.id.edittext_job);
                TextView residenceTextView = findViewById(R.id.edittext_residence);

                firstNameTextView.setText(firstName);
                lastNameTextView.setText(lastName);
                phoneNumberTextView.setText(phoneNumber);
                emailTextView.setText(email);
                jobTitleTextView.setText(job);
                residenceTextView.setText(residence);

                firstNameTextView.setFocusable(false);
                lastNameTextView.setFocusable(false);
                phoneNumberTextView.setFocusable(false);
                emailTextView.setFocusable(false);
                jobTitleTextView.setFocusable(false);
                residenceTextView.setFocusable(false);

                int grayColor = getResources().getColor(android.R.color.darker_gray);
                firstNameTextView.setTextColor(grayColor);
                lastNameTextView.setTextColor(grayColor);
                phoneNumberTextView.setTextColor(grayColor);
                emailTextView.setTextColor(grayColor);
                jobTitleTextView.setTextColor(grayColor);
                residenceTextView.setTextColor(grayColor);

                cursor.close();
            } else {
                Log.d("Employee Details", "No employee found with ID: " + employeeId);
            }
        } else {
            Log.d("Employee Details", "Invalid employee ID");
        }
    }
}
