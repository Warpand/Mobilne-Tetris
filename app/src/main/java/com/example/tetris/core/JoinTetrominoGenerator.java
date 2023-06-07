package com.example.tetris.core;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;

public class JoinTetrominoGenerator implements TetrominoGenerator {
    private static final int REQUEST_THRESHOLD = 4;
    private final TetrominoGenerator baseGenerator;
    private final Queue<Integer> tetrominoQueue;
    private boolean independent = false;
    private boolean deliveryPending = false;

    public JoinTetrominoGenerator(TetrominoGenerator baseGenerator) {
        this.baseGenerator = baseGenerator;
        tetrominoQueue = new ArrayDeque<>();
    }

    @Override
    public int next() {
       if(independent && tetrominoQueue.isEmpty())
           return baseGenerator.next();
       return tetrominoQueue.peek();
    }

    @Override
    public int get() {
        if(independent && tetrominoQueue.isEmpty())
            return baseGenerator.get();
        return tetrominoQueue.poll();
    }

    public boolean requiresDelivery() {
        if(!independent && tetrominoQueue.size() <= REQUEST_THRESHOLD && !deliveryPending) {
            deliveryPending = true;
            return true;
        }
        return false;
    }

    public boolean stateCritical() {
        return !independent && tetrominoQueue.isEmpty();
    }

    public void delivery(Collection<Integer> tetrominoIds) {
        deliveryPending = false;
        tetrominoQueue.addAll(tetrominoIds);
    }

    public void becomeIndependent() {
        independent = true;
    }
}
