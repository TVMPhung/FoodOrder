package com.example.foodorder.ui.order;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodorder.adapter.OrderAdapter;
import com.example.foodorder.databinding.ActivityOrderHistoryBinding;
import com.example.foodorder.repository.OrderRepository;
import com.example.foodorder.utils.SessionManager;

public class OrderHistoryActivity extends AppCompatActivity {
    private ActivityOrderHistoryBinding binding;
    private OrderAdapter orderAdapter;
    private OrderRepository orderRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        orderRepository = new OrderRepository(getApplication());
        sessionManager = new SessionManager(this);

        setupToolbar();
        setupRecyclerView();
        loadOrders();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Order History");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        orderAdapter = new OrderAdapter(order -> {
            // Order click - could show order details
        });

        binding.rvOrders.setLayoutManager(new LinearLayoutManager(this));
        binding.rvOrders.setAdapter(orderAdapter);
    }

    private void loadOrders() {
        int userId = sessionManager.getUserId();
        orderRepository.getOrdersByUserId(userId).observe(this, orders -> {
            if (orders != null && !orders.isEmpty()) {
                orderAdapter.setOrders(orders);
                binding.rvOrders.setVisibility(View.VISIBLE);
                binding.tvEmptyOrders.setVisibility(View.GONE);
            } else {
                binding.rvOrders.setVisibility(View.GONE);
                binding.tvEmptyOrders.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
