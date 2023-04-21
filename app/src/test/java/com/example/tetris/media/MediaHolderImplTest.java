package com.example.tetris.media;

import static org.junit.Assert.*;

import android.content.Context;

import org.junit.Test;
import org.mockito.Mockito;

public class MediaHolderImplTest {
    @Test
    public void testGetSoundEffectsPlayer() {
        SoundEffectsPlayer sfx = Mockito.mock(SoundEffectsPlayer.class);
        MediaHolderImpl mediaHolder = new MediaHolderImpl(sfx);
        assertSame(sfx, mediaHolder.getSoundEffects());
    }

    @Test
    public void testReleaseCallsReleaseOnHeldClasses() {
        SoundEffectsPlayer sfx = Mockito.mock(SoundEffectsPlayer.class);
        MediaHolderImpl mediaHolder = new MediaHolderImpl(sfx);
        mediaHolder.release();
        Mockito.verify(sfx, Mockito.times(1)).release();
        Mockito.verifyNoMoreInteractions(sfx);
    }

    @Test
    public void testLoadCallsLoadOnHeldClasses() {
        SoundEffectsPlayer sfx = Mockito.mock(SoundEffectsPlayer.class);
        MediaHolderImpl mediaHolder = new MediaHolderImpl(sfx);
        Context context = Mockito.mock(Context.class);
        mediaHolder.load(context);
        Mockito.verify(sfx, Mockito.times(1)).load(context);
        Mockito.verifyNoMoreInteractions(sfx);
    }
}