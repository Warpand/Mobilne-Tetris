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
            prevRotationMatrix = rotationMatrixBuffer.clone();
            return;
        }
        SensorManager.getAngleChange(angleChangeBuffer, rotationMatrixBuffer, prevRotationMatrix);
        prevRotationMatrix = rotationMatrixBuffer.clone();
        if (!calibrated) {
            calibrated = true;
            return;
        }
        azimuth += angleChangeBuffer[0];
        handleTiltAndTimestamp(Math.toDegrees(azimuth), sensorEvent.timestamp);
    }
}
