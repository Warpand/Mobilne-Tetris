package com.example.tetris.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class GameInfoPackageTest {
    @Test
    public void testGetScore() {
        GameInfoPackage infoPackage = new GameInfoPackage(1000, 4);
        assertEquals(1000, infoPackage.getScore());
    }

    @Test
    public void testGetNextTetrominoId() {
        GameInfoPackage infoPackage = new GameInfoPackage(1000, 4);
        assertEquals(4, infoPackage.getNextTetrominoId());
    }
}