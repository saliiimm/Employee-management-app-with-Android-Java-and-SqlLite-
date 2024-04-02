package com.example.employeemanagementapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        ListView listView = findViewById(R.id.listview);
        displayEmployees(listView);


        findViewById(R.id.button_add_employee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEmployeeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void displayEmployees(ListView listView) {
        try {
            Cursor cursor = dbHelper.getAllEmployees();
            if (cursor != null && cursor.moveToFirst()) {
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                        this,
                        android.R.layout.simple_list_item_2,
                        cursor,
                        new String[]{DatabaseHelper.COLUMN_FIRST_NAME, DatabaseHelper.COLUMN_LAST_NAME},
                        new int[]{android.R.id.text1, android.R.id.text2},
                        0);
                listView.setAdapter(adapter);
            } else {
                Log.d("Employee Details", "No employees found in the database.");
            }
        } catch (Exception e) {
            Log.e("Employee Details", "Error accessing database: " + e.getMessage());
        }
    }

}
