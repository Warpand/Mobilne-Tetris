package com.example.tetris.core;

public class MultiplayerJoinGameEngine extends AbstractGameEngine implements GameEngine {
    // this is a temporary dummy implementation
    private final SocketWrapper socketWrapper;

    public MultiplayerJoinGameEngine(SocketWrapper socketWrapper) {
        super(new Dummies.DummyLogicManager(), new Dummies.DummyDrawingManager());
        this.socketWrapper = socketWrapper;
    }

    @Override
    protected void performFrame() {
        MultiplayerMessage msg = socketWrapper.read();
        if(msg != null && msg.getType() != MultiplayerMessage.TYPE_DONE)
            notifyObservers(new GameInfoPackage(msg.at(0), 0));
    }
}
