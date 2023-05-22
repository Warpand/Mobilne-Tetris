package com.example.tetris.android;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.example.tetris.bluetooth.BluetoothDiscoveryReceiver;
import com.example.tetris.databinding.DuelJoinActivityBinding;

public class DuelJoinActivity extends AbstractBluetoothActivity {
    private BluetoothDiscoveryReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver = new BluetoothDiscoveryReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);

        DuelJoinActivityBinding binding = DuelJoinActivityBinding.inflate(getLayoutInflater());

        binding.joinButton.setOnClickListener(
                view -> {
                    turnEverythingOn();
                    if(!bluetoothAdapter.isEnabled())
                        return;
                    try {
                        bluetoothAdapter.startDiscovery();
                    }
                    catch(SecurityException e) {
                        Log.e("BLUETOOTH", "SECURITY PERMISSIONS WERE DENIED", e);
                    }
                }
        );

        setContentView(binding.getRoot());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
