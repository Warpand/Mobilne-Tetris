package com.example.tetris.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.tetris.core.MultiplayerMessage;
import com.example.tetris.core.SocketWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BluetoothSocketWrapper implements SocketWrapper {
    private final BluetoothSocket socket;
    private final InputStream input;
    private final OutputStream output;
    private final BlockingQueue<MultiplayerMessage> msgQueue;

    public BluetoothSocketWrapper(BluetoothSocket socket) {
        this.socket = socket;
        try {
            input = socket.getInputStream();
            output = socket.getOutputStream();
        }
        catch (IOException e) {
            Log.e("BLUETOOTH", "Could not obtain the streams", e);
            throw new BluetoothException(BluetoothError.UNKNOWN, e);
        }
        msgQueue = new LinkedBlockingQueue<>();
        Thread readerThread = new Thread(this::readFromSocket);
        readerThread.start();
    }

    @Override
    public void write(MultiplayerMessage m) {
        try {
            output.write(m.toRawMessage());
        }
        catch (IOException e) {
            Log.d("BLUETOOTH", "Could not write to the socket");
        }
    }

    @Override
    public MultiplayerMessage read() {
        return msgQueue.poll();
    }

    @Override
    public MultiplayerMessage readBlocking() {
        try {
            return msgQueue.take();
        } catch (InterruptedException e) {
            Log.e("BLUETOOTH", "Interrupted while performing a blocking read", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            input.close();
            output.close();
        }
        catch (IOException e) {
            Log.e("BLUETOOTH", "Exception while closing socket's streams", e);
        }
        try {
            socket.close();
        }
        catch (IOException e) {
            Log.e("BLUETOOTH", "Exception while closing the socket", e);
        }
    }

    private void readFromSocket() {
        byte[] buf = new byte[MultiplayerMessage.RAW_MSG_SIZE];
        while(true) {
            int bytesRead = 0;
            while(bytesRead < MultiplayerMessage.RAW_MSG_SIZE) {
                try {
                    bytesRead += input.read(buf, bytesRead, MultiplayerMessage.RAW_MSG_SIZE - bytesRead);
                } catch (IOException e) {
                    Log.d("BLUETOOTH", "Error while reading from socket's input");
                    return;
                }
            }
            try {
                msgQueue.put(MultiplayerMessage.fromRawMessage(buf));
            }
            catch (InterruptedException e) {
                Log.e("BLUETOOTH", "Error while putting a msg to the queue", e);
            }
            if((int)buf[0] == MultiplayerMessage.TYPE_DONE)
                return;
        }
    }
}
