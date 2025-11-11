package com.example.foodorder.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodorder.model.Order;

import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    long insert(Order order);

    @Update
    void update(Order order);

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY timestamp DESC")
    LiveData<List<Order>> getOrdersByUserId(int userId);

    @Query("SELECT * FROM orders WHERE id = :orderId LIMIT 1")
    LiveData<Order> getOrderById(int orderId);

    @Query("SELECT * FROM orders WHERE userId = :userId AND status = :status ORDER BY timestamp DESC")
    LiveData<List<Order>> getOrdersByStatus(int userId, String status);

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY timestamp DESC")
    List<Order> getOrdersSync(int userId);
}
