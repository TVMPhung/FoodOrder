package com.example.foodorder.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodorder.model.CartItem;

import java.util.List;

@Dao
public interface CartItemDao {
    @Insert
    long insert(CartItem cartItem);

    @Update
    void update(CartItem cartItem);

    @Delete
    void delete(CartItem cartItem);

    @Query("SELECT * FROM cart_items WHERE userId = :userId")
    LiveData<List<CartItem>> getCartItemsByUserId(int userId);

    @Query("SELECT * FROM cart_items WHERE userId = :userId AND foodId = :foodId LIMIT 1")
    CartItem getCartItem(int userId, int foodId);

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    void clearCart(int userId);

    @Query("SELECT COUNT(*) FROM cart_items WHERE userId = :userId")
    LiveData<Integer> getCartItemCount(int userId);

    @Query("SELECT SUM(foodPrice * quantity) FROM cart_items WHERE userId = :userId")
    LiveData<Double> getCartTotal(int userId);
}
