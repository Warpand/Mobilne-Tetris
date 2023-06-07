package com.example.tetris.core;

import java.util.ArrayDeque;
import java.util.Queue;

public class HostTetrominoGenerator implements TetrominoGenerator {
    private final TetrominoGenerator baseGenerator;
    private final Queue<Integer> hostQueue;
    private final Queue<Integer> rivalQueue;
    private boolean rivalActive = true;

    public HostTetrominoGenerator(TetrominoGenerator baseGenerator) {
        this.baseGenerator = baseGenerator;
        hostQueue = new ArrayDeque<>();
        rivalQueue = new ArrayDeque<>();
        generate();
    }

    private void generate() {
        int tetromino = baseGenerator.get();
        hostQueue.add(tetromino);
        if(rivalActive)
            rivalQueue.add(tetromino);
    }

    @Override
    public int next() {
        return hostQueue.peek();
    }

    @Override
    public int get() {
        int r = hostQueue.poll();
        if(hostQueue.isEmpty())
            generate();
        return r;
    }

    public int getForRival() {
        try {
            int r = rivalQueue.poll();
            if (rivalQueue.isEmpty())
                generate();
            return r;
        }
        catch (NullPointerException e) {
            return baseGenerator.next(); // not important
        }
    }

    public void rivalFinished() {
        rivalActive = false;
        rivalQueue.clear();
    }
}
