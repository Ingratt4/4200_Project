package com.example.a4200_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private int gravity = 10;
    private int worldSpeed = 5;
    private Bitmap backgroundBitmap;
    private int backgroundWidth, backgroundHeight;
    private int bgX;



    private boolean running = true; // Flag to control the game loop

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

        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background_image);
        backgroundWidth = backgroundBitmap.getWidth();
        backgroundHeight = backgroundBitmap.getHeight();
        bgX = 0;


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Start drawing when the surface is created
        draw();
        startGameLoop();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Respond to surface changes here
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopGameLoop();
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
                canvas.drawBitmap(backgroundBitmap, bgX, 0, null);

                // Draw the player square
                canvas.drawRect(playerX, playerY, playerX + playerSize, playerY + playerSize, playerPaint);
            } finally {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void startGameLoop() {
        running = true;
        gravityThread.start();
        worldMovementThread.start();
    }

    private void stopGameLoop() {
        running = false;
        gravityThread.interrupt();
        worldMovementThread.interrupt();
    }

    private Thread gravityThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (running && !Thread.currentThread().isInterrupted()) {
                // Update player's vertical position for gravity effect
                playerY += gravity;
                // Redraw the canvas
                draw();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    });

    private Thread worldMovementThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (running && !Thread.currentThread().isInterrupted()) {
                // Update world position for side-scrolling effect
                // Move background to the left
                bgX -= worldSpeed;
                if (bgX <= -backgroundWidth) {
                    bgX = 0; // Reset background position when it reaches the end
                }
                // Redraw the canvas
                draw();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    });

    private void jump() {
        // Move the player upwards (jump)
        playerY -= 100; // Adjust the value as needed
        // Redraw the canvas after the jump
        draw();
    }
}
