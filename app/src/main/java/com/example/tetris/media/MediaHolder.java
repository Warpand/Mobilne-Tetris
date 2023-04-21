package com.example.tetris.media;

import android.content.Context;

public interface MediaHolder {
    void load(Context context);

    void release();

    SoundEffectsPlayer getSoundEffects();
}
