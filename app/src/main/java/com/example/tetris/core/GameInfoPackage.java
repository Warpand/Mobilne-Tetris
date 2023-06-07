package com.example.tetris.core;

public class GameInfoPackage {
    private final int score;
    private final int nextTetrominoId;
    private final int rivalScore; // ignored in single-player context

    GameInfoPackage(int score, int nextTetrominoId) {
        this.score = score;
        this.nextTetrominoId = nextTetrominoId;
        this.rivalScore = 0;
    }

    GameInfoPackage(int score, int nextTetrominoId, int rivalScore) {
        this.score = score;
        this.nextTetrominoId = nextTetrominoId;
        this.rivalScore = rivalScore;
    }

    public int getScore() {
        return score;
    }

    public int getNextTetrominoId() {
        return nextTetrominoId;
    }

    public int getRivalScore() {
        return rivalScore;
    }
}
