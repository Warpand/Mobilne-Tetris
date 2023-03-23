package com.example.tetris.core;

import java.util.Random;

public class SinglePlayerTetrominoGenerator implements TetrominoGenerator{
    private final Random gen;
    private int next;

    SinglePlayerTetrominoGenerator() {
        gen = new Random();
        next = randomize();
    }

    private int randomize() {
        return gen.nextInt(Tetromino.MAX_TETROMINO_TYPE) + Tetromino.MIN_TETROMINO_TYPE;
    }
    @Override
    public int get() {
        int current = next;
        next = randomize();
        return current;
    }

    @Override
    public int next() {
        return next;
    }
}
