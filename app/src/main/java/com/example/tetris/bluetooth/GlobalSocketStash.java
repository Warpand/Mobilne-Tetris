package com.example.tetris.bluetooth;

import android.bluetooth.BluetoothSocket;

public abstract class GlobalSocketStash {
    private static BluetoothSocket socket;

    public static void stash(BluetoothSocket newSocket) {
        socket = newSocket;
    }

    public static BluetoothSocket obtain() {
        BluetoothSocket result = socket;
        socket = null;
        return result;
    }
}
