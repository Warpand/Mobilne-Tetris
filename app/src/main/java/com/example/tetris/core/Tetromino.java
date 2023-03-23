package com.example.tetris.core;

import java.util.ArrayList;
import java.util.List;

public class Tetromino {
    public static final int MIN_TETROMINO_TYPE = 1;
    public static final int MAX_TETROMINO_TYPE = 7;
    private final Pair pos;
    private final int boundingBoxSize;
    private final int type;
    private final ArrayList<Pair> blockTranslations;

    private Tetromino(int x, int y, int boundingBoxSize, int type) {
        pos = new Pair(x - (boundingBoxSize + 1) / 2, y);
        this.boundingBoxSize = boundingBoxSize;
        this.blockTranslations = new ArrayList<>(4);
        this.type = type;
    }

    private Tetromino addBlock(int x, int y) {
        blockTranslations.add(new Pair(x, y));
        return this;
    }

    public int getType() {
        return type;
    }

    void moveDown() {
        pos.y--;
    }

    void moveUp() {
        pos.y++;
    }

    void moveLeft() {
        pos.x--;
    }

    void moveRight() {
        pos.x++;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    void rotateRight() {
        for(Pair p : blockTranslations) {
            int newX = p.y;
            int newY = boundingBoxSize - p.x - 1;
            p.x = newX;
            p.y = newY;
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    void rotateLeft() {
        for(Pair p : blockTranslations) {
            int newX = boundingBoxSize - p.y - 1;
            int newY = p.x;
            p.x = newX;
            p.y = newY;
        }
    }

    public List<Pair> getBlocks() {
        ArrayList<Pair> result = new ArrayList<>(4);
        for(Pair p : blockTranslations) {
            result.add(new Pair(pos.x + p.x, pos.y + p.y));
        }
        return result;
    }

    private static final int[] boundingBoxSizeMap = {0, 4, 3, 3, 2, 3, 3, 3};
    private static final int[] yTranslationMap = {0, -2, -2, -2, -1, -2, -2, -2};
    public static Tetromino getTetromino(int x, int y, int type) {
        if(MIN_TETROMINO_TYPE > type || type > MAX_TETROMINO_TYPE)
            throw new IllegalArgumentException("Illegal tetromino type");
        Tetromino result = new Tetromino(
                x - (boundingBoxSizeMap[type] - 1) / 2,
                y + yTranslationMap[type],
                boundingBoxSizeMap[type],
                type
        );
        switch (type) {
            case 1: // hero
                result.addBlock(0, 2).addBlock(1, 2).addBlock(2, 2).addBlock(3, 2);
                break;
            case 2: // blue ricky
                result.addBlock(0, 2).addBlock(0, 1).addBlock(1, 1).addBlock(2, 1);
                break;
            case 3: // orange ricky
                result.addBlock(2,2 ).addBlock(0, 1).addBlock(1, 1).addBlock(2, 1);
                break;
            case 4: // box
                result.addBlock(0, 0).addBlock(0, 1).addBlock(1, 0).addBlock(1,1);
                break;
            case 5: // Rhode Island Z
                result.addBlock(0,1).addBlock(1, 1).addBlock(1, 2).addBlock(2,2);
                break;
            case 6: // tee
                result.addBlock(0, 1).addBlock(1, 1).addBlock(2, 1).addBlock(1, 2);
                break;
            case 7: // Cleveland Z
                result.addBlock(1, 1).addBlock(2, 1).addBlock(0, 2).addBlock(1, 2);
                break;
        }
        return result;
    }
}
