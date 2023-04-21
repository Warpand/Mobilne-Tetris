package com.example.tetris.media;

import android.content.Context;

public interface Dummies {
    /*
        A set of classes that do absolutely nothing.
        Can be used when sounds are turned off.
     */

    class DummyMediaHolder implements MediaHolder {
        @Override
        public void load(Context context) {}

        @Override
        public void release() {}

        @Override
        public SoundEffectsPlayer getSoundEffects() {
            return new DummySoundEffectsPlayer();
        }
    }

    class DummySoundEffectsPlayer implements SoundEffectsPlayer {

        @Override
        public void load(Context context) {}

        @Override
        public void release() {}

        @Override
        public void play(int effectId) {}
    }

}
