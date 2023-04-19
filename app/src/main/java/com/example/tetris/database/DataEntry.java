package com.example.tetris.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DataEntry {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int score;
    @ColumnInfo(name = "deleted_rows")
    public int deletedRows;
    public int moves;

    public DataEntry(int score, int deletedRows, int moves) {
        this.score = score;
        this.deletedRows = deletedRows;
        this.moves = moves;
    }
}
