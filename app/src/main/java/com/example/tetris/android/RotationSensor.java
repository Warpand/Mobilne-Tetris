package com.example.tetris.android;

import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public interface RotationSensor extends SensorEventListener {
    void register(SensorManager sensorManager);

    void unregister(SensorManager sensorManager);
}
