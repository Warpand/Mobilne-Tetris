package com.example.tetris.android;

import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import com.example.tetris.core.GameEngine;

public class RotationAbsolute extends AbstractRotationSensor {
    private final float[] rotationMatrixBuffer = new float[9];
    private final float[] remappedRotationBuffer = new float[9];
    private final float[] orientationsBuffer = new float[3];

    RotationAbsolute(SensorManager sensorManager, GameEngine gameEngine) {
        super(sensorManager, gameEngine);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        SensorManager.getRotationMatrixFromVector(rotationMatrixBuffer, sensorEvent.values);
        SensorManager.remapCoordinateSystem(
                rotationMatrixBuffer,
                SensorManager.AXIS_X,
                SensorManager.AXIS_Z,
                remappedRotationBuffer
        );
        SensorManager.getOrientation(remappedRotationBuffer, orientationsBuffer);
        handleTiltAndTimestamp(Math.toDegrees(orientationsBuffer[2]), sensorEvent.timestamp);
    }
}
