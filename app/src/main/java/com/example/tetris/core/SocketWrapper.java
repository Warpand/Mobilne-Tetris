package com.example.tetris.core;

public interface SocketWrapper {
    MultiplayerMessage read();
    MultiplayerMessage readBlocking();
    void write(MultiplayerMessage m);
    void close();
}
