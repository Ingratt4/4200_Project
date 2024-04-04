package com.example.a4200_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
        //dbHelper.wipeHighScores(); //uncomment to wipe hiscores

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the main menu
                Intent mainMenuIntent = new Intent(HighScoresActivity.this, MainActivity.class);
                startActivity(mainMenuIntent);
                finish();
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
        ArrayAdapter<HighScore> adapter = new ArrayAdapter<HighScore>(this, 0, highScores) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Inflate the custom layout for each list item
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.highscore_item, parent, false);
                }

                HighScore highScore = getItem(position);

                TextView textView = convertView.findViewById(R.id.highScoreTextView);

                textView.setText(highScore.toString()); // Make sure your HighScore class has a proper toString() method.

                // Return the completed view to be displayed
                return convertView;
            }
        };

        ListView listView = findViewById(R.id.listViewHighScores);
        listView.setAdapter(adapter);
    }
}

