package com.example.tetris.android;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tetris.bluetooth.BluetoothSocketWrapper;
import com.example.tetris.bluetooth.GlobalSocketStash;
import com.example.tetris.core.GameEvent;
import com.example.tetris.core.MultiplayerHostGameEngine;
import com.example.tetris.core.MultiplayerMessage;
import com.example.tetris.databinding.MultiplayerJoinActivityBinding;

public class MultiplayerHostActivity extends AppCompatActivity {
    private BluetoothSocketWrapper socket;
    private MultiplayerHostGameEngine gameEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        socket = new BluetoothSocketWrapper(GlobalSocketStash.obtain());

        MultiplayerJoinActivityBinding binding = MultiplayerJoinActivityBinding.inflate(getLayoutInflater());
        gameEngine = new MultiplayerHostGameEngine(socket);
        gameEngine.registerObserver(binding.dummy);
        setContentView(binding.getRoot());
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameEngine.registerEvent(new GameEvent.SetSpeedEvent(false));
        gameEngine.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameEngine.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.write(new MultiplayerMessage(MultiplayerMessage.TYPE_DONE, new byte[0]));
        socket.close();
    }
}
