package com.example.wehelie.grocipe;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = AppDatabase.TABLE_NAME_GROCIPE)
public class Grocipe implements Serializable{

    @PrimaryKey(autoGenerate = true)
    public int groceryItem_id;

    public String dishName;

    public String recipe;

    public String mealtype;

}
