package com.example.tetris.core;

public class MultiplayerHostGameEngine extends AbstractGameEngine implements GameEngine {
    // this is a temporary dummy implementation
    private final SocketWrapper socketWrapper;
    private int cooldown = 30;
    private int tick = 1;

    public MultiplayerHostGameEngine(SocketWrapper socketWrapper) {
        super(new Dummies.DummyLogicManager(), new Dummies.DummyDrawingManager());
        this.socketWrapper = socketWrapper;
    }

    @Override
    protected void performFrame() {
        cooldown--;
        if (cooldown == 0) {
            cooldown = 30;
            byte[] c = new byte[1];
            c[0] = (byte) tick;
            socketWrapper.write(new MultiplayerMessage(1, c));
            notifyObservers(new GameInfoPackage(tick, 0));
            tick++;
        }
    }
}
