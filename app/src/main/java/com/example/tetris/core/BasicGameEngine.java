package com.example.tetris.core;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BasicGameEngine implements GameEngine {
    private final GameDrawingManager drawingManager;
    private final ConcurrentLinkedQueue<GameEvent> eventQueue;
    private final ArrayList<GameObserver> observers = new ArrayList<>();
    private ScheduledExecutorService gameScheduler;
    private final Runnable gameExecution;
    private int score = 0;
    private int nextTetrominoId = Tetromino.MIN_TETROMINO_TYPE - 1; // not a valid tetromino id

    BasicGameEngine(GameLogicManager logicManager, GameDrawingManager drawingManager) {
        this.drawingManager = drawingManager;
        eventQueue = new ConcurrentLinkedQueue<>();
        gameExecution = () -> {
            GameEvent event;
            while((event = eventQueue.poll()) != null) {
                event.execute(logicManager);
            }
            logicManager.update();
            int newScore = logicManager.getScore();
            int newTetrominoId = logicManager.getNextTetrominoId();
            if(newScore != score || newTetrominoId != nextTetrominoId) {
                score = newScore;
                nextTetrominoId = newTetrominoId;
                notifyObservers();
            }
            drawingManager.draw();
        };
    }

    @Override
    public void start() {
        gameScheduler = Executors.newSingleThreadScheduledExecutor();
        gameScheduler.scheduleAtFixedRate(gameExecution, 0, Constants.millisecondsPerFrame, TimeUnit.MILLISECONDS);
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

    private void notifyObservers() {
        GameInfoPackage infoPackage = new GameInfoPackage(score, nextTetrominoId);
        for(GameObserver observer : observers)
            observer.notifyChange(infoPackage);
    }
}
