package com.example.employeemanagementapp;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EmployeeGridAdapter extends BaseAdapter {

    private Context mContext;
    private Cursor mCursor;

    public EmployeeGridAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public void changeCursor(Cursor cursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_layout, parent, false);
            holder = new ViewHolder();
            holder.nameTextView = convertView.findViewById(R.id.text_name);
            holder.lastNameTextView = convertView.findViewById(R.id.text_lastname);
            holder.jobTextView = convertView.findViewById(R.id.text_job);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mCursor.moveToPosition(position)) {
            @SuppressLint("Range") String firstName = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_FIRST_NAME));
            @SuppressLint("Range") String lastName = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_LAST_NAME));
            @SuppressLint("Range") String job = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.COLUMN_JOB));

            holder.nameTextView.setText(firstName);
            holder.lastNameTextView.setText(lastName);
            holder.jobTextView.setText(job);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView nameTextView;
        TextView lastNameTextView;
        TextView jobTextView;
    }
}
