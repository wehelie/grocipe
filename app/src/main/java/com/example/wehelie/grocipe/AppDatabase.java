package com.example.wehelie.grocipe;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Grocipe.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "GrocipeDB";
    public static final String TABLE_NAME_GROCIPE = "grocipe";

    public abstract GrocipeDao GrocipeDao();

}