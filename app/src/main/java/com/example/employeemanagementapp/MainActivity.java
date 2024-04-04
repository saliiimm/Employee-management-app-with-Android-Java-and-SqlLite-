package com.example.employeemanagementapp;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_EMPLOYEE_REQUEST_CODE = 1;

    private DatabaseHelper dbHelper;
    private SimpleCursorAdapter adapter;
    private EditText searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);


        ListView listView = findViewById(R.id.listview);
        displayEmployees(listView);

        searchInput = findViewById(R.id.search_input);

        ImageView searchIcon = findViewById(R.id.search);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSearchInputVisibility();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                @SuppressLint("Range") long employeeId = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));

                Intent intent = new Intent(MainActivity.this, EmployeeDetails.class);

                intent.putExtra("employeeId", employeeId);
                startActivity(intent);
            }
        });

        findViewById(R.id.button_add_employee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEmployeeActivity.class);
                startActivityForResult(intent, ADD_EMPLOYEE_REQUEST_CODE);
            }
        });
    }

    private void displayEmployees(ListView listView) {
        try {
            Cursor cursor = dbHelper.getAllEmployees();
            if (cursor != null && cursor.moveToFirst()) {
                if (adapter == null) {
                    adapter = new SimpleCursorAdapter(
                            this,
                            R.layout.list_item_layout,
                            cursor,
                            new String[]{DatabaseHelper.COLUMN_FIRST_NAME, DatabaseHelper.COLUMN_LAST_NAME,  DatabaseHelper.COLUMN_JOB},
                            new int[]{R.id.text_name,R.id.text_lastname, R.id.text_job},
                            0);
                    listView.setAdapter(adapter);
                } else {
                    adapter.changeCursor(cursor);
                }
            } else {
                Log.d("Employee Details", "No employees found in the database.");
            }
        } catch (Exception e) {
            Log.e("Employee Details", "Error accessing database: " + e.getMessage());
        }
    }

    private void toggleSearchInputVisibility() {
        if (searchInput.getVisibility() == View.VISIBLE) {
            searchInput.setVisibility(View.GONE);
        } else {
            searchInput.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_EMPLOYEE_REQUEST_CODE && resultCode == RESULT_OK) {
            ListView listView = findViewById(R.id.listview);
            displayEmployees(listView);
        }
    }

}
