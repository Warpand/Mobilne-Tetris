package com.example.tetris.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;

public class BluetoothSocketServer extends Thread {
    private BluetoothServerSocket server;
    private final Context context;
    private final Intent intent;

    public BluetoothSocketServer(BluetoothAdapter adapter, Context context, Intent intent) {
        try {
            server = adapter.listenUsingRfcommWithServiceRecord(
                    BluetoothConstants.NAME, BluetoothConstants.APP_UUID
            );
        }
        catch (IOException e) {
            Log.e("BLUETOOTH SERVER", "Could not create the server socket.", e);
        }
        catch (SecurityException e) {
            Log.e("BLUETOOTH", "Security error", e);
        }
        this.context = context;
        this.intent = intent;
    }

    @Override
    public void run() {
        BluetoothSocket socket = null;
        while(true) {
            try {
                socket = server.accept();
            } catch (NullPointerException nullPointerException) {
                // close was called by Activity::onDestroy
                break;
            } catch (IOException e) {
                Log.w("BLUETOOTH SERVER", "BluetoothSocket::accept failed", e);
            }
            if(socket != null) {
                GlobalSocketStash.stash(socket);
                close();
                context.startActivity(intent);
                break;
            }
        }
    }

    public void close() {
        try {
            server.close();
        }
        catch (IOException e) {
            Log.e("BLUETOOTH SERVER", "Exception from BluetoothServerSocket::close");
        }
        catch (NullPointerException nullPointerException) {
            return;
        }
        server = null;
    }
}
