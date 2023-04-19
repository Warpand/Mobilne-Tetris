package com.example.tetris.android;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.tetris.core.GameEngine;
import com.example.tetris.core.GameEvent;

public class RotationGyroscope implements RotationSensor {
    private static final float neutralPoseDelay = 0.2f;
    private static final float tiltedPoseDelay = 0.8f;
    private static final double threshold = 15d;

    private float[] posVector = {0, 1, 0};
    private final float[] quaternion = new float[4];
    private float timestamp = 0.0f;
    private static final float ns2s = 1e-9f;

    private float x = neutralPoseDelay;

    private final boolean selfCalibrating;
    private double prevD = 0;
    private static final float selfCalibrationDelay = 1.0f;
    private float selfCalibrationCooldown = selfCalibrationDelay;
    private static final double selfCalibrationMaxDelta = 1.0;
    private static final double selfCalibrationThreshold = 9.0;

    private final Sensor sensor;
    private final GameEngine gameEngine;

    RotationGyroscope(SensorManager sensorManager, GameEngine gameEngine, boolean selfCalibrating) {
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        this.gameEngine = gameEngine;
        this.selfCalibrating = selfCalibrating;
    }

    private void getQuaternion(float[] v, float dt) {
        float norm = (float)Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
        v[0] /= norm;
        v[1] /= norm;
        v[2] /= norm;
        float thetaOver2 = norm * dt / 2.0f;
        float sin = (float)Math.sin(thetaOver2);
        float cos = (float)Math.cos(thetaOver2);
        quaternion[0] = sin * v[0];
        quaternion[1] = sin * v[1];
        quaternion[2] = sin * v[2];
        quaternion[3] = cos;
    }

    @Override
    public void register(SensorManager sensorManager) {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void unregister(SensorManager sensorManager) {
        sensorManager.unregisterListener(this);
    }


    float dlog = 0.0f;
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float dt = (sensorEvent.timestamp - timestamp) * ns2s;
        if(timestamp != 0)
            getQuaternion(sensorEvent.values, dt);
        timestamp = sensorEvent.timestamp;
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, quaternion);
         posVector = new float[]{
                posVector[0] * rotationMatrix[0] + posVector[1] * rotationMatrix[3] + posVector[2] * rotationMatrix[6],
                posVector[0] * rotationMatrix[1] + posVector[1] * rotationMatrix[4] + posVector[2] * rotationMatrix[7],
                posVector[0] * rotationMatrix[2] + posVector[1] * rotationMatrix[5] + posVector[2] * rotationMatrix[8]
        };

        double sin = posVector[0] / Math.sqrt(posVector[0] * posVector[0] + posVector[1] * posVector[1]);
        double d = Math.toDegrees(Math.asin(sin));
        dlog -= dt;
        if(dlog <= 0) {
            Log.d("AZIMUTH", String.valueOf(d));
            dlog = 1;
        }
        if(Math.abs(d) > threshold) {
            x -= dt;
            if(x <= 0) {
                x = tiltedPoseDelay;
                if (d < 0)
                    gameEngine.registerEvent(new GameEvent.RotateRightEvent());
                else if (d > 0)
                    gameEngine.registerEvent(new GameEvent.RotateLeftEvent());
            }
        }
        else {
            if(selfCalibrating) {
                if(Math.abs(d - prevD) < selfCalibrationMaxDelta && Math.abs(d) < selfCalibrationThreshold) {
                    selfCalibrationCooldown -= dt;
                    if(selfCalibrationCooldown <= 0) {
                        selfCalibrationCooldown = selfCalibrationDelay;
                        posVector = new float[]{0, 1, 0};
                    }
                }
                else
                    selfCalibrationCooldown = selfCalibrationDelay;
            }
            x = neutralPoseDelay;
        }
        prevD = d;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
