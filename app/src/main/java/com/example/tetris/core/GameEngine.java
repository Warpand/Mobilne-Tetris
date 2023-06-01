package com.example.tetris.core;

import android.graphics.Canvas;

public interface GameEngine {
   void start();
   void setDrawingBuffer(Canvas canvas, int w, int h);
   void registerEvent(GameEvent event);
   void stop();
   void registerObserver(GameObserver observer);
}
