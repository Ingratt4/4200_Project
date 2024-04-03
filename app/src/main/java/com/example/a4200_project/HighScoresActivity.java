package com.example.a4200_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class HighScoresActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        dbHelper = new DatabaseHelper(this);
        displayHighScores();

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the main menu
                Intent mainMenuIntent = new Intent(HighScoresActivity.this, MainActivity.class);
                startActivity(mainMenuIntent);
                finish(); // Close this activity
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayHighScores();
    }

    private void displayHighScores() {
        List<HighScore> highScores = dbHelper.getAllHighScores();
        ArrayAdapter<HighScore> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, highScores);
        ListView listView = findViewById(R.id.listViewHighScores);
        listView.setAdapter(adapter);
    }
}
