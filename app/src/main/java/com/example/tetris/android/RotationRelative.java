package com.example.tetris.android;

import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import com.example.tetris.core.GameEngine;

public class RotationRelative extends AbstractRotationSensor {
    private float[] prevRotationMatrix = null;
    private final float[] rotationMatrixBuffer = new float[9];
    private final float[] angleChangeBuffer = new float[3];

    private float azimuth = 0.0f;
    private boolean calibrated = false;

    RotationRelative(SensorManager sensorManager, GameEngine gameEngine) {
        super(sensorManager, gameEngine);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        SensorManager.getRotationMatrixFromVector(rotationMatrixBuffer, sensorEvent.values);
        if (prevRotationMatrix == null) {
            // first event is used to set up the initial rotation...
            prevRotationMatrix = rotationMatrixBuffer.clone();
            handleTiltAndTimestamp(0.0, sensorEvent.timestamp);
            return;
        }
        SensorManager.getAngleChange(angleChangeBuffer, rotationMatrixBuffer, prevRotationMatrix);
        prevRotationMatrix = rotationMatrixBuffer.clone();
        if (!calibrated) {
            // ... but for some reason the rotation between first and second event is about
            // 90 degrees, if the second event is used as initial rotation everything works fine
            calibrated = true;
            handleTiltAndTimestamp(0.0d, sensorEvent.timestamp);
            return;
        }
        azimuth += angleChangeBuffer[0];
        handleTiltAndTimestamp(Math.toDegrees(azimuth), sensorEvent.timestamp);
    }

    @Override
    public void unregister(SensorManager sensorManager) {
        prevRotationMatrix = null;
        calibrated = false;
        azimuth = 0.0f;
        super.unregister(sensorManager);
    }
}
