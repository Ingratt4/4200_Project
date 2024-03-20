package com.example.a4200_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "your_database_name.db";
    public static final int DATABASE_VERSION = 1;

    // Define your table name and columns
    public static final String TABLE_HIGH_SCORES = "high_scores";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PLAYER_NAME = "player_name";
    public static final String COLUMN_SCORE = "score";

    // SQL statement to create the table
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_HIGH_SCORES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_PLAYER_NAME + " TEXT," +
                    COLUMN_SCORE + " INTEGER)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the table
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if necessary
    }
}
