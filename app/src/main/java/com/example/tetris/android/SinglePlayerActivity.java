package com.example.tetris.android;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tetris.core.GameEngine;
import com.example.tetris.core.GameEngineFactory;
import com.example.tetris.core.GameEvent;
import com.example.tetris.databinding.SinglePlayerActivityBinding;


public class SinglePlayerActivity extends AppCompatActivity {
    private GameEngine gameEngine;
    private Sensor rotationSensor;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        GameEngineFactory factory = new GameEngineFactory.SinglePlayerEngineFactory();
        gameEngine = factory.produce();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);

        SinglePlayerActivityBinding binding = SinglePlayerActivityBinding.inflate(getLayoutInflater());
        gameEngine.registerObserver(binding.scoreText);
        gameEngine.registerObserver(binding.tetrominoView);
        binding.pauseButton.setOnClickListener(view -> gameEngine.registerEvent(new GameEvent.PauseEvent()));
        binding.boardView.setGameEngine(gameEngine);
        setContentView(binding.getRoot());
    }

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        private static final int neutralPoseDelay = 1;
        private static final int tiltedPoseDelay = 4;
        private static final double threshold = 27d;
        private final float[] rotationMatrixBuffer = new float[16];
        private final float[] remappedRotationBuffer = new float[16];
        private final float[] orientationsBuffer = new float[3];
        private int x = neutralPoseDelay;
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            SensorManager.getRotationMatrixFromVector(rotationMatrixBuffer, sensorEvent.values);
            SensorManager.remapCoordinateSystem(
                    rotationMatrixBuffer,
                    SensorManager.AXIS_X,
                    SensorManager.AXIS_Y,
                    remappedRotationBuffer
            );
            SensorManager.getOrientation(remappedRotationBuffer, orientationsBuffer);
            double d = Math.toDegrees(orientationsBuffer[2]);
            if(Math.abs(d) > threshold) {
                x--;
                if(x != 0)
                    return;
                x = tiltedPoseDelay;
                if (d > 0)
                    gameEngine.registerEvent(new GameEvent.RotateRightEvent());
                else if (d < 0)
                    gameEngine.registerEvent(new GameEvent.RotateLeftEvent());
            }
            else {
                x = neutralPoseDelay;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
        gameEngine.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener, rotationSensor,
                SensorManager.SENSOR_DELAY_NORMAL // 0.2 sec
        );
        gameEngine.registerEvent(new GameEvent.SetSpeedEvent(false));
        gameEngine.start();
    }

}
