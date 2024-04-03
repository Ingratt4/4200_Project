package com.example.a4200_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {


    private static final long PIPE_SPAWN_INTERVAL = 3300; // Interval between pipe spawns (milliseconds)
    private SurfaceHolder surfaceHolder;
    private boolean isGameOver = false;
    private int playerX, playerY;
    private int gravity = 10;
    private int score = 0;
    private int worldSpeed = 10;
    private Bitmap backgroundBitmap;
    private static final int MAX_PIPES = 5; // Maximum number of active pipes on the screen
    private Bitmap pipeBitmap, pipeTopBitmap;
    private Bitmap birdBitmap, birdUpBitmap, birdDownBitmap;

    private int backgroundWidth, backgroundHeight;
    private int bgX;
    private List<Pipe> pipes = new ArrayList<>();
    private long lastPipeSpawnTime;

    private int velocity = 0;
    private final int jumpStrength = -40; // Negative value to move up

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
        birdUpBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bird_up);
        birdDownBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bird_down);


        int birdWidth = 100;
        int birdHeight = 100;
        birdBitmap = Bitmap.createScaledBitmap(birdBitmap, birdWidth, birdHeight, false);
        birdUpBitmap = Bitmap.createScaledBitmap(birdUpBitmap, birdWidth, birdHeight, false);
        birdDownBitmap = Bitmap.createScaledBitmap(birdDownBitmap, birdWidth, birdHeight, false);

        // Get the dimensions of the screen
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;


        // Initialize player position dynamically
        playerX = screenWidth / 4; // Initial X position (1/4 of the screen width)
        playerY = screenHeight / 2; // Initial Y position (center of the screen)

        // Initialize background dimensions
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background_image);
        backgroundWidth = screenWidth; // Set background width to screen width
        backgroundHeight = screenHeight; // Set background height to screen height
        bgX = 0;

        pipeTopBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pipe_top);
        pipeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pipe_bottom);




    }


    private void generatePipe() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPipeSpawnTime >= PIPE_SPAWN_INTERVAL) {
            int screenWidth = getWidth();
            int screenHeight = getHeight();
            int pipeWidth = 200; // Adjust based on your design preferences
            // Randomize the gap size between 200 and 500 pixels
            int gapSize = new Random().nextInt(301) + 200; // (Max - Min + 1) + Min to ensure the range is 200 to 500 inclusive
            int minPipeHeight = 100; // Minimum visible height of a pipe
            int maxPipeHeight = screenHeight - gapSize - minPipeHeight;
            int topPipeHeight = new Random().nextInt(maxPipeHeight - minPipeHeight) + minPipeHeight;
            int bottomPipeHeight = screenHeight - topPipeHeight - gapSize;
            int pipeX = screenWidth; // Spawn pipes off-screen to the right

            // Since you're using separate images for top and bottom pipes and assuming they can be scaled uniformly,
            // Create scaled versions of the pipe bitmaps for this pair of pipes
            Bitmap scaledTopPipeBitmap = Bitmap.createScaledBitmap(pipeTopBitmap, pipeWidth, topPipeHeight, false);
            Bitmap scaledBottomPipeBitmap = Bitmap.createScaledBitmap(pipeBitmap, pipeWidth, bottomPipeHeight, false);

            // Calculate Y positions for the top and bottom pipes
            int topPipeY = 0; // Top pipe's Y position starts at the top of the screen
            int bottomPipeY = screenHeight - bottomPipeHeight; // Bottom pipe's Y is calculated from the bottom up

            // Create and add the pipe objects
            Pipe topPipe = new Pipe(pipeX, topPipeY, pipeWidth, topPipeHeight, scaledTopPipeBitmap);
            Pipe bottomPipe = new Pipe(pipeX, bottomPipeY, pipeWidth, bottomPipeHeight, scaledBottomPipeBitmap);

            pipes.add(topPipe);
            pipes.add(bottomPipe);

            lastPipeSpawnTime = currentTime;
        }
    }


    private void movePipes(int screenWidth) {
        Iterator<Pipe> iterator = pipes.iterator();
        while (iterator.hasNext()) {
            Pipe pipe = iterator.next();
            pipe.update(worldSpeed); // Assuming update() method moves the pipe to the left
            if (pipe.isOffScreen(screenWidth)) {
                iterator.remove(); // Remove the pipe if it's off the screen
            }
        }
    }

    private void checkForScore() {
        for (int i = 0; i < pipes.size(); i+=2) { // Increment by 2 to check every other pipe
            Pipe pipe = pipes.get(i);
            int birdCenterX = playerX + birdBitmap.getWidth() / 2;
            // Assuming getX() gives the left edge of the pipe and you score as the bird crosses the left edge
            if (!pipe.scored && birdCenterX > pipe.getX() + pipe.getWidth()) {
                score++; // Increment score for passing a pair
                pipe.scored = true; // Mark this pipe as scored
                pipes.get(i+1).scored = true; // Also mark its pair as scored, if pairs are consecutive
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
        if (!isGameOver && event.getAction() == MotionEvent.ACTION_DOWN) {
            jump();
            return true;
        }
        return false;
    }


    private void resetGameState() {
        // Reset game state for a new game
        score = 0;
        pipes.clear();
        playerY = getHeight() / 2;
        isGameOver = false;
        running = true;
    }

    private void checkCollisionsAndGameOver() {
        if (checkCollision()) {
            gameOver(); // If collision detected, end the game
        }
    }

    private void gameOver() {
        running = false; // Stop game loop

        // Create an Intent for EnterNameActivity
        Intent enterNameIntent = new Intent(getContext(), EnterNameActivity.class);
        enterNameIntent.putExtra("score", score); // Pass the final score to the name entry activity
        getContext().startActivity(enterNameIntent);
    }




    private boolean checkCollision() {
        for (Pipe pipe : pipes) {
            // Assuming Pipe class has methods getX(), getY(), getWidth(), and getHeight() for collision detection
            // Check for collision with the top and bottom pipes
            if (playerX < pipe.getX() + pipe.getWidth() &&
                    playerX + birdBitmap.getWidth() > pipe.getX() &&
                    playerY < pipe.getY() + pipe.getHeight() &&
                    playerY + birdBitmap.getHeight() > pipe.getY()) {
                return true; // Collision detected
            }
        }

        // Check if the bird has fallen out of bounds
        if (playerY < 0 || playerY + birdBitmap.getHeight() > getHeight()) {
            return true; // Out of bounds
        }

        return false; // No collision
    }
    private void drawGameOver(Canvas canvas) {
        Paint gameOverPaint = new Paint();
        gameOverPaint.setColor(Color.WHITE);
        gameOverPaint.setTextSize(100);
        gameOverPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Game Over", getWidth() / 2, getHeight() / 2, gameOverPaint);

        Paint scorePaint = new Paint();
        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(50);
        scorePaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Score: " + score, getWidth() / 2, (getHeight() / 2) + 60, scorePaint);

        // Draw "Play Again" and "Hiscores" buttons or text placeholders
    }


    private void draw() {
        Canvas canvas = surfaceHolder.lockCanvas();
        if (canvas != null) {
            try {
                canvas.drawBitmap(backgroundBitmap, bgX, 0, null);

                // Choose the correct bird image
                Bitmap currentBirdBitmap = birdBitmap;
                if (velocity < 0) {
                    currentBirdBitmap = birdUpBitmap; // Ascending
                } else if (velocity > 0) {
                    currentBirdBitmap = birdDownBitmap; // Descending
                }

                // Draw the player bird
                canvas.drawBitmap(currentBirdBitmap, playerX, playerY, null);

                for (Pipe pipe : pipes) {
                    pipe.draw(canvas);
                }
                Paint scorePaint = new Paint();
                scorePaint.setColor(Color.WHITE);
                scorePaint.setTextSize(50);
                scorePaint.setAntiAlias(true);

                canvas.drawText("Score: " + score, canvas.getWidth() / 2f, 100, scorePaint);

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
                // Check for collision and handle game over
                checkCollisionsAndGameOver();

                // Update player's vertical position for gravity effect
                velocity += gravity; // Gravity pulls down, increasing the downward velocity
                playerY += velocity;
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
            double loopingPointX = -backgroundWidth / 1.05; // Adjust this value according to your background image
            while (running && !Thread.currentThread().isInterrupted()) {
                // Move the background
                bgX -= worldSpeed;
                if (bgX <= loopingPointX) {
                    bgX = 0; // Reset background position
                }

                // Check for collision and handle game over
                checkCollisionsAndGameOver();

                // Move the pipes every frame
                movePipes(getWidth());

                // Redraw the canvas to reflect changes
                draw();

                // Sleep to control frame rate
                try {
                    Thread.sleep(16); // Aim for ~60 FPS
                } catch (InterruptedException e) {
                    Thread.currentThread().isInterrupted();
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
                checkForScore();

                // Check for collision and handle game over
                checkCollisionsAndGameOver();

                // Redraw the canvas
                draw();

                try {
                    Thread.sleep(PIPE_SPAWN_INTERVAL); // Adjust the sleep time as needed
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    });
    private void jump() {
        velocity = jumpStrength; // This will make the bird "jump" upwards
        draw();
    }
}
