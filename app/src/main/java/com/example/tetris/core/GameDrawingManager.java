package com.example.tetris.core;

import android.graphics.Canvas;

public interface GameDrawingManager {
    void draw();
    void setDrawingBuffer(Canvas canvas, int w, int h);
}
