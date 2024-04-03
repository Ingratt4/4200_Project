package com.example.a4200_project;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Pipe {
    private int x, y;
    private int width, height;

    private Bitmap bitmap;
    public boolean scored = false;

    public Pipe(int x, int y, int width, int height, Bitmap bitmap) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bitmap = bitmap;

    }

    public void update(int speed) {

        x -= speed;
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

    public int getHeight() {
        return height;
    }

}