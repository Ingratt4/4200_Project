package com.example.a4200_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.a4200_project.HighScore;

import java.util.ArrayList;
import java.util.List;

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

    public void insertHighScore(String playerName, int score) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER_NAME, playerName);
        values.put(COLUMN_SCORE, score);

        db.insert(TABLE_HIGH_SCORES, null, values);
        db.close();
    }

    public List<HighScore> getAllHighScores() {
        List<HighScore> highScores = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_HIGH_SCORES,
                null,
                null,
                null,
                null,
                null,
                COLUMN_SCORE + " DESC"); // Order by score descending

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String playerName = cursor.getString(cursor.getColumnIndex(COLUMN_PLAYER_NAME));
                int score = cursor.getInt(cursor.getColumnIndex(COLUMN_SCORE));
                HighScore highScore = new HighScore(id, playerName, score);
                highScores.add(highScore);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return highScores;
    }
}
