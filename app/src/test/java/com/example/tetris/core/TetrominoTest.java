package com.example.tetris.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.List;

public class TetrominoTest {
    @Test
    public void testGetType() {
        Tetromino t = Tetromino.getTetromino(0, 0, 6);
        assertEquals(6, t.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructionThrowsWhenTypeTooLow() {
        Tetromino.getTetromino(0, 0, Tetromino.MIN_TETROMINO_TYPE - 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructionThrowsWhenTypeTooHigh() {
        Tetromino.getTetromino(0, 0, Tetromino.MAX_TETROMINO_TYPE + 1);
    }

    @Test
    public void testMoveUpAfterMoveDownDoesNothing() {
        for(int i = Tetromino.MIN_TETROMINO_TYPE; i <= Tetromino.MAX_TETROMINO_TYPE; i++) {
            Tetromino t = Tetromino.getTetromino(0, 0, i);
            List<Pair> before = t.getBlocks();
            t.moveDown();
            t.moveUp();
            List<Pair> after = t.getBlocks();
            assertEquals("Error for type " + i, before, after);
        }
    }

    @Test
    public void testRotateLeftAfterRotateRightDoesNothing() {
        for(int i = Tetromino.MIN_TETROMINO_TYPE; i <= Tetromino.MAX_TETROMINO_TYPE; i++) {
            Tetromino t = Tetromino.getTetromino(0, 0, i);
            List<Pair> before = t.getBlocks();
            t.rotateRight();
            t.rotateLeft();
            List<Pair> after = t.getBlocks();
            assertEquals("Error for type " + i, before, after);
        }
    }

    @Test
    public void testRotateRightAfterRotateLeftDoesNothing() {
        for(int i = Tetromino.MIN_TETROMINO_TYPE; i <= Tetromino.MAX_TETROMINO_TYPE; i++) {
            Tetromino t = Tetromino.getTetromino(0, 0, i);
            List<Pair> before = t.getBlocks();
            t.rotateLeft();
            t.rotateRight();
            List<Pair> after = t.getBlocks();
            assertEquals("Error for type " + i, before, after);
        }
    }

    @Test
    public void moveLeftAfterMoveRightDoesNothing() {
        for(int i = Tetromino.MIN_TETROMINO_TYPE; i <= Tetromino.MAX_TETROMINO_TYPE; i++) {
            Tetromino t = Tetromino.getTetromino(0, 0, i);
            List<Pair> before = t.getBlocks();
            t.moveRight();
            t.moveLeft();
            List<Pair> after = t.getBlocks();
            assertEquals("Error for type " + i, before, after);
        }
    }

    @Test
    public void moveRightAfterMoveLeftDoesNothing() {
        for(int i = Tetromino.MIN_TETROMINO_TYPE; i <= Tetromino.MAX_TETROMINO_TYPE; i++) {
            Tetromino t = Tetromino.getTetromino(0, 0, i);
            List<Pair> before = t.getBlocks();
            t.moveLeft();
            t.moveRight();
            List<Pair> after = t.getBlocks();
            assertEquals("Error for type " + i, before, after);
        }
    }
}