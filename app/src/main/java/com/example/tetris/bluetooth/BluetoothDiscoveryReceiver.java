package com.example.tetris.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.example.tetris.R;

import java.util.Set;

public class BluetoothDiscoveryReceiver extends BroadcastReceiver {
    private final BluetoothAdapter bluetoothAdapter;
    private final DevicesView devicesView;
    private final TextView statusTextView;

    public BluetoothDiscoveryReceiver(BluetoothAdapter bluetoothAdapter,
                                      DevicesView devicesView,
                                      TextView statusTextView) {
        this.bluetoothAdapter = bluetoothAdapter;
        this.devicesView = devicesView;
        this.statusTextView = statusTextView;
    }

    @Override
    public void onReceive(Context context, Intent intent) throws SecurityException {
        String action = intent.getAction();
        if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            devicesView.clear();
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            if(pairedDevices.size() > 0)
                devicesView.addMultipleDevices(pairedDevices);
            /*Toast toast = Toast.makeText(context, "SCANNING STARTED", Toast.LENGTH_SHORT);
            toast.show();*/
            statusTextView.setText(R.string.scanning);

        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            /*Toast toast = Toast.makeText(context, "SCANNING FINISHED", Toast.LENGTH_SHORT);
            toast.show();*/
            statusTextView.setText(R.string.idle);
        } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            devicesView.addNewDevice(device);
        }
        else {
            Log.d("BluetoothDiscoveryReceiver",
                    "Received an intent that should never be received");
        }
    }
}
