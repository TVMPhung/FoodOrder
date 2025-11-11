package com.example.foodorder.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.foodorder.dao.ReviewDao;
import com.example.foodorder.database.AppDatabase;
import com.example.foodorder.model.Review;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReviewRepository {
    private ReviewDao reviewDao;
    private ExecutorService executorService;

    public ReviewRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        reviewDao = database.reviewDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Review review) {
        executorService.execute(() -> reviewDao.insert(review));
    }

    public LiveData<List<Review>> getReviewsByFoodId(int foodId) {
        return reviewDao.getReviewsByFoodId(foodId);
    }

    public LiveData<Float> getAverageRating(int foodId) {
        return reviewDao.getAverageRating(foodId);
    }

    public LiveData<Integer> getReviewCount(int foodId) {
        return reviewDao.getReviewCount(foodId);
    }

    public void getUserReviewForFood(int userId, int foodId, OnReviewFetchListener listener) {
        executorService.execute(() -> {
            List<Review> reviews = reviewDao.getUserReviewForFood(userId, foodId);
            if (listener != null) {
                listener.onReviewFetched(reviews);
            }
        });
    }

    public interface OnReviewFetchListener {
        void onReviewFetched(List<Review> reviews);
    }
}
