package com.example.tetris.core;

public interface GameLogicManager {
    void update();
    void moveLeft();
    void moveRight();

    boolean getTile(int i, int j);

    int getScore();

    int getNextTetrominoId();

    Tetromino getCurrentTetromino();
    boolean isPaused();
    boolean isGameOver();
    void setPause(boolean state);
    void setSpeedUp(boolean state);

    void rotateRight();

    void rotateLeft();
}
