package com.example.employeemanagementapp;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.text.TextWatcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_EMPLOYEE_REQUEST_CODE = 1;

    private DatabaseHelper dbHelper;
    private SimpleCursorAdapter listAdapter;
    private EmployeeGridAdapter gridAdapter;
    private EditText searchInput;
    private ListView listView;
    private GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        listView = findViewById(R.id.listview);
        gridLayout = findViewById(R.id.gridlayout);

        displayEmployees();

        searchInput = findViewById(R.id.search_input);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                filterEmployeeList(query); // Filter employee list based on the entered text
            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        });


        ImageView menuIcon = findViewById(R.id.image_menu);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleListViewGridLayout();
            }
        });
        ImageView listIcon = findViewById(R.id.list);
        listIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleListViewGridLayout();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                @SuppressLint("Range") long employeeId = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                showEmployeeDetails(employeeId);
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


    @Override
    protected void onResume() {
        super.onResume();
        displayEmployees();
    }
    private void displayEmployees() {
        try {
            Cursor cursor = dbHelper.getAllEmployees();
            if (cursor != null && cursor.moveToFirst()) {
                if (listAdapter == null) {
                    listAdapter = new SimpleCursorAdapter(
                            this,
                            R.layout.list_item_layout,
                            cursor,
                            new String[]{DatabaseHelper.COLUMN_FIRST_NAME, DatabaseHelper.COLUMN_LAST_NAME, DatabaseHelper.COLUMN_JOB},
                            new int[]{R.id.text_name, R.id.text_lastname, R.id.text_job},
                            0);
                    listView.setAdapter(listAdapter);
                } else {
                    listAdapter.changeCursor(cursor);
                }
                listView.setVisibility(View.VISIBLE);
                gridLayout.setVisibility(View.GONE);

                if (gridAdapter == null) {
                    gridAdapter = new EmployeeGridAdapter(this, cursor);
                    updateGridLayout(gridLayout, cursor);
                } else {
                    gridAdapter.changeCursor(cursor);
                    updateGridLayout(gridLayout, cursor);
                }
            } else {
                Log.d("Employee Details", "No employees found in the database.");
            }
        } catch (Exception e) {
            Log.e("Employee Details", "Error accessing database: " + e.getMessage());
        }
    }

    private void filterEmployeeList(String query) {
        Cursor cursor = dbHelper.getAllEmployeesFiltered(query);
        if (cursor != null) {
            listAdapter.changeCursor(cursor);
        }
    }

    @SuppressLint("Range")
    private void updateGridLayout(GridLayout gridLayout, Cursor cursor) {
        gridLayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        while (!cursor.isAfterLast()) {
            View itemView = inflater.inflate(R.layout.grid_item_layout, gridLayout, false);
            TextView nameTextView = itemView.findViewById(R.id.text_name);
            TextView lastNameTextView = itemView.findViewById(R.id.text_lastname);
            TextView jobTextView = itemView.findViewById(R.id.text_job);

            nameTextView.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FIRST_NAME)));
            lastNameTextView.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LAST_NAME)));
            jobTextView.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_JOB)));

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = GridLayout.LayoutParams.WRAP_CONTENT;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(8, 8, 8, 8);
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f);
            itemView.setLayoutParams(params);

            final long employeeId = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEmployeeDetails(employeeId);
                }
            });

            gridLayout.addView(itemView);
            cursor.moveToNext();
        }
    }

    private void showEmployeeDetails(long employeeId) {
        Intent intent = new Intent(MainActivity.this, EmployeeDetails.class);
        intent.putExtra("employeeId", employeeId);
        startActivity(intent);
    }



    private void toggleListViewGridLayout() {
        if (listView.getVisibility() == View.VISIBLE) {
            listView.setVisibility(View.GONE);
            gridLayout.setVisibility(View.VISIBLE);
            findViewById(R.id.list).setVisibility(View.VISIBLE);
            findViewById(R.id.image_menu).setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.VISIBLE);
            gridLayout.setVisibility(View.GONE);
            findViewById(R.id.list).setVisibility(View.GONE);
            findViewById(R.id.image_menu).setVisibility(View.VISIBLE);
        }
    }

   public void GoToSettings(View view){
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_EMPLOYEE_REQUEST_CODE && resultCode == RESULT_OK) {
            displayEmployees();
        }
    }


}
