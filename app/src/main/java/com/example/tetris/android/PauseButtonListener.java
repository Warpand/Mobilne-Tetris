package com.example.tetris.android;
import android.view.View;
import android.widget.ImageButton;

import com.example.tetris.core.GameEngine;
import com.example.tetris.core.GameEvent;

public class PauseButtonListener implements View.OnClickListener {
    private final GameEngine gameEngine;
    private final int pauseImageId;
    private final int resumeImageId;
    private boolean paused = false;

    PauseButtonListener(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        pauseImageId = android.R.drawable.ic_media_pause;
        resumeImageId = android.R.drawable.ic_media_play;
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
