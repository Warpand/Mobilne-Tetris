package com.example.tetris.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OriginalScoreCounterTest {
    @Test
    public void testZeroRows() {
        ScoreCounter c = new OriginalGravityManager.OriginalScoreCounter();
        assertEquals(0, c.rowsToScore(0));
    }

    @Test
    public void testOneRow() {
        ScoreCounter c = new OriginalGravityManager.OriginalScoreCounter();
        assertEquals(40, c.rowsToScore(1));
    }

    @Test
    public void testTwoRows() {
        ScoreCounter c = new OriginalGravityManager.OriginalScoreCounter();
        assertEquals(100, c.rowsToScore(2));
    }

    @Test
    public void testThreeRows() {
        ScoreCounter c = new OriginalGravityManager.OriginalScoreCounter();
        assertEquals(300, c.rowsToScore(3));
    }

    @Test
    public void testFourRows() {
        ScoreCounter c = new OriginalGravityManager.OriginalScoreCounter();
        assertEquals(1200, c.rowsToScore(4));
    }
}
