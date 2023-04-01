package com.example.tetris.android;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import com.example.tetris.core.GameEngine;
import com.example.tetris.core.GameEvent;

public class RotationVector implements RotationSensor {
    private static final float neutralPoseDelay = 0.2f;
    private static final float tiltedPoseDelay = 0.8f;
    private static final float ns2s = 1e-9f;
    private static final double threshold = 15d;

    private final float[] rotationMatrixBuffer = new float[9];
    private final float[] remappedRotationBuffer = new float[9];
    private final float[] orientationsBuffer = new float[3];
    private float x = neutralPoseDelay;
    private float timestamp = 0f;
    private final Sensor sensor;
    private final GameEngine gameEngine;
    RotationVector(SensorManager sensorManager, GameEngine gameEngine) {
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        this.gameEngine = gameEngine;
    }
    @Override
    public void register(SensorManager sensorManager) {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void unregister(SensorManager sensorManager) {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        final float dt = (sensorEvent.timestamp - timestamp) * ns2s;
        timestamp = sensorEvent.timestamp;
        SensorManager.getRotationMatrixFromVector(rotationMatrixBuffer, sensorEvent.values);
        SensorManager.remapCoordinateSystem(
                rotationMatrixBuffer,
                SensorManager.AXIS_X,
                SensorManager.AXIS_Z,
                remappedRotationBuffer
        );
        SensorManager.getOrientation(remappedRotationBuffer, orientationsBuffer);
        double d = Math.toDegrees(orientationsBuffer[2]);
        if(Math.abs(d) > threshold) {
            x -= dt;
            if(x > 0)
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
}
