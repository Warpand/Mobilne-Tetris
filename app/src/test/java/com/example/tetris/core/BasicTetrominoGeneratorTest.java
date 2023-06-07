package com.example.tetris.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class BasicTetrominoGeneratorTest {
    @Test
    public void testGetAfterNextReturnsCorrectly() {
        BasicTetrominoGenerator gen = new BasicTetrominoGenerator();
        int n = gen.next();
        assertEquals(n, gen.get());
    }

    @Test
    public void testSubsequentNextDoNotChange() {
        BasicTetrominoGenerator gen = new BasicTetrominoGenerator();
        int n = gen.next();
        assertEquals(n, gen.next());
    }
}