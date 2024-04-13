package com.example.employeemanagementapp;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

public class EmployeeDetails extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private boolean isEditMode = false;
    private ImageView  profileImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyLanguage();
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

                // Inside onCreate() method after populating other fields
            profileImageView = findViewById(R.id.image_profile2);
                profileImageView.setImageResource(R.drawable.ic_launcher_background);

                if (employeeId != -1) {
                    byte[] imageData = dbHelper.getEmployeeProfileImage(employeeId);
                    if (imageData != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                        profileImageView.setImageBitmap(bitmap);
                    } else {
                        profileImageView.setImageResource(R.drawable.rounded_button_background); // Default image if no image found
                    }
                }

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
    public byte[] convertImageToByteArray() {
        Bitmap bmp = ((BitmapDrawable) profileImageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
    public void openCamera2(View view) {
        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);
    }
    public void openGallery2(View view) {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        Bitmap bmp = (Bitmap) bundle.get("data");
                        if (bmp != null) {
                            Bitmap resized = Bitmap.createScaledBitmap(bmp, 100, 100, true);
                            profileImageView.setImageBitmap(resized);
                        }
                    }
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        profileImageView.setImageURI(selectedImage);
                    }
                }
                break;
        }
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
    public void updateEmployee(View view) {
        long employeeId = getIntent().getLongExtra("employeeId", -1);

        if (employeeId != -1) {
            EditText firstNameEditText = findViewById(R.id.edittext_first_name);
            EditText lastNameEditText = findViewById(R.id.edittext_last_name);
            EditText phoneNumberEditText = findViewById(R.id.edittext_phone_number);
            EditText emailEditText = findViewById(R.id.edittext_email);
            EditText jobEditText = findViewById(R.id.edittext_job);
            EditText residenceEditText = findViewById(R.id.edittext_residence);

            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String phoneNumber = phoneNumberEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String job = jobEditText.getText().toString();
            String residence = residenceEditText.getText().toString();
            byte[] imageBytes = convertImageToByteArray();
            if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) ||
                    TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(email) ||
                    TextUtils.isEmpty(job) || TextUtils.isEmpty(residence)) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Mettez à jour l'employé dans la base de données
            int rowsAffected = dbHelper.updateEmployee(employeeId, firstName, lastName, imageBytes, phoneNumber, email, job, residence);
            if (rowsAffected > 0) {
                Toast.makeText(this, "Employee updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update employee", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d("Update Employee", "Invalid employee ID");
        }
    }


    private void applyLanguage() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedLanguage = preferences.getString("selected_language", "");
        Log.d("selected language",selectedLanguage);
        Locale newLocale;
        if (selectedLanguage.equals("العربية")) {
            newLocale = new Locale("ar");
            Locale.setDefault(newLocale);
            Configuration config = new Configuration();
            config.setLocale(newLocale);
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());

        } else {
            newLocale = Locale.ENGLISH;
            Locale.setDefault(newLocale);
            Configuration config = new Configuration();
            config.setLocale(newLocale);
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());

        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Handle configuration changes here, if needed
        // This method will be called automatically when the language configuration is changed
    }



}