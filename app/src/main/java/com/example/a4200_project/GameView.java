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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {


    private static final long PIPE_SPAWN_INTERVAL = 2000; // Interval between pipe spawns (milliseconds)


    private SurfaceHolder surfaceHolder;
    private Paint playerPaint;
    private int playerX, playerY; // Player's position
    private int playerSize = 100; // Size of the player square
    private int gravity = 10;
    private int worldSpeed = 5;
    private Bitmap backgroundBitmap;
    private Bitmap birdBitmap;
    private int backgroundWidth, backgroundHeight;
    private int bgX;
    private List<Pipe> pipes = new ArrayList<>(); // List to hold active pipes
    private long lastPipeSpawnTime; // Timestamp of the last pipe spawn



    private boolean running = true; // Flag to control the game loop

    public GameView(Context context) {
        this(context, null);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        // Initialize player paint
        birdBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bird);
        int birdWidth = 250;
        int birdHeight = 250;
        birdBitmap = Bitmap.createScaledBitmap(birdBitmap, birdWidth, birdHeight, false);


        // Initialize player position
        playerX = 100; // Initial X position
        playerY = 100; // Initial Y position

        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background_image);
        backgroundWidth = backgroundBitmap.getWidth();
        backgroundHeight = backgroundBitmap.getHeight();
        bgX = 0;


    }

    private void generatePipe() {
        // Generate a new pair of pipes if enough time has passed since the last spawn
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPipeSpawnTime > PIPE_SPAWN_INTERVAL) {
            int screenHeight = getHeight(); // Get the height of the screen
            int pipeWidth = 100;
            int gapHeight = 300; // Height of the gap between the pipes

            // Calculate the top pipe's position
            int topPipeY = (int) (Math.random() * (screenHeight - gapHeight)); // Random Y position for top pipe

            // Create the top pipe
            Pipe topPipe = new Pipe(getWidth(), 0, pipeWidth, topPipeY);

            // Calculate the bottom pipe's position and height based on the gap
            int bottomPipeY = topPipeY + gapHeight;
            int bottomPipeHeight = screenHeight - bottomPipeY;

            // Create the bottom pipe
            Pipe bottomPipe = new Pipe(getWidth(), bottomPipeY, pipeWidth, bottomPipeHeight);

            // Add the pipes to the list
            pipes.add(topPipe);
            pipes.add(bottomPipe);

            // Update the last spawn time
            lastPipeSpawnTime = currentTime;
        }
    }


    private void movePipes(int screenWidth) {
        Iterator<Pipe> iterator = pipes.iterator();
        while (iterator.hasNext()) {
            Pipe pipe = iterator.next();
            pipe.update(); // Update pipe position
            if (pipe.isOffScreen(screenWidth)) {
                iterator.remove(); // Remove pipe if off screen
            }
        }
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

                // Draw the player bird
                canvas.drawBitmap(birdBitmap, playerX, playerY, null);

                // Draw pipes
                for (Pipe pipe : pipes) {
                    pipe.draw(canvas);
                }
            } finally {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void startGameLoop() {
        running = true;
        gravityThread.start();
        pipeThread.start();
        worldMovementThread.start();

    }

    private void stopGameLoop() {
        running = false;
        gravityThread.interrupt();
        pipeThread.interrupt();
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
            int loopingPointX = -1000; // Adjust this value according to your game's design
            while (running && !Thread.currentThread().isInterrupted()) {
                // Update world position for side-scrolling effect
                bgX -= worldSpeed;

                // Check if the background image has reached the looping point
                if (bgX <= loopingPointX) {
                    // Reset background position to start scrolling from the beginning
                    bgX = 0; // Or set it to a specific position if needed
                }

                // Redraw the canvas
                draw();

                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    });

    private Thread pipeThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (running && !Thread.currentThread().isInterrupted()) {
                // Game loop logic
                generatePipe();
                movePipes(getWidth()); // Pass the screen width to movePipes
                // Other game logic

                // Redraw the canvas
                draw();

                try {
                    Thread.sleep(30); // Adjust the sleep time as needed
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
