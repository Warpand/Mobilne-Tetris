package com.example.tetris.android;

import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tetris.core.Settings;
import com.example.tetris.databinding.SettingsActivityBinding;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        final Settings.SettingsWriter settings = new Settings.SettingsWriter(getApplicationContext());
        SettingsActivityBinding binding = SettingsActivityBinding.inflate(getLayoutInflater());

        if(settings.getTiltDetectorType() == Settings.tiltDetectorType.ROTATION_VECTOR)
            binding.rotationVectorButton.toggle();
        else if(settings.getTiltDetectorType() == Settings.tiltDetectorType.GYROSCOPE)
            binding.gyroscopeButton.toggle();

        binding.tiltDetectorRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if(i == binding.rotationVectorButton.getId())
                settings.setTiltDetectorType(Settings.tiltDetectorType.ROTATION_VECTOR);
            else if(i == binding.gyroscopeButton.getId())
                settings.setTiltDetectorType(Settings.tiltDetectorType.GYROSCOPE);
        });

        if(settings.getGeneratorType() == Settings.generatorType.RANDOM)
            binding.stdGeneratorButton.toggle();
        else if(settings.getGeneratorType() == Settings.generatorType.SEQUENCE_RANDOM)
            binding.sequenceGeneratorButton.toggle();

        binding.generatorRadioGroup.setOnCheckedChangeListener(((radioGroup, i) -> {
            if(i == binding.stdGeneratorButton.getId())
                settings.setGeneratorType(Settings.generatorType.RANDOM);
            else if(i == binding.sequenceGeneratorButton.getId())
                settings.setGeneratorType(Settings.generatorType.SEQUENCE_RANDOM);
        }));

        binding.saveButton.setOnClickListener(view -> settings.save(getApplicationContext()));

        setContentView(binding.getRoot());
    }
}
