package com.example.employeemanagementapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddEmployeeActivity extends AppCompatActivity {

    private EditText editTextFirstName, editTextLastName, editTextPhoneNumber, editTextEmail, editTextResidence, editTextJob;
    private ImageView imageViewValidate;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        editTextFirstName = findViewById(R.id.edittext_first_name);
        editTextLastName = findViewById(R.id.edittext_last_name);
        editTextPhoneNumber = findViewById(R.id.edittext_phone_number);
        editTextEmail = findViewById(R.id.edittext_email);
        editTextResidence = findViewById(R.id.edittext_residence);
        editTextJob = findViewById(R.id.edittext_job);
        imageViewValidate = findViewById(R.id.image_validate);

        databaseHelper = new DatabaseHelper(this);

        imageViewValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEmployee();
            }
        });
    }

    private void addEmployee() {
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String residence = editTextResidence.getText().toString().trim();
        String job = editTextJob.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || residence.isEmpty() || job.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        long result = databaseHelper.insertEmployee(firstName, lastName, null, phoneNumber, email, residence, job);

        if (result != -1) {
            Toast.makeText(this, "Employee added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add employee", Toast.LENGTH_SHORT).show();
        }
    }
}
