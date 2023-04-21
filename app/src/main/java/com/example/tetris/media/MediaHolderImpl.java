package com.example.tetris.media;

import android.content.Context;

public class MediaHolderImpl implements MediaHolder {
    private final SoundEffectsPlayer soundEffectsPlayer;

    public MediaHolderImpl(SoundEffectsPlayer soundEffectsPlayer) {
        this.soundEffectsPlayer = soundEffectsPlayer;
    }

    @Override
    public SoundEffectsPlayer getSoundEffects() {
        return soundEffectsPlayer;
    }

    @Override
    public void load(Context context) {
        soundEffectsPlayer.load(context);
    }

    @Override
    public void release() {
        soundEffectsPlayer.release();
    }
}
