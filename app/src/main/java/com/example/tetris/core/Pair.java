package com.example.tetris.core;

public class Pair {
    public int x;
    public int y;

    Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Pair))
            return false;
        Pair p = (Pair) other;
        return x == p.x && y == p.y;
    }
}
