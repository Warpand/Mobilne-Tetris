package com.example.tetris.core;

import android.util.Log;

public class MultiplayerHostGameEngine extends AbstractGameEngine implements GameEngine {
    private static final int BATCH_SIZE = 10;
    private final SocketWrapper socketWrapper;
    private final HostTetrominoGenerator tetrominoGenerator;
    private int score = 0;
    private int rivalScore = 0;
    private int previousRivalScore = 0;
    private int nextTetrominoId = Tetromino.MAX_TETROMINO_TYPE + 1;
    private boolean gameOverMsgSent = false;

    public MultiplayerHostGameEngine(GameLogicManager logicManager, GameDrawingManager drawingManager,
                                     SocketWrapper socketWrapper, HostTetrominoGenerator tetrominoGenerator) {
        super(logicManager, drawingManager);
        this.socketWrapper = socketWrapper;
        this.tetrominoGenerator = tetrominoGenerator;
    }

    @Override
    protected void performFrame() {
        MultiplayerMessage msg;
        if((msg = socketWrapper.read()) != null)
            handleMessage(msg);
        GameEvent event;
        while((event = eventQueue.poll()) != null) {
            event.execute(logicManager);
        }
        logicManager.update();
        if(!gameOverMsgSent && logicManager.isGameOver()) {
            gameOverMsgSent = true;
            socketWrapper.write(new MultiplayerMessage(MultiplayerMessage.TYPE_RIVAL_GAME_OVER, new byte[0]));
        }
        int newScore = logicManager.getScore();
        int newTetrominoId = logicManager.getNextTetrominoId();
        if(newScore != score || newTetrominoId != nextTetrominoId || rivalScore != previousRivalScore) {
            if(score != newScore) {
                score = newScore;
                socketWrapper.write(MultiplayerMessage.fromScore(score));
            }
            nextTetrominoId = newTetrominoId;
            previousRivalScore = rivalScore;
            notifyObservers(new GameInfoPackage(score, nextTetrominoId, rivalScore));
        }
        drawingManager.draw();
    }

    private void handleMessage(MultiplayerMessage msg) {
        switch (msg.getType()) {
            case MultiplayerMessage.TYPE_TETROMINO_REQUEST:
                byte[] content = new byte[BATCH_SIZE];
                for(int i = 0; i < BATCH_SIZE; i++)
                    content[i] = (byte)tetrominoGenerator.getForRival();
                socketWrapper.write(new MultiplayerMessage(MultiplayerMessage.TYPE_TETROMINO_BATCH, content));
                break;
            case MultiplayerMessage.TYPE_RIVAL_SCORE_CHANGE:
                rivalScore = msg.toScore();
                break;
            case MultiplayerMessage.TYPE_RIVAL_GAME_OVER:
                rivalScore = -1;
                tetrominoGenerator.rivalFinished();
                break;
            case MultiplayerMessage.TYPE_DONE:
                socketWrapper.close();
                rivalScore = -1;
                tetrominoGenerator.rivalFinished();
                break;
            default:
                Log.e("Message", "Message of bad type received by the game engine");
        }
    }
}
