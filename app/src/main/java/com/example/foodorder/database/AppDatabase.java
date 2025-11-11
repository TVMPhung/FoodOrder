package com.example.foodorder.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.foodorder.dao.CartItemDao;
import com.example.foodorder.dao.FoodDao;
import com.example.foodorder.dao.MessageDao;
import com.example.foodorder.dao.OrderDao;
import com.example.foodorder.dao.ReviewDao;
import com.example.foodorder.dao.UserDao;
import com.example.foodorder.model.CartItem;
import com.example.foodorder.model.Food;
import com.example.foodorder.model.Message;
import com.example.foodorder.model.Order;
import com.example.foodorder.model.Review;
import com.example.foodorder.model.User;

@Database(entities = {User.class, Food.class, CartItem.class, Order.class, Review.class, Message.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract UserDao userDao();
    public abstract FoodDao foodDao();
    public abstract CartItemDao cartItemDao();
    public abstract OrderDao orderDao();
    public abstract ReviewDao reviewDao();
    public abstract MessageDao messageDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "food_order_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
