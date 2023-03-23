package com.example.tetris.core;

public interface GameEngineFactory {
    GameEngine produce();

    class SinglePlayerEngineFactory implements GameEngineFactory {
        @Override
        public GameEngine produce() {
            GameLogicManager logicManager = new GameLogicManagerImpl(
                    new SinglePlayerTetrominoGenerator(),
                    new OriginalGravityManager()
            );
            GameDrawingManager drawingManager = new GameDrawingManagerImpl(logicManager);
            return new BasicGameEngine(logicManager, drawingManager);
        }
    }
}


