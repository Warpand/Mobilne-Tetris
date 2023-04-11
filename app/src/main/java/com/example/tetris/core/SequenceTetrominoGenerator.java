package com.example.tetris.core;

import java.util.Random;

public class SequenceTetrominoGenerator implements TetrominoGenerator {
    private final Random gen;
    private int[] sequence;
    private int i = 0;
    private static final int SEQ_LEN = Tetromino.MAX_TETROMINO_TYPE - Tetromino.MIN_TETROMINO_TYPE + 1;

    SequenceTetrominoGenerator() {
        gen = new Random();
        sequence = generateSequence();
    }

    private int[] generateSequence() {
        int[] seq = new int[SEQ_LEN];
        for(int i = 0; i < SEQ_LEN; i++)
            seq[i] = i + Tetromino.MIN_TETROMINO_TYPE;
        for(int i = SEQ_LEN - 1; i >= 1; i--) {
            int j = gen.nextInt(i + 1);
            int p = seq[i];
            seq[i] = seq[j];
            seq[j] = p;
        }
        return seq;
    }

    @Override
    public int next() {
        return sequence[i];
    }

    @Override
    public int get() {
        int r = sequence[i];
        i++;
        if(i >= SEQ_LEN) {
            sequence = generateSequence();
            i = 0;
        }
        return r;
    }
}
