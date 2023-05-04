package com.example.tetris.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface EntryDao {
    @Insert
    void insert(DataEntry entry);

    @Query("select count(*) from dataentry")
    int getCount();

    @Query("select sum(deleted_rows) from dataentry")
    int getTotalDeletedRows();

    @Query("select * from dataentry order by score desc limit 20")
    DataEntry[] getTopGames();
}
