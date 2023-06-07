package com.example.tetris.core;

import android.content.Context;
import android.util.Log;

import com.example.tetris.database.AppDatabase;
import com.example.tetris.media.MediaHolder;

import java.util.ArrayList;

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
            TetrominoGenerator tetrominoGenerator;
            if(settings.getGeneratorType() == Settings.generatorType.RANDOM)
                tetrominoGenerator = new BasicTetrominoGenerator();
            else
                tetrominoGenerator = new SequenceTetrominoGenerator();
            SpeedManager speedManager;
            if(settings.getSpeed() == Settings.speedType.ADJUSTING)
                speedManager = new SpeedManager.AdjustingSpeedManager();
            else if(settings.getSpeed() == Settings.speedType.CONSTANT)
                speedManager = new SpeedManager.ConstantSpeedManager();
            else
                speedManager = new SpeedManager.ConstantSpeedManagerHard();
            GameLogicManager logicManager = new GameLogicManagerImpl(
                tetrominoGenerator,
                new OriginalGravityManager(),
                speedManager,
                AppDatabase.getDb(appContext).entryDao(),
                media.getSoundEffects()
            );
            GameDrawingManager drawingManager = new GameDrawingManagerImpl(logicManager);
            return new BasicGameEngine(logicManager, drawingManager);
        }
    }

    class HostGameEngineFactory implements GameEngineFactory {
        private final Settings settings;
        private final Context appContext;
        private final MediaHolder media;
        private final SocketWrapper socketWrapper;

        public HostGameEngineFactory(Settings settings, Context appContext, MediaHolder media, SocketWrapper socketWrapper) {
            this.settings = settings;
            this.appContext = appContext;
            this.media = media;
            this.socketWrapper = socketWrapper;
        }

        @Override
        public GameEngine produce() {
            byte[] s = new byte[]{(byte)settings.getGeneratorType(), (byte)settings.getSpeed()};
            socketWrapper.write(new MultiplayerMessage(MultiplayerMessage.TYPE_SETTINGS, s));
            HostTetrominoGenerator tetrominoGenerator;
            if(settings.getGeneratorType() == Settings.generatorType.RANDOM)
                tetrominoGenerator = new HostTetrominoGenerator(new BasicTetrominoGenerator());
            else
                tetrominoGenerator = new HostTetrominoGenerator(new SequenceTetrominoGenerator());
            SpeedManager speedManager;
            if(settings.getSpeed() == Settings.speedType.ADJUSTING)
                speedManager = new SpeedManager.AdjustingSpeedManager();
            else if(settings.getSpeed() == Settings.speedType.CONSTANT)
                speedManager = new SpeedManager.ConstantSpeedManager();
            else
                speedManager = new SpeedManager.ConstantSpeedManagerHard();
            socketWrapper.write(new MultiplayerMessage(MultiplayerMessage.TYPE_TETROMINO_BATCH,
                    new byte[]{
                            (byte)tetrominoGenerator.getForRival(),
                            (byte)tetrominoGenerator.getForRival(),
                            (byte)tetrominoGenerator.getForRival(),
                            (byte)tetrominoGenerator.getForRival()
            }));
            GameLogicManager logicManager = new GameLogicManagerImpl(
                    tetrominoGenerator,
                    new OriginalGravityManager(),
                    speedManager,
                    AppDatabase.getDb(appContext).entryDao(),
                    media.getSoundEffects()
            );
            GameDrawingManager drawingManager = new GameDrawingManagerImpl(logicManager);
            return new MultiplayerHostGameEngine(logicManager, drawingManager, socketWrapper, tetrominoGenerator);
        }
    }

    class JoinGameEngineFactory implements GameEngineFactory {
        private final Settings settings;
        private final Context appContext;
        private final MediaHolder media;
        private final SocketWrapper socketWrapper;

        public JoinGameEngineFactory(Settings settings, Context appContext, MediaHolder media, SocketWrapper socketWrapper) {
            this.settings = settings;
            this.appContext = appContext;
            this.media = media;
            this.socketWrapper = socketWrapper;
        }

        @Override
        public GameEngine produce() {
            MultiplayerMessage msg = socketWrapper.readBlocking();
            if(msg.getType() != MultiplayerMessage.TYPE_SETTINGS) {
                Log.e("Message", "Wrong message in settings");
                throw new RuntimeException();
            }
            settings.updateFromMessage(msg);
            JoinTetrominoGenerator tetrominoGenerator;
            if(settings.getGeneratorType() == Settings.generatorType.RANDOM)
                tetrominoGenerator = new JoinTetrominoGenerator(new BasicTetrominoGenerator());
            else
                tetrominoGenerator = new JoinTetrominoGenerator(new SequenceTetrominoGenerator());
            SpeedManager speedManager;
            if(settings.getSpeed() == Settings.speedType.ADJUSTING)
                speedManager = new SpeedManager.AdjustingSpeedManager();
            else if(settings.getSpeed() == Settings.speedType.CONSTANT)
                speedManager = new SpeedManager.ConstantSpeedManager();
            else
                speedManager = new SpeedManager.ConstantSpeedManagerHard();
            msg = socketWrapper.readBlocking();
            if(msg.getType() != MultiplayerMessage.TYPE_TETROMINO_BATCH) {
                Log.e("Message", "Wrong message in settings");
                throw new RuntimeException();
            }
            ArrayList<Integer> tetrominoIds = new ArrayList<>();
            for(int i = 0; i < msg.size(); i++)
                tetrominoIds.add(msg.at(i));
            tetrominoGenerator.delivery(tetrominoIds);
            GameLogicManager logicManager = new GameLogicManagerImpl(
                    tetrominoGenerator,
                    new OriginalGravityManager(),
                    speedManager,
                    AppDatabase.getDb(appContext).entryDao(),
                    media.getSoundEffects()
            );
            GameDrawingManager drawingManager = new GameDrawingManagerImpl(logicManager);
            return new MultiplayerJoinGameEngine(logicManager, drawingManager, socketWrapper, tetrominoGenerator);
        }
    }
}


