package com.example.a4200_project;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Pipe {
    private int x, y; // Position of the pipe
    private int width, height; // Size of the pipe

    private Bitmap bitmap; // The image for this pipe
    public boolean scored = false; // Add this line

    public Pipe(int x, int y, int width, int height, Bitmap bitmap) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bitmap = bitmap;

        // Initialize paint object

    }

    public void update(int speed) {
        // Update pipe position (e.g., move it to the left)
        x -= speed; // Assuming 'worldSpeed' is accessible from this class
        Log.d("PipeMove", "Moving pipe at speed: " + speed);
    }

    public void draw(Canvas canvas) {
        // Draw the pipe on the canvas
        canvas.drawBitmap(bitmap,x,y,null);
    }

    public boolean isOffScreen(int screenWidth) {
        // Check if the pipe is off the left side of the screen
        return x + width < 0;
    }

    public int getX() {
        return x;
    }

    public int getWidth() {
        return width;
    }

    public int getY() {
        return y;
    }

    // Other methods as needed (e.g., getters/setters)
}