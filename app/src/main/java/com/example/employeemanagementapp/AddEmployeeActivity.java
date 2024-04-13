package com.example.employeemanagementapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

public class AddEmployeeActivity extends AppCompatActivity {

    private EditText editTextFirstName, editTextLastName, editTextPhoneNumber, editTextEmail, editTextResidence, editTextJob;
    private ImageView imageViewValidate, imageViewBack, imageView;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyLanguage();
        setContentView(R.layout.activity_add_employee);

        editTextFirstName = findViewById(R.id.edittext_first_name);
        editTextLastName = findViewById(R.id.edittext_last_name);
        editTextPhoneNumber = findViewById(R.id.edittext_phone_number);
        editTextEmail = findViewById(R.id.edittext_email);
        editTextResidence = findViewById(R.id.edittext_residence);
        editTextJob = findViewById(R.id.edittext_job);
        imageViewValidate = findViewById(R.id.image_validate);
        imageViewBack = findViewById(R.id.image_back);
        imageView = findViewById(R.id.image_profile);
        imageView.setImageResource(R.drawable.ic_launcher_background);

        databaseHelper = new DatabaseHelper(this);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageViewValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEmployee();
            }
        });
    }

    public byte[] convertImageToByteArray() {
        Bitmap bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public void openCamera(View view) {
        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);
    }

    public void openGallery(View view) {
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
                            imageView.setImageBitmap(resized);
                        }
                    }
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        imageView.setImageURI(selectedImage);
                    }
                }
                break;
        }
    }

    private void addEmployee() {
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String residence = editTextResidence.getText().toString().trim();
        String job = editTextJob.getText().toString().trim();
        byte[] imageBytes = convertImageToByteArray();

        if (firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || residence.isEmpty() || job.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        long result = databaseHelper.insertEmployee(firstName, lastName, imageBytes, phoneNumber, email, residence, job);

        if (result != -1) {
            Toast.makeText(this, "Employee added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add employee", Toast.LENGTH_SHORT).show();
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
