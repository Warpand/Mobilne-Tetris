package com.example.tetris.core;

import android.graphics.Canvas;

// delete later
public class Dummies {
    public static class DummyLogicManager implements GameLogicManager {

        @Override
        public void update() {

        }

        @Override
        public void moveLeft() {

        }

        @Override
        public void moveRight() {

        }

        @Override
        public boolean getTile(int i, int j) {
            return false;
        }

        @Override
        public int getScore() {
            return 0;
        }

        @Override
        public int getNextTetrominoId() {
            return 0;
        }

        @Override
        public Tetromino getCurrentTetromino() {
            return null;
        }

        @Override
        public boolean isPaused() {
            return false;
        }

        @Override
        public boolean isGameOver() {
            return false;
        }

        @Override
        public void setPause(boolean state) {

        }

        @Override
        public void setSpeedUp(boolean state) {

        }

        @Override
        public void rotateRight() {

        }

        @Override
        public void rotateLeft() {

        }
    }

    public static class DummyDrawingManager implements GameDrawingManager {

        @Override
        public void draw() {

        }

        @Override
        public void setDrawingBuffer(Canvas canvas, int w, int h) {

        }
    }
}
