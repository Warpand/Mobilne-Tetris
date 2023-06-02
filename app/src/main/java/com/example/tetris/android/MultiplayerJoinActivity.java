package com.example.tetris.android;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tetris.bluetooth.GlobalSocketStash;
import com.example.tetris.databinding.MultiplayerJoinActivityBinding;

import java.io.IOException;

public class MultiplayerJoinActivity extends AppCompatActivity {
    private BluetoothSocket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        socket = GlobalSocketStash.obtain();

        MultiplayerJoinActivityBinding binding = MultiplayerJoinActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            socket.close();
        }
        catch (IOException e) {
            Log.e("ACTIVITY", "socket.close() failed", e);
        }
    }
}
