package com.example.tetris.core;

public class BasicGameEngine extends AbstractGameEngine implements GameEngine {
    private int score = 0;
    private int nextTetrominoId = Tetromino.MIN_TETROMINO_TYPE - 1; // not a valid tetromino id

    BasicGameEngine(GameLogicManager logicManager, GameDrawingManager drawingManager) {
        super(logicManager, drawingManager);
    }

    @Override
    protected void performFrame() {
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
            notifyObservers(new GameInfoPackage(score, nextTetrominoId));
        }
        drawingManager.draw();
    }
}
