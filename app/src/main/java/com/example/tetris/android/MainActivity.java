package com.example.tetris.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.tetris.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        Button[] buttons = {binding.singlePlayerButton};
        Intent[] buttonIntents = {
                new Intent(this, SinglePlayerActivity.class)
        };
        for(int i = 0; i < buttons.length; i++) {
            final Intent currIntent = buttonIntents[i];
            buttons[i].setOnClickListener(view -> view.getContext().startActivity(currIntent));
        }

        binding.settingsButton.setOnClickListener(view -> view.getContext().startActivity(new Intent(this, SettingsActivity.class)));

        // temp
        final String msg = "Available in the future";
        binding.duelButton.setOnClickListener(view -> {
            Toast toast = Toast.makeText(view.getContext(), msg, Toast.LENGTH_SHORT);
            toast.show();
        });
        binding.scoresButton.setOnClickListener(view -> {
            Toast toast = Toast.makeText(view.getContext(), msg, Toast.LENGTH_SHORT);
            toast.show();
        });

        setContentView(binding.getRoot());
    }
}