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
    private boolean closed = false;

    public BluetoothSocketWrapper(BluetoothSocket socket) {
        this.socket = socket;
        try {
            input = socket.getInputStream();
            output = socket.getOutputStream();
        }
        catch (IOException e) {
            Log.e("BLUETOOTH", "Could not obtain the streams");
            throw new BluetoothException(BluetoothError.UNKNOWN, e);
        }
        msgQueue = new LinkedBlockingQueue<>();
        Thread readerThread = new Thread(this::readFromSocket);
        readerThread.start();
    }

    @Override
    public void write(MultiplayerMessage m) {
        if(closed)
            return;
        try {
            output.write(m.toRawMessage());
        }
        catch (IOException e) {
            Log.e("BLUETOOTH", "Error while writing to the socket's stream", e);
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
            Log.e("BLUETOOTH", "Interrupted while performing a blocking read");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        if(closed)
            return;
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
        closed = true;
    }

    private void readFromSocket() {
        byte[] buf = new byte[MultiplayerMessage.RAW_MSG_SIZE];
        while(true) {
            int bytesRead = 0;
            while(bytesRead < MultiplayerMessage.RAW_MSG_SIZE) {
                try {
                    bytesRead += input.read(buf, bytesRead, MultiplayerMessage.RAW_MSG_SIZE - bytesRead);
                } catch (IOException e) {
                    // this should actually sometimes happen
                    Log.w("BLUETOOTH", "Error while reading from socket's input");
                    try {
                        msgQueue.put(new MultiplayerMessage(MultiplayerMessage.TYPE_DONE, new byte[0]));
                    }
                    catch (InterruptedException ie) {
                        Log.e("BLUETOOTH", "Error while putting a msg to the queue", ie);
                    }
                    return;
                }
            }
            try {
                msgQueue.put(MultiplayerMessage.fromRawMessage(buf));
            }
            catch (InterruptedException e) {
                Log.e("BLUETOOTH", "Error while putting a msg to the queue", e);
                return;
            }
            if((int)buf[0] == MultiplayerMessage.TYPE_DONE)
                return;
        }
    }
}
