package com.example.tetris.core;

import android.content.Context;

import com.example.tetris.database.AppDatabase;

public interface GameEngineFactory {
    GameEngine produce();

    class SinglePlayerEngineFactory implements GameEngineFactory {
        private final Settings settings;
        private final Context appContext;
        public SinglePlayerEngineFactory(Settings settings, Context appContext) {
            this.settings = settings;
            this.appContext = appContext;
        }
        @Override
        public GameEngine produce() {
            GameLogicManager logicManager = new GameLogicManagerImpl(
                    settings.getGeneratorType() == Settings.generatorType.RANDOM ?
                            new SinglePlayerTetrominoGenerator() :
                            new SequenceTetrominoGenerator(),
                    new OriginalGravityManager(),
                    AppDatabase.getDb(appContext).entryDao()
            );
            GameDrawingManager drawingManager = new GameDrawingManagerImpl(logicManager);
            return new BasicGameEngine(logicManager, drawingManager);
        }
    }
}


