package com.example.tetris.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tetris.R;
import com.example.tetris.database.AppDatabase;
import com.example.tetris.database.DataEntry;
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

        binding.scoresRecyclerView.setAdapter(new ScoresAdapter(dao.getTopGames()));
        binding.scoresRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setContentView(binding.getRoot());
    }

    private static class ScoreHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        ScoreHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.score_view_holder);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    private static class ScoresAdapter extends RecyclerView.Adapter<ScoreHolder> {
        private static final String text = "* points in ? moves";
        private final DataEntry[] dataList;

        ScoresAdapter(DataEntry[] dataList) {
            this.dataList = dataList;
        }

        @NonNull
        @Override
        public ScoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ScoreHolder(
                    LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.scores_view_holder, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(ScoreHolder scoreHolder, final int position) {
            DataEntry data = dataList[position];
            scoreHolder.getTextView().setText(text
                    .replace("*", String.valueOf(data.score))
                    .replace("?", String.valueOf(data.moves)));
        }

        @Override
        public int getItemCount() {
            return dataList.length;
        }
    }
}
