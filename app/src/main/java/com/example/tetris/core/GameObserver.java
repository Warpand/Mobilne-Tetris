package com.example.tetris.core;

import com.example.tetris.core.GameInfoPackage;

public interface GameObserver {
    void notifyChange(GameInfoPackage infoPackage);
}
