package com.example.a4200_project;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Pipe {
    private int x, y; // Position of the pipe
    private int width, height; // Size of the pipe
    private Paint paint; // Paint object for drawing the pipe

    public Pipe(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        // Initialize paint object
        paint = new Paint();
        paint.setColor(Color.GREEN); // Set pipe color
        paint.setStyle(Paint.Style.FILL); // Set paint style
    }

    public void update() {
        // Update pipe position (e.g., move it to the left)
        x -= 10; // Assuming 'worldSpeed' is accessible from this class
    }

    public void draw(Canvas canvas) {
        // Draw the pipe on the canvas
        canvas.drawRect(x, y, x + width, y + height, paint);
    }

    public boolean isOffScreen(int screenWidth) {
        // Check if the pipe is off the left side of the screen
        return x + width < 0;
    }

    // Other methods as needed (e.g., getters/setters)
}