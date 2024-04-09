package com.example.employeemanagementapp;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EmployeeDetails extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private boolean isEditMode = false;



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

                setEditTextReadonly((EditText) firstNameTextView);
                setEditTextReadonly((EditText) lastNameTextView);
                setEditTextReadonly((EditText) phoneNumberTextView);
                setEditTextReadonly((EditText) emailTextView);
                setEditTextReadonly((EditText) jobTitleTextView);
                setEditTextReadonly((EditText) residenceTextView);

                cursor.close();
            } else {
                Log.d("Employee Details", "No employee found with ID: " + employeeId);
            }
        } else {
            Log.d("Employee Details", "Invalid employee ID");
        }
    }


    private void setEditTextReadonly(EditText editText) {
        editText.setFocusable(false);
        editText.setTextColor(getResources().getColor(android.R.color.darker_gray));
    }


    public void toggleEditMode(View view) {
        isEditMode = !isEditMode;

        TextView firstNameTextView = findViewById(R.id.edittext_first_name);
        TextView lastNameTextView = findViewById(R.id.edittext_last_name);
        TextView phoneNumberTextView = findViewById(R.id.edittext_phone_number);
        TextView emailTextView = findViewById(R.id.edittext_email);
        TextView jobTitleTextView = findViewById(R.id.edittext_job);
        TextView residenceTextView = findViewById(R.id.edittext_residence);

        if (isEditMode) {

            setEditTextEditable((EditText) firstNameTextView);
            setEditTextEditable((EditText) lastNameTextView);
            setEditTextEditable((EditText) phoneNumberTextView);
            setEditTextEditable((EditText) emailTextView);
            setEditTextEditable((EditText) jobTitleTextView);
            setEditTextEditable((EditText) residenceTextView);
        } else {
            setEditTextReadonly((EditText) firstNameTextView);
            setEditTextReadonly((EditText) lastNameTextView);
            setEditTextReadonly((EditText) phoneNumberTextView);
            setEditTextReadonly((EditText) emailTextView);
            setEditTextReadonly((EditText) jobTitleTextView);
            setEditTextReadonly((EditText) residenceTextView);
        }
    }


    private void setEditTextEditable(EditText editText) {
        editText.setFocusableInTouchMode(true);
        editText.setTextColor(getResources().getColor(android.R.color.black));
    }

    public void goBack(View view) {
        finish();
    }

    public void SendMail(View view) {
        TextView emailTextView = findViewById(R.id.edittext_email);
        String recipientEmail = emailTextView.getText().toString();

        Log.d("Recipient Email", recipientEmail);
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + Uri.encode(recipientEmail)));
            intent.putExtra(Intent.EXTRA_EMAIL,new String[] {recipientEmail});

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }  else {
            Toast.makeText(getApplicationContext(), "No email app found. Please install an email app.", Toast.LENGTH_SHORT).show();
        }
    }

    public void MakeCall(View view ) {
        TextView phonetextView = findViewById(R.id.edittext_phone_number);
        String recipientPhone = phonetextView.getText().toString();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + recipientPhone));
        startActivity(intent);
    }

    public void MakeSMS(View view ) {
        TextView phonetextView = findViewById(R.id.edittext_phone_number);
        String recipientPhone = phonetextView.getText().toString();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("sms:" + recipientPhone));
        startActivity(intent);
    }

    public void deleteEmployee(View view) {
        long employeeId = getIntent().getLongExtra("employeeId", -1);
        if (employeeId != -1) {
            int rowsDeleted = dbHelper.deleteEmployee(employeeId);
            if (rowsDeleted > 0) {
                Toast.makeText(this, "Employee deleted successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear the back stack
                startActivity(intent);// Close the activity after successful deletion
            } else {
                Toast.makeText(this, "Failed to delete employee", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d("Delete Employee", "Invalid employee ID");
        }
    }





}