package com.example.a4200_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover); // Set the content view to the game over screen layout

        // Retrieve the final score from the intent extras
        int finalScore = getIntent().getIntExtra("score", 0);

        // Update the final score TextView with the actual score
        TextView finalScoreTextView = findViewById(R.id.final_score);
        finalScoreTextView.setText("Final Score: " + finalScore);

        // Set up button click listeners
        Button restartButton = findViewById(R.id.restart_button);
        Button highScoresButton = findViewById(R.id.high_scores_button);

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle restart button click (e.g., start a new game)
                Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finish this activity to prevent going back to game over screen
            }
        });

        highScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle high scores button click (e.g., view high scores)
                // You can implement this according to your game's logic
            }
        });
    }
}
