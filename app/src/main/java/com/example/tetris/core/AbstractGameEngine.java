package com.example.tetris.core;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class AbstractGameEngine implements GameEngine {
    protected final GameLogicManager logicManager;
    protected final GameDrawingManager drawingManager;
    protected final ConcurrentLinkedQueue<GameEvent> eventQueue;
    private final ArrayList<GameObserver> observers;
    private ScheduledExecutorService gameScheduler;

    AbstractGameEngine(GameLogicManager logicManager, GameDrawingManager drawingManager) {
        this.logicManager = logicManager;
        this.drawingManager = drawingManager;
        eventQueue = new ConcurrentLinkedQueue<>();
        observers = new ArrayList<>();
    }

    abstract protected void performFrame();

    @Override
    public void start() {
        gameScheduler = Executors.newSingleThreadScheduledExecutor();
        gameScheduler.scheduleAtFixedRate(this::performFrame, 0, Constants.millisecondsPerFrame, TimeUnit.MILLISECONDS);
    }

    @Override
    public void setDrawingBuffer(Canvas canvas, int w, int h) {
        drawingManager.setDrawingBuffer(canvas, w, h);
    }

    @Override
    public void registerEvent(GameEvent event) {
        eventQueue.add(event);
    }

    @Override
    public void stop() {
        gameScheduler.shutdown();
    }

    @Override
    public void registerObserver(GameObserver observer) {
        observers.add(observer);
    }

    protected void notifyObservers(GameInfoPackage infoPackage) {
        for(GameObserver observer : observers)
            observer.notifyChange(infoPackage);
    }
}
