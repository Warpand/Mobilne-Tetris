package com.example.tetris.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = { DataEntry.class }, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EntryDao entryDao();
    private static AppDatabase db = null;

    public static AppDatabase getDb(Context appContext) {
        if(db == null)
            db = Room.databaseBuilder(appContext, AppDatabase.class, "TetrisDatabase").allowMainThreadQueries().build();
        return db;
    }
}
