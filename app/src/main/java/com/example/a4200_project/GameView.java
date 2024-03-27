package com.example.a4200_project;

import android.content.Context;
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
    private static final int GAP_SIZE =400 ;


    private SurfaceHolder surfaceHolder;
    private int playerX, playerY; // Player's position

    private int gravity = 10;

    private int gapSize = 200; // You can adjust this value as needed

    private int score = 0; // Player's score
    private int worldSpeed = 10;
    private Bitmap backgroundBitmap;
    private static final int MAX_PIPES = 5; // Maximum number of active pipes on the screen
    private Bitmap pipeBitmap, pipeTopBitmap;
    private Bitmap birdBitmap, birdUpBitmap, birdDownBitmap;

    private int backgroundWidth, backgroundHeight;
    private int bgX;
    private List<Pipe> pipes = new ArrayList<>(); // List to hold active pipes
    private long lastPipeSpawnTime; // Timestamp of the last pipe spawn

    private int velocity = 0; // Player's velocity
    private final int jumpStrength = -40; // Negative value to move up


    private boolean running = true; // Flag to control the game loop
    private int gapHeight = 200; // The vertical gap between the top and bottom pipes
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

        pipeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pipe);

        Matrix matrix = new Matrix();
        matrix.setScale(1, -1); // Flip vertically
        pipeTopBitmap = Bitmap.createBitmap(pipeBitmap, 0, 0, pipeBitmap.getWidth(), pipeBitmap.getHeight(), matrix, true);




    }


    private void generatePipe() {
        Log.d("PipeDebug", "Generating Pipe...");
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPipeSpawnTime >= PIPE_SPAWN_INTERVAL) {
            int screenWidth = getWidth();
            int screenHeight = getHeight();
            int pipeWidth = 200; // Desired width for the pipes
            int gapSize = 300; // Fixed gap size between the top and bottom pipes
            int minPipeHeight = 100; // Minimum height for the pipes
            int maxPipeHeight = screenHeight - gapSize - minPipeHeight; // Maximum height for the pipes to maintain a gap
            int pipeHeight = new Random().nextInt(maxPipeHeight - minPipeHeight) + minPipeHeight; // Random height for the pipes
            int pipeX = screenWidth; // Pipes spawn off-screen to the right

            // Calculate the Y position of the bottom pipe based on the gap size and top pipe height
            int bottomPipeY = pipeHeight + gapSize;

            Log.d("PipeDebug", "Screen Height: " + screenHeight);
            Log.d("PipeDebug", "Pipe Height: " + pipeHeight);
            Log.d("PipeDebug", "Gap Size: " + gapSize);
            Log.d("PipeDebug", "Bottom Pipe Y: " + bottomPipeY);

            // Resize the pipeBitmap and pipeTopBitmap to match the desired pipe dimensions
            pipeBitmap = Bitmap.createScaledBitmap(pipeBitmap, pipeWidth, pipeHeight, false);
            pipeTopBitmap = Bitmap.createScaledBitmap(pipeTopBitmap, pipeWidth, screenHeight - bottomPipeY, false);

            // Create and add pipes to the list
            Pipe topPipe = new Pipe(pipeX, 0, pipeWidth, screenHeight - bottomPipeY, pipeTopBitmap);
            Pipe bottomPipe = new Pipe(pipeX, bottomPipeY, pipeWidth, pipeHeight, pipeBitmap);

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
