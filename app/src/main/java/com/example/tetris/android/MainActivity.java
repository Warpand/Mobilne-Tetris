package com.example.tetris.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import com.example.tetris.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        Button[] buttons = {binding.singlePlayerButton};
        Intent[] buttonIntents = {new Intent(this, SinglePlayerActivity.class)};
        for(int i = 0; i < buttons.length; i++) {
            final Intent currIntent = buttonIntents[i];
            buttons[i].setOnClickListener(view -> view.getContext().startActivity(currIntent));
        }

        setContentView(binding.getRoot());
    }
}