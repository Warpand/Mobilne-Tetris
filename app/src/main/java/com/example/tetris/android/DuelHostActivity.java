package com.example.tetris.android;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.tetris.databinding.DuelHostActivityBinding;

public class DuelHostActivity extends AbstractBluetoothActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DuelHostActivityBinding binding = DuelHostActivityBinding.inflate(getLayoutInflater());

        binding.hostButton.setOnClickListener(
                view -> {
                    turnEverythingOn();
                    Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
                    try {
                        startActivity(discoverableIntent);
                    }
                    catch(SecurityException e) {
                        Log.e("BLUETOOTH", "SECURITY PERMISSIONS WERE DENIED", e);
                    }
                }
        );

        setContentView(binding.getRoot());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // maybe some toast?
    }
}
