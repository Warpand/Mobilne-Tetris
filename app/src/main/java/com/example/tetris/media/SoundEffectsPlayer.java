package com.example.tetris.media;

import android.content.Context;

public interface SoundEffectsPlayer {
    int MOVE_EFFECT = 0;
    int ROTATE_EFFECT = 1;
    int PAUSE_EFFECT = 2;
    int DELETE_EFFECT = 3;
    int GAME_OVER_EFFECT = 4;
    int PLACE_EFFECT = 5;
    int GAME_EFFECT_COUNT = 6;

    void load(Context context);

    void release();

    void play(int effectId);
}
