package com.example.employeemanagementapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "employees.db";
    private static final int DATABASE_VERSION = 5;
    public static final String TABLE_NAME = "employees";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_RESIDENCE = "residence";
    public static final String COLUMN_JOB = "job";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_FIRST_NAME + " TEXT," +
                    COLUMN_LAST_NAME + " TEXT," +
                    COLUMN_IMAGE + " BLOB," +
                    COLUMN_PHONE_NUMBER + " TEXT," +
                    COLUMN_JOB + " TEXT," +
                    COLUMN_EMAIL + " TEXT," +
                    COLUMN_RESIDENCE + " TEXT)";
    ;
    ;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertEmployee(String firstName, String lastName, byte[] image, String phoneNumber, String email, String residence, String job) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_IMAGE, image);
        values.put(COLUMN_PHONE_NUMBER, phoneNumber);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_RESIDENCE, residence);
        values.put(COLUMN_JOB, job);
        return db.insert(TABLE_NAME, null, values);
    }

    public Cursor getAllEmployees() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public int updateEmployee(long id, String firstName, String lastName, byte[] image, String phoneNumber, String email, String residence, String job) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_IMAGE, image); // Modifier pour accepter un tableau de bytes pour l'image
        values.put(COLUMN_PHONE_NUMBER, phoneNumber);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_RESIDENCE, residence);
        values.put(COLUMN_JOB, job);
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }


    public int deleteEmployee(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }
    public Cursor getEmployeeById(long employeeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                COLUMN_FIRST_NAME,
                COLUMN_LAST_NAME,
                COLUMN_PHONE_NUMBER,
                COLUMN_EMAIL,
                COLUMN_JOB,
                COLUMN_RESIDENCE
        };
        String selection = COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(employeeId)};
        return db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);
    }

    public Cursor getAllEmployeesFiltered(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                COLUMN_ID,
                COLUMN_FIRST_NAME,
                COLUMN_LAST_NAME,
                COLUMN_IMAGE,
                COLUMN_PHONE_NUMBER,
                COLUMN_EMAIL,
                COLUMN_RESIDENCE,
                COLUMN_JOB
        };
        String selection = COLUMN_FIRST_NAME + " LIKE ? OR " +
                COLUMN_LAST_NAME + " LIKE ? " ;
        String[] selectionArgs = new String[]{
                "%" + query + "%", // search for first name
                "%" + query + "%",
        };
        return db.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }

    @SuppressLint("Range")
    public byte[] getEmployeeProfileImage(long employeeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        byte[] imageData = null;

        Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{COLUMN_IMAGE},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(employeeId)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            imageData = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));
            cursor.close();
        }

        return imageData;
    }



}