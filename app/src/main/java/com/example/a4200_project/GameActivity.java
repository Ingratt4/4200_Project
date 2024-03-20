package com.example.a4200_project;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create and set the game view as the content view
        GameView gameView = new GameView(this);
        setContentView(gameView);
    }
}
