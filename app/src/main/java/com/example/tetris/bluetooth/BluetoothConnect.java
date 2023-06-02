package com.example.tetris.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;

public class BluetoothConnect extends Thread {
    private final Context context;
    private final Intent intent;
    private BluetoothSocket socket;

    public BluetoothConnect(BluetoothDevice device, Context context, Intent intent) {
        try {
            socket = device.createRfcommSocketToServiceRecord(BluetoothConstants.APP_UUID);
        }
        catch (IOException e) {
            Log.e("BLUETOOTH CLIENT", "Socket creation failed", e);
        }
        catch (SecurityException e) {
            Log.e("BLUETOOTH", "Security error", e);
        }
        this.context = context;
        this.intent = intent;
    }

    @Override
    public void run() {
        try {
            socket.connect();
        } catch (IOException connectException) {
            try {
                socket.close();
            } catch (IOException closeException) {
                Log.e("BLUETOOTH CLIENT", "Could not close the client socket", closeException);
            }
            return;
        }
        catch (SecurityException e) {
            Log.e("BLUETOOTH", "Security error", e);
            return;
        }
        GlobalSocketStash.stash(socket);
        context.startActivity(intent);
    }
}
