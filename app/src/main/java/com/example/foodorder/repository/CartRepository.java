package com.example.foodorder.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.foodorder.dao.CartItemDao;
import com.example.foodorder.database.AppDatabase;
import com.example.foodorder.model.CartItem;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartRepository {
    private CartItemDao cartItemDao;
    private ExecutorService executorService;

    public CartRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        cartItemDao = database.cartItemDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(CartItem cartItem) {
        executorService.execute(() -> cartItemDao.insert(cartItem));
    }

    public void update(CartItem cartItem) {
        executorService.execute(() -> cartItemDao.update(cartItem));
    }

    public void delete(CartItem cartItem) {
        executorService.execute(() -> cartItemDao.delete(cartItem));
    }

    public void getCartItem(int userId, int foodId, OnCartItemFetchListener listener) {
        executorService.execute(() -> {
            CartItem cartItem = cartItemDao.getCartItem(userId, foodId);
            if (listener != null) {
                listener.onCartItemFetched(cartItem);
            }
        });
    }

    public LiveData<List<CartItem>> getCartItemsByUserId(int userId) {
        return cartItemDao.getCartItemsByUserId(userId);
    }

    public void clearCart(int userId) {
        executorService.execute(() -> cartItemDao.clearCart(userId));
    }

    public LiveData<Integer> getCartItemCount(int userId) {
        return cartItemDao.getCartItemCount(userId);
    }

    public LiveData<Double> getCartTotal(int userId) {
        return cartItemDao.getCartTotal(userId);
    }

    public interface OnCartItemFetchListener {
        void onCartItemFetched(CartItem cartItem);
    }
}
