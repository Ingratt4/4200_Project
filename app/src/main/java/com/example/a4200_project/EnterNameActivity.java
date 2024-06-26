package com.example.a4200_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a4200_project.DatabaseHelper;

public class EnterNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);

        int score = getIntent().getIntExtra("score", 0); // Retrieve the final score from the intent extras

        EditText nameEditText = findViewById(R.id.name_edit_text);
        Button submitButton = findViewById(R.id.submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerName = nameEditText.getText().toString().trim();


                if (!playerName.isEmpty()) {

                    DatabaseHelper dbHelper = new DatabaseHelper(EnterNameActivity.this);
                    dbHelper.insertHighScore(playerName, score);


                    Intent highScoresIntent = new Intent(EnterNameActivity.this, HighScoresActivity.class);
                    startActivity(highScoresIntent);


                    finish();
                } else {

                }
            }
        });
    }
}
