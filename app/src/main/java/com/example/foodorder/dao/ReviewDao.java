package com.example.foodorder.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.foodorder.model.Review;

import java.util.List;

@Dao
public interface ReviewDao {
    @Insert
    long insert(Review review);

    @Query("SELECT * FROM reviews WHERE foodId = :foodId ORDER BY timestamp DESC")
    LiveData<List<Review>> getReviewsByFoodId(int foodId);

    @Query("SELECT * FROM reviews WHERE userId = :userId AND foodId = :foodId")
    List<Review> getUserReviewForFood(int userId, int foodId);

    @Query("SELECT AVG(rating) FROM reviews WHERE foodId = :foodId")
    LiveData<Float> getAverageRating(int foodId);

    @Query("SELECT COUNT(*) FROM reviews WHERE foodId = :foodId")
    LiveData<Integer> getReviewCount(int foodId);
}
