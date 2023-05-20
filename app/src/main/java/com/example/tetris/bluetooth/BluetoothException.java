package com.example.tetris.bluetooth;

public class BluetoothException extends RuntimeException {
    public static final String unknownErrorMsg = "Unknown Error";
    public BluetoothException(String message) {
        super(message);
    }
}
