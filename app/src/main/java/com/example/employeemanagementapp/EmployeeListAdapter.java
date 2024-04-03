package com.example.employeemanagementapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EmployeeListAdapter extends ArrayAdapter<Employee> {

    public EmployeeListAdapter(Context context, ArrayList<Employee> employees) {
        super(context, 0, employees);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Employee employee = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_layout, parent, false);
        }

        TextView firstLastNameTextView = convertView.findViewById(R.id.text_name);
        TextView jobTitleTextView = convertView.findViewById(R.id.text_job);
        firstLastNameTextView.setText(employee.getFirstName() + " " + employee.getLastName());
        jobTitleTextView.setText(employee.getJobTitle());

        return convertView;
    }
}