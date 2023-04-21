package com.example.tetris.android;
import android.view.View;
import android.widget.ImageButton;

import com.example.tetris.core.GameEngine;
import com.example.tetris.core.GameEvent;

public class PauseButtonListener implements View.OnClickListener {
    private final GameEngine gameEngine;
    private static final int pauseImageId = android.R.drawable.ic_media_pause;
    private static final int resumeImageId = android.R.drawable.ic_media_play;
    private boolean paused = false;

    PauseButtonListener(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    @Override
    public void onClick(View view) {
        ImageButton pauseButton = (ImageButton) view;
        paused = !paused;
        if(paused)
            pauseButton.setImageResource(resumeImageId);
        else
            pauseButton.setImageResource(pauseImageId);
        gameEngine.registerEvent(new GameEvent.PauseEvent());
    }
}
