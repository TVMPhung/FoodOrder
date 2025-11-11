package com.example.foodorder.ui.food;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodorder.R;
import com.example.foodorder.adapter.FoodAdapter;
import com.example.foodorder.databinding.ActivityFoodListBinding;
import com.example.foodorder.model.Food;
import com.example.foodorder.repository.FoodRepository;
import com.example.foodorder.ui.auth.LoginActivity;
import com.example.foodorder.ui.cart.CartActivity;
import com.example.foodorder.ui.chat.ChatActivity;
import com.example.foodorder.ui.location.MapActivity;
import com.example.foodorder.ui.order.OrderHistoryActivity;
import com.example.foodorder.utils.SessionManager;

import java.util.List;

public class FoodListActivity extends AppCompatActivity {
    private ActivityFoodListBinding binding;
    private FoodAdapter foodAdapter;
    private FoodRepository foodRepository;
    private SessionManager sessionManager;
    private LiveData<List<Food>> currentFoodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        foodRepository = new FoodRepository(getApplication());

        setupToolbar();
        setupRecyclerView();
        setupSearchListener();
        setupSwipeRefresh();
        setupFabButton();
        
        loadFoods();
        populateSampleData();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Food Menu");
        }
    }

    private void setupRecyclerView() {
        foodAdapter = new FoodAdapter(food -> {
            Intent intent = new Intent(this, FoodDetailActivity.class);
            intent.putExtra("FOOD_ID", food.getId());
            startActivity(intent);
        });

        binding.rvFoods.setLayoutManager(new LinearLayoutManager(this));
        binding.rvFoods.setAdapter(foodAdapter);
    }

    private void setupSearchListener() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    searchFoods(s.toString());
                } else {
                    loadFoods();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            loadFoods();
            binding.swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setupFabButton() {
        binding.fabCart.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });
    }

    private void loadFoods() {
        if (currentFoodList != null) {
            currentFoodList.removeObservers(this);
        }
        currentFoodList = foodRepository.getAllFoods();
        currentFoodList.observe(this, foods -> {
            if (foods != null) {
                foodAdapter.setFoodList(foods);
            }
        });
    }

    private void searchFoods(String query) {
        if (currentFoodList != null) {
            currentFoodList.removeObservers(this);
        }
        currentFoodList = foodRepository.searchFoods(query);
        currentFoodList.observe(this, foods -> {
            if (foods != null) {
                foodAdapter.setFoodList(foods);
            }
        });
    }

    private void populateSampleData() {
        // Add sample foods if database is empty
        foodRepository.getAllFoods().observe(this, foods -> {
            if (foods == null || foods.isEmpty()) {
                addSampleFoods();
            }
        });
    }

    private void addSampleFoods() {
        foodRepository.insert(new Food("Margherita Pizza", "Classic pizza with tomato sauce and mozzarella", 12.99, "", "Pizza", "Tomato, Mozzarella, Basil"));
        foodRepository.insert(new Food("Cheeseburger", "Juicy beef patty with cheese, lettuce, and tomato", 9.99, "", "Burgers", "Beef, Cheese, Lettuce, Tomato"));
        foodRepository.insert(new Food("Caesar Salad", "Fresh romaine lettuce with Caesar dressing", 7.99, "", "Salads", "Romaine, Parmesan, Croutons"));
        foodRepository.insert(new Food("Spaghetti Carbonara", "Italian pasta with bacon and creamy sauce", 11.99, "", "Pasta", "Pasta, Bacon, Eggs, Parmesan"));
        foodRepository.insert(new Food("Chicken Wings", "Spicy buffalo wings with ranch dressing", 8.99, "", "Appetizers", "Chicken, Buffalo Sauce"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.action_orders) {
            startActivity(new Intent(this, OrderHistoryActivity.class));
            return true;
        } else if (id == R.id.action_map) {
            startActivity(new Intent(this, MapActivity.class));
            return true;
        } else if (id == R.id.action_chat) {
            startActivity(new Intent(this, ChatActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            sessionManager.logout();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
