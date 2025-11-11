package com.example.foodorder;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorder.repository.CartRepository;
import com.example.foodorder.ui.auth.LoginActivity;
import com.example.foodorder.ui.food.FoodListActivity;
import com.example.foodorder.utils.NotificationHelper;
import com.example.foodorder.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager sessionManager = new SessionManager(this);

        // Check cart notification
        if (sessionManager.isLoggedIn()) {
            checkCartNotification(sessionManager.getUserId());
            
            // Navigate to FoodListActivity
            Intent intent = new Intent(this, FoodListActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Navigate to LoginActivity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void checkCartNotification(int userId) {
        CartRepository cartRepository = new CartRepository(getApplication());
        cartRepository.getCartItemCount(userId).observe(this, count -> {
            if (count != null && count > 0) {
                NotificationHelper.showCartNotification(this, count);
            }
        });
    }
}