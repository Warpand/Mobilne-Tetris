package com.example.tetris.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.example.tetris.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        binding.singlePlayerButton.setOnClickListener(view -> view.getContext().startActivity(
                new Intent(this, SinglePlayerActivity.class)));
        binding.duelHostButton.setOnClickListener(view -> view.getContext().startActivity(
                new Intent(this, DuelHostActivity.class)));
        binding.duelJoinButton.setOnClickListener(view -> view.getContext().startActivity(
                new Intent(this, DuelJoinActivity.class)));
        binding.settingsButton.setOnClickListener(view -> view.getContext().startActivity(
                new Intent(this, SettingsActivity.class)));
        binding.scoresButton.setOnClickListener(view -> view.getContext().startActivity(
                new Intent(this, ScoresActivity.class)));

        setContentView(binding.getRoot());
    }
}