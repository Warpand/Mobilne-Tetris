package com.example.tetris.core;

import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class MultiplayerJoinGameEngine extends AbstractGameEngine implements GameEngine {
    private final SocketWrapper socketWrapper;
    private final JoinTetrominoGenerator tetrominoGenerator;
    private int score = 0;
    private int rivalScore = 0;
    private int previousRivalScore = 0;
    private int nextTetrominoId = Tetromino.MAX_TETROMINO_TYPE + 1;
    private boolean gameOverMsgSent = false;
    private final AtomicBoolean discardEvents = new AtomicBoolean(false);

    public MultiplayerJoinGameEngine(GameLogicManager logicManager, GameDrawingManager drawingManager,
                                     SocketWrapper socketWrapper, JoinTetrominoGenerator tetrominoGenerator) {
        super(logicManager, drawingManager);
        this.socketWrapper = socketWrapper;
        this.tetrominoGenerator = tetrominoGenerator;
    }

    @Override
    protected void performFrame() {
        if(tetrominoGenerator.requiresDelivery())
            socketWrapper.write(new MultiplayerMessage(MultiplayerMessage.TYPE_TETROMINO_REQUEST, new byte[0]));
        MultiplayerMessage msg;
        if((msg = socketWrapper.read()) != null)
            handleMessage(msg);
        if(tetrominoGenerator.stateCritical()) {
            discardEvents.set(true);
            return;
        }
        discardEvents.set(false);
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
            case MultiplayerMessage.TYPE_RIVAL_SCORE_CHANGE:
                rivalScore = msg.toScore();
                break;
            case MultiplayerMessage.TYPE_TETROMINO_BATCH:
                ArrayList<Integer> tetrominoList = new ArrayList<>();
                for(int i = 0; i < msg.size(); i++)
                    tetrominoList.add(msg.at(i));
                tetrominoGenerator.delivery(tetrominoList);
                break;
            case MultiplayerMessage.TYPE_RIVAL_GAME_OVER:
                rivalScore = -1;
                break;
            case MultiplayerMessage.TYPE_DONE:
                socketWrapper.close();
                rivalScore = -1;
                tetrominoGenerator.becomeIndependent();
                break;
            default:
                Log.e("Message", "Message of bad type received by the game engine");
        }
    }

    @Override
    public void registerEvent(GameEvent event) {
        if(discardEvents.get() && !(event instanceof GameEvent.PauseEvent) && !(event instanceof GameEvent.SetSpeedEvent))
            return;
        super.registerEvent(event);
    }
}
