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
    private String playerName; // Declare playerName variable here

    static {
        System.loadLibrary("a4200_project");
    }

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
                // Handle High Scores button click
                // Navigate to the high scores screen
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            hideSystemUi();
        }
    }

    private void hideSystemUi() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

    // Method to insert a high score into the database
    private void insertHighScore(String playerName, int score) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PLAYER_NAME, playerName);
        values.put(DatabaseHelper.COLUMN_SCORE, score);

        long newRowId = db.insert(DatabaseHelper.TABLE_HIGH_SCORES, null, values);
    }


}
