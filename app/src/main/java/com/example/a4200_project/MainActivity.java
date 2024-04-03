package com.example.a4200_project;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu); // Set the content view to main menu layout

        // Initialize the database helper
        dbHelper = new DatabaseHelper(this);

        // Initialize buttons
        Button startButton = findViewById(R.id.start_button);
        Button highScoresButton = findViewById(R.id.high_scores_button);

        // Set click listeners for buttons
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        highScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HighScoresActivity.class);
                startActivity(intent);
            }
        });
    }

    // Insert high score into the database
    private void insertHighScore(String playerName, int score) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PLAYER_NAME, playerName);
        values.put(DatabaseHelper.COLUMN_SCORE, score);

        long newRowId = db.insert(DatabaseHelper.TABLE_HIGH_SCORES, null, values);
    }
}

