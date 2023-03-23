package com.example.tetris.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OriginalGravityManagerTest {
    @Test
    public void testNothingHappens() {
        boolean[][] board = {
                {true, false, false, false, false},
                {false ,true ,false, true, false},
                {false, true, false, false, false},
                {false, true, false, false, false},
                {false, true, false, false, false}
        };
        OriginalGravityManager m = new OriginalGravityManager();
        boolean[][] before = board.clone();
        int score = m.checkBoard(board);
        assertEquals(0, score);
        assertArrayEquals(before, board);
    }

    @Test
    public void oneRowDeleted() {
        boolean[][] board = {
                {true, true, true, true, true},
                {true, true, true, true, false},
                {true, true, true, false, false},
                {true, true, false, false, false},
                {true, false, false, false, false}
        };
        OriginalGravityManager m = new OriginalGravityManager();
        boolean[][] expected = {
                {true, true, true, true, false},
                {true, true, true, false, false},
                {true, true, false, false, false},
                {true, false, false, false, false},
                {false, false, false, false, false}
        };
        int score = m.checkBoard(board);
        assertEquals(40, score);
        assertArrayEquals(expected, board);
    }
    
    @Test
    public void testTwoRowsDeleted() {
        boolean[][] board = {
                {true, true, true, true, true},
                {true, false, true, false, false},
                {true, true, true, true, true},
                {true, false, true, false, false},
                {true, false, true, false, false}
        };
        OriginalGravityManager m = new OriginalGravityManager();
        boolean[][] expected = {
                {true, true, true, false, false},
                {false, false, false, false, false},
                {true, true, true, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false}
        };
        int score = m.checkBoard(board);
        assertEquals(100, score);
        assertArrayEquals(expected, board);
    }

    @Test
    public void testThreeRowsDeleted() {
        boolean[][] board = {
                {true, true, true, true, false},
                {true, true, true, false, false},
                {true, true, true, false, false},
                {true, true, true, false, false},
                {true, true, true, false, false}
        };
        OriginalGravityManager m = new OriginalGravityManager();
        boolean[][] expected = new boolean[5][5];
        expected[0][0] = true;
        int score = m.checkBoard(board);
        assertEquals(300, score);
        assertArrayEquals(expected, board);
    }

    @Test
    public void testFourRowsDeleted() {
        boolean[][] board = {
                {true, true, true, true, false},
                {true, true, true, true, false},
                {true, true, true, true, false},
                {true, true, true, true, false},
                {true, true, true, true, false}
        };
        OriginalGravityManager m = new OriginalGravityManager();
        int score = m.checkBoard(board);
        assertEquals(1200, score);
        assertArrayEquals(new boolean[5][5], board);
    }
}
