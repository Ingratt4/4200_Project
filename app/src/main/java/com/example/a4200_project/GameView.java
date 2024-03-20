package com.example.a4200_project;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder;
    private Paint playerPaint;
    private int playerX, playerY; // Player's position
    private int playerSize = 100; // Size of the player square

    public GameView(Context context) {
        this(context, null);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        // Initialize player paint
        playerPaint = new Paint();
        playerPaint.setColor(Color.BLUE); // Player color

        // Initialize player position
        playerX = 100; // Initial X position
        playerY = 100; // Initial Y position
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Start drawing when the surface is created
        draw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Respond to surface changes here
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Clean up resources when surface is destroyed
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Handle touch events
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Jump when touched
                jump();
                break;
        }
        return true;
    }

    private void draw() {
        Canvas canvas = surfaceHolder.lockCanvas();
        if (canvas != null) {
            try {
                // Clear the canvas
                canvas.drawColor(Color.WHITE);

                // Draw the player square
                canvas.drawRect(playerX, playerY, playerX + playerSize, playerY + playerSize, playerPaint);
            } finally {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void jump() {
        // Move the player upwards (jump)
        playerY -= 100; // Adjust the value as needed
        // Redraw the canvas after the jump
        draw();
    }
}
