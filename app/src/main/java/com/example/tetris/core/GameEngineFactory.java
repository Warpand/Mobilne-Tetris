package com.example.tetris.core;

import android.content.Context;

import com.example.tetris.database.AppDatabase;
import com.example.tetris.media.MediaHolder;

public interface GameEngineFactory {
    GameEngine produce();

    class SinglePlayerEngineFactory implements GameEngineFactory {
        private final Settings settings;
        private final Context appContext;
        private final MediaHolder media;

        public SinglePlayerEngineFactory(Settings settings, Context appContext, MediaHolder media) {
            this.settings = settings;
            this.appContext = appContext;
            this.media = media;
        }

        @Override
        public GameEngine produce() {
            GameLogicManager logicManager = new GameLogicManagerImpl(
                    settings.getGeneratorType() == Settings.generatorType.RANDOM ?
                            new SinglePlayerTetrominoGenerator() :
                            new SequenceTetrominoGenerator(),
                    new OriginalGravityManager(),
                    settings.getSpeed() == Settings.speedType.ADJUSTING ?
                            new SpeedManager.AdjustingSpeedManager() :
                            settings.getSpeed() == Settings.speedType.CONSTANT ?
                                    new SpeedManager.ConstantSpeedManager() :
                                    new SpeedManager.ConstantSpeedManagerHard(),
                    AppDatabase.getDb(appContext).entryDao(),
                    media.getSoundEffects()
            );
            GameDrawingManager drawingManager = new GameDrawingManagerImpl(logicManager);
            return new BasicGameEngine(logicManager, drawingManager);
        }
    }
}


