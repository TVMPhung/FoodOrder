package com.example.foodorder.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodorder.model.Food;

import java.util.List;

@Dao
public interface FoodDao {
    @Insert
    long insert(Food food);

    @Update
    void update(Food food);

    @Delete
    void delete(Food food);

    @Query("SELECT * FROM foods WHERE id = :foodId LIMIT 1")
    LiveData<Food> getFoodById(int foodId);

    @Query("SELECT * FROM foods ORDER BY name ASC")
    LiveData<List<Food>> getAllFoods();

    @Query("SELECT * FROM foods WHERE category = :category ORDER BY name ASC")
    LiveData<List<Food>> getFoodsByCategory(String category);

    @Query("SELECT * FROM foods WHERE name LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%' ORDER BY name ASC")
    LiveData<List<Food>> searchFoods(String searchQuery);

    @Query("SELECT * FROM foods WHERE isAvailable = 1 ORDER BY name ASC")
    LiveData<List<Food>> getAvailableFoods();

    @Query("SELECT DISTINCT category FROM foods ORDER BY category ASC")
    LiveData<List<String>> getAllCategories();
}
