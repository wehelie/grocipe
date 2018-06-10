package com.example.wehelie.grocipe;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface GrocipeDao {
    @Insert
    long insertGroceryItem(Grocipe grocipe);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGrocipeList(List<Grocipe> grocipeList);

    @Query("SELECT * FROM " + AppDatabase.TABLE_NAME_GROCIPE)
    List<Grocipe> fetchAllGrocipe();


    @Query("SELECT * FROM " + AppDatabase.TABLE_NAME_GROCIPE + " WHERE mealtype = :mealtype")
    List<Grocipe> fetchGrocipeByCategory(String mealtype);


    @Query("SELECT * FROM " + AppDatabase.TABLE_NAME_GROCIPE + " WHERE groceryItem_id = :grocipeid")
    Grocipe fetchGrocipeListById(int grocipeid);

    @Update
    int updateGrocipe(Grocipe grocipe);

    @Delete
    int deleteGrocipe(Grocipe grocipe);
}
