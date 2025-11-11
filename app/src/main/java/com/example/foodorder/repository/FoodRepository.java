package com.example.foodorder.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.foodorder.dao.FoodDao;
import com.example.foodorder.database.AppDatabase;
import com.example.foodorder.model.Food;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FoodRepository {
    private FoodDao foodDao;
    private ExecutorService executorService;

    public FoodRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        foodDao = database.foodDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Food food) {
        executorService.execute(() -> foodDao.insert(food));
    }

    public void update(Food food) {
        executorService.execute(() -> foodDao.update(food));
    }

    public void delete(Food food) {
        executorService.execute(() -> foodDao.delete(food));
    }

    public LiveData<Food> getFoodById(int foodId) {
        return foodDao.getFoodById(foodId);
    }

    public LiveData<List<Food>> getAllFoods() {
        return foodDao.getAllFoods();
    }

    public LiveData<List<Food>> getFoodsByCategory(String category) {
        return foodDao.getFoodsByCategory(category);
    }

    public LiveData<List<Food>> searchFoods(String searchQuery) {
        return foodDao.searchFoods(searchQuery);
    }

    public LiveData<List<Food>> getAvailableFoods() {
        return foodDao.getAvailableFoods();
    }

    public LiveData<List<String>> getAllCategories() {
        return foodDao.getAllCategories();
    }
}
