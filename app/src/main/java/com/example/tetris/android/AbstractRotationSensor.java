package com.example.tetris.android;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.example.tetris.core.GameEngine;
import com.example.tetris.core.GameEvent;

public abstract class AbstractRotationSensor implements RotationSensor {
    private static final float neutralPoseDelay = 0.2f;
    private static final float tiltedPoseDelay = 0.8f;
    private static final float ns2s = 1e-9f;
    private static final double threshold = 15d;

    private float timeToRotate = neutralPoseDelay;
    private long timestamp = 0;

    private final Sensor sensor;
    private final GameEngine gameEngine;

    AbstractRotationSensor(SensorManager sensorManager, GameEngine gameEngine) {
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        this.gameEngine = gameEngine;
    }

    protected void handleTiltAndTimestamp(double tilt, long eventTimestamp) {
        // the documentation doesn't disclose if event.timestamp is time since registration
        // or some arbitrary number, so the first event timestamp is used to initialize our timestamp
        float dt = (timestamp != 0) ? (eventTimestamp - timestamp) * ns2s : 0.0f;
        timestamp = eventTimestamp;
        if(Math.abs(tilt) > threshold) {
            timeToRotate -= dt;
            if(timeToRotate > 0)
                return;
            if(tilt > 0)
                gameEngine.registerEvent(new GameEvent.RotateRightEvent());
            else if (tilt < 0)
                gameEngine.registerEvent(new GameEvent.RotateLeftEvent());
            timeToRotate = tiltedPoseDelay;
        }
        else {
            timeToRotate = neutralPoseDelay;
        }
    }

    @Override
    public void register(SensorManager sensorManager) {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void unregister(SensorManager sensorManager) {
        sensorManager.unregisterListener(this);
        timestamp = 0;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}
}
