package com.example.tetris.core;

public interface GameEngineFactory {
    GameEngine produce();

    class SinglePlayerEngineFactory implements GameEngineFactory {
        private final Settings settings;
        public SinglePlayerEngineFactory(Settings settings) {
            this.settings = settings;
        }
        @Override
        public GameEngine produce() {
            GameLogicManager logicManager = new GameLogicManagerImpl(
                    settings.getGeneratorType() == Settings.generatorType.RANDOM ?
                            new SinglePlayerTetrominoGenerator() :
                            new SequenceTetrominoGenerator(),
                    new OriginalGravityManager()
            );
            GameDrawingManager drawingManager = new GameDrawingManagerImpl(logicManager);
            return new BasicGameEngine(logicManager, drawingManager);
        }
    }
}


