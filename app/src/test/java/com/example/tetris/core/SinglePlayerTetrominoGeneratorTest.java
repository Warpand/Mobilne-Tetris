package com.example.tetris.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class SinglePlayerTetrominoGeneratorTest {
    @Test
    public void testGetAfterNextReturnsCorrectly() {
        SinglePlayerTetrominoGenerator gen = new SinglePlayerTetrominoGenerator();
        int n = gen.next();
        assertEquals(n, gen.get());
    }

    @Test
    public void testSubsequentNextDoNotChange() {
        SinglePlayerTetrominoGenerator gen = new SinglePlayerTetrominoGenerator();
        int n = gen.next();
        assertEquals(n, gen.next());
    }
}