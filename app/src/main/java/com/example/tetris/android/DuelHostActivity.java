package com.example.tetris.android;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.tetris.bluetooth.BluetoothError;
import com.example.tetris.bluetooth.BluetoothSocketServer;
import com.example.tetris.databinding.DuelHostActivityBinding;

public class DuelHostActivity extends AbstractBluetoothActivity {
    private BluetoothSocketServer server = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DuelHostActivityBinding binding = DuelHostActivityBinding.inflate(getLayoutInflater());

        binding.hostButton.setOnClickListener(
            view -> {
                checkPermissions();
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
                try {
                    startActivity(discoverableIntent);
                    server = new BluetoothSocketServer(
                            bluetoothAdapter,
                            this,
                            new Intent(this, MultiplayerHostActivity.class)
                    );
                    server.start();
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
        if(resultCode == RESULT_CANCELED)
            toastForError(BluetoothError.USER_DENIED_OPERATION);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(server != null)
            server.close();
    }
}
