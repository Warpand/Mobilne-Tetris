package com.example.tetris.android;

import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tetris.R;
import com.example.tetris.database.AppDatabase;
import com.example.tetris.database.EntryDao;
import com.example.tetris.databinding.ScoresActivityBinding;

public class ScoresActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        EntryDao dao = AppDatabase.getDb(getApplicationContext()).entryDao();
        ScoresActivityBinding binding = ScoresActivityBinding.inflate(getLayoutInflater());

        int playedGames = dao.getCount();
        int totalDeletedRows = dao.getTotalDeletedRows();

        String text = getResources().getString(R.string.scores_head_text)
                .replace("*", String.valueOf(playedGames))
                .replace("?", String.valueOf(totalDeletedRows));
        binding.headText.setText(text);

        setContentView(binding.getRoot());
    }
}
