package com.example.foodorder.ui.food;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorder.databinding.ActivityFoodDetailBinding;
import com.example.foodorder.model.CartItem;
import com.example.foodorder.model.Food;
import com.example.foodorder.repository.CartRepository;
import com.example.foodorder.repository.FoodRepository;
import com.example.foodorder.ui.review.ReviewActivity;
import com.example.foodorder.utils.SessionManager;

import java.util.Locale;

public class FoodDetailActivity extends AppCompatActivity {
    private ActivityFoodDetailBinding binding;
    private FoodRepository foodRepository;
    private CartRepository cartRepository;
    private SessionManager sessionManager;
    private Food currentFood;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        foodRepository = new FoodRepository(getApplication());
        cartRepository = new CartRepository(getApplication());
        sessionManager = new SessionManager(this);

        setupToolbar();
        setupQuantityControls();
        setupButtons();
        
        int foodId = getIntent().getIntExtra("FOOD_ID", -1);
        if (foodId != -1) {
            loadFoodDetails(foodId);
        } else {
            finish();
        }
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupQuantityControls() {
        binding.btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                binding.tvQuantity.setText(String.valueOf(quantity));
            }
        });

        binding.btnPlus.setOnClickListener(v -> {
            quantity++;
            binding.tvQuantity.setText(String.valueOf(quantity));
        });
    }

    private void setupButtons() {
        binding.btnAddToCart.setOnClickListener(v -> addToCart());
        
        binding.btnViewReviews.setOnClickListener(v -> {
            if (currentFood != null) {
                Intent intent = new Intent(this, ReviewActivity.class);
                intent.putExtra("FOOD_ID", currentFood.getId());
                startActivity(intent);
            }
        });
    }

    private void loadFoodDetails(int foodId) {
        foodRepository.getFoodById(foodId).observe(this, food -> {
            if (food != null) {
                currentFood = food;
                displayFoodDetails(food);
            }
        });
    }

    private void displayFoodDetails(Food food) {
        binding.tvFoodName.setText(food.getName());
        binding.tvDescription.setText(food.getDescription());
        binding.tvIngredients.setText(food.getIngredients());
        binding.tvPrice.setText(String.format(Locale.US, "$%.2f", food.getPrice()));
        binding.ratingBar.setRating((float) food.getAverageRating());
        binding.tvReviewCount.setText(String.format(Locale.US, "(%d reviews)", food.getReviewCount()));
        
        if (food.isAvailable()) {
            binding.tvAvailability.setText("Available");
            binding.tvAvailability.setTextColor(getResources().getColor(android.R.color.holo_green_dark, null));
            binding.btnAddToCart.setEnabled(true);
        } else {
            binding.tvAvailability.setText("Not Available");
            binding.tvAvailability.setTextColor(getResources().getColor(android.R.color.holo_red_dark, null));
            binding.btnAddToCart.setEnabled(false);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(food.getName());
        }
    }

    private void addToCart() {
        if (currentFood == null) {
            return;
        }

        int userId = sessionManager.getUserId();
        if (userId == -1) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if item already exists in cart
        cartRepository.getCartItem(userId, currentFood.getId(), existingItem -> {
            if (existingItem != null) {
                // Update existing item
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                cartRepository.update(existingItem);
            } else {
                // Add new item
                CartItem newItem = new CartItem(
                        userId,
                        currentFood.getId(),
                        currentFood.getName(),
                        currentFood.getPrice(),
                        currentFood.getImageUrl(),
                        quantity
                );
                cartRepository.insert(newItem);
            }

            runOnUiThread(() -> {
                Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
