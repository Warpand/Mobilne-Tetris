package com.example.tetris.android;

import android.content.Context;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tetris.core.GameEngine;
import com.example.tetris.core.GameEngineFactory;
import com.example.tetris.core.GameEvent;
import com.example.tetris.core.Settings;
import com.example.tetris.databinding.SinglePlayerActivityBinding;
import com.example.tetris.media.Dummies;
import com.example.tetris.media.MediaHolder;
import com.example.tetris.media.MediaHolderImpl;
import com.example.tetris.media.SoundEffectsPlayerImpl;

public class SinglePlayerActivity extends AppCompatActivity {
    private GameEngine gameEngine;
    private SensorManager sensorManager;
    private RotationSensor rotationSensor;
    private MediaHolder media;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        Context appContext = getApplicationContext();
        Settings settings = new Settings(appContext);
        media = (settings.getSoundEffects() == Settings.soundEffects.ON) ?
                new MediaHolderImpl(new SoundEffectsPlayerImpl()) :
                new Dummies.DummyMediaHolder();
        GameEngineFactory factory = new GameEngineFactory.SinglePlayerEngineFactory(settings, appContext, media);
        gameEngine = factory.produce();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        rotationSensor = settings.getTiltDetectorType() == Settings.tiltDetectorType.ROTATION_RELATIVE ?
                new RotationRelative(sensorManager, gameEngine) :
                new RotationAbsolute(sensorManager, gameEngine);
        SinglePlayerActivityBinding binding = SinglePlayerActivityBinding.inflate(getLayoutInflater());
        gameEngine.registerObserver(binding.scoreText);
        gameEngine.registerObserver(binding.tetrominoView);
        binding.pauseButton.setOnClickListener(new PauseButtonListener(gameEngine));
        binding.boardView.setGameEngine(gameEngine);
        setContentView(binding.getRoot());
    }

    @Override
    public void onPause() {
        super.onPause();
        rotationSensor.unregister(sensorManager);
        gameEngine.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        rotationSensor.register(sensorManager);
        gameEngine.registerEvent(new GameEvent.SetSpeedEvent(false));
        gameEngine.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        media.release();
    }

    @Override
    public void onStart() {
        super.onStart();
        media.load(getApplicationContext());
    }
}
