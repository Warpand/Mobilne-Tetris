package com.example.tetris.android;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tetris.core.GameEngine;
import com.example.tetris.core.GameEngineFactory;
import com.example.tetris.core.GameEvent;
import com.example.tetris.core.Settings;
import com.example.tetris.databinding.SinglePlayerActivityBinding;


public class SinglePlayerActivity extends AppCompatActivity {
    private GameEngine gameEngine;
    private SensorManager sensorManager;
    private RotationSensor rotationSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        Settings settings = new Settings(getApplicationContext());
        GameEngineFactory factory = new GameEngineFactory.SinglePlayerEngineFactory(settings);
        gameEngine = factory.produce();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        rotationSensor = settings.getTiltDetectorType() == Settings.tiltDetectorType.ROTATION_VECTOR ?
                new RotationVector(sensorManager, gameEngine) :
                new RotationGyroscope(sensorManager, gameEngine, true);
        SinglePlayerActivityBinding binding = SinglePlayerActivityBinding.inflate(getLayoutInflater());
        gameEngine.registerObserver(binding.scoreText);
        gameEngine.registerObserver(binding.tetrominoView);
        binding.pauseButton.setOnClickListener(view -> gameEngine.registerEvent(new GameEvent.PauseEvent()));
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
        rotationSensor.register(sensorManager);
        gameEngine.registerEvent(new GameEvent.SetSpeedEvent(false));
        gameEngine.start();
    }
}
