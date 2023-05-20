package com.example.tetris.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BluetoothDiscoveryReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            Toast toast = Toast.makeText(context, "START", Toast.LENGTH_SHORT);
            toast.show();
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            Toast toast = Toast.makeText(context, "STOP", Toast.LENGTH_SHORT);
            toast.show();
        } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            //bluetooth device found
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Toast toast = Toast.makeText(context, "FOUND!", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            Toast toast = Toast.makeText(context, "WHAT!!?", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
