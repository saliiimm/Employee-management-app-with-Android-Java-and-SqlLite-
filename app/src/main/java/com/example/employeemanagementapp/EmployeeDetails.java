package com.example.employeemanagementapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EmployeeDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);

        // Retrieve the employee object from the intent
        Employee employee = (Employee) getIntent().getSerializableExtra("employee");

        // Display the employee details in the UI
        TextView firstNameTextView = findViewById(R.id.edittext_first_name);
        TextView lastNameTextView = findViewById(R.id.edittext_last_name);
        TextView phoneNumberTextView = findViewById(R.id.edittext_phone_number);
        TextView emailTextView = findViewById(R.id.edittext_email);
        TextView jobTitleTextView = findViewById(R.id.edittext_job);
        TextView residenceTextView = findViewById(R.id.edittext_residence);


        if (employee != null) {
            firstNameTextView.setText(employee.getFirstName());
            lastNameTextView.setText(employee.getLastName());
            phoneNumberTextView.setText(employee.getPhoneNumber());
            emailTextView.setText(employee.getEmail());
            jobTitleTextView.setText(employee.getJobTitle());
            residenceTextView.setText(employee.getResidence());
        }
    }
}
