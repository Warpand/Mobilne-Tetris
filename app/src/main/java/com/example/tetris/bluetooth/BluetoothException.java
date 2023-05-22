package com.example.tetris.bluetooth;

public class BluetoothException extends RuntimeException {
    private final BluetoothError errorType;

    public BluetoothException(BluetoothError errorType) {
        super();
        this.errorType = errorType;
    }

    public BluetoothException(BluetoothError errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public BluetoothError getErrorType() {
        return errorType;
    }
}
