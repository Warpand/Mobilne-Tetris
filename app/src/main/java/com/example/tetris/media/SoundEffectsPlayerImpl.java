package com.example.tetris.media;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.tetris.R;

public class SoundEffectsPlayerImpl implements SoundEffectsPlayer {
    private static final int[] idMap = new int[SoundEffectsPlayer.GAME_EFFECT_COUNT];

    static {
        idMap[MOVE_EFFECT] = R.raw.move;
        idMap[ROTATE_EFFECT] = R.raw.rotate;
        idMap[PAUSE_EFFECT] = R.raw.pause;
        idMap[DELETE_EFFECT] = R.raw.row;
        idMap[GAME_OVER_EFFECT] = R.raw.game_over;
        idMap[PLACE_EFFECT] = R.raw.place;
    }

    private final MediaPlayer[] effects = new MediaPlayer[GAME_EFFECT_COUNT];

    @Override
    public void load(Context context) {
        for(int i = 0; i < GAME_EFFECT_COUNT; i++)
            effects[i] = MediaPlayer.create(context, idMap[i]);
    }

    @Override
    public void release() {
        for(int i = 0; i < GAME_EFFECT_COUNT; i++) {
            effects[i].release();
            effects[i] = null;
        }
    }

    @Override
    public void play(int effectId) {
        if(effects[effectId] != null)
            effects[effectId].start();
    }
}
