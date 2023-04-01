package com.example.tetris.core;

public interface BoardGravityManager {
    int checkBoard(boolean[][] board);

    ScoreCounter getAssociatedScoreCounter();
}
