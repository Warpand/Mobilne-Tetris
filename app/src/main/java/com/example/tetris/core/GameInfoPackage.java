package com.example.tetris.core;

public class GameInfoPackage {
    private final int score;
    private final int nextTetrominoId;

    GameInfoPackage(int score, int nextTetrominoId) {
        this.score = score;
        this.nextTetrominoId = nextTetrominoId;
    }

    public int getScore() {
        return score;
    }

    public int getNextTetrominoId() {
        return nextTetrominoId;
    }
}
