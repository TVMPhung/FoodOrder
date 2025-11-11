package com.example.foodorder.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.foodorder.dao.OrderDao;
import com.example.foodorder.database.AppDatabase;
import com.example.foodorder.model.Order;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderRepository {
    private OrderDao orderDao;
    private ExecutorService executorService;

    public OrderRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        orderDao = database.orderDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Order order, OnOrderInsertedListener listener) {
        executorService.execute(() -> {
            long orderId = orderDao.insert(order);
            if (listener != null) {
                listener.onOrderInserted(orderId);
            }
        });
    }

    public void update(Order order) {
        executorService.execute(() -> orderDao.update(order));
    }

    public LiveData<List<Order>> getOrdersByUserId(int userId) {
        return orderDao.getOrdersByUserId(userId);
    }

    public LiveData<Order> getOrderById(int orderId) {
        return orderDao.getOrderById(orderId);
    }

    public LiveData<List<Order>> getOrdersByStatus(int userId, String status) {
        return orderDao.getOrdersByStatus(userId, status);
    }

    public interface OnOrderInsertedListener {
        void onOrderInserted(long orderId);
    }
}
