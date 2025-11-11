package com.example.foodorder.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodorder.adapter.CartAdapter;
import com.example.foodorder.databinding.ActivityCartBinding;
import com.example.foodorder.model.CartItem;
import com.example.foodorder.repository.CartRepository;
import com.example.foodorder.ui.billing.BillingActivity;
import com.example.foodorder.utils.SessionManager;

import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    private ActivityCartBinding binding;
    private CartAdapter cartAdapter;
    private CartRepository cartRepository;
    private SessionManager sessionManager;
    private List<CartItem> cartItems;
    private static final double DELIVERY_FEE = 2.99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cartRepository = new CartRepository(getApplication());
        sessionManager = new SessionManager(this);

        setupToolbar();
        setupRecyclerView();
        setupButtons();
        loadCartItems();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Shopping Cart");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        cartAdapter = new CartAdapter(new CartAdapter.OnCartItemActionListener() {
            @Override
            public void onQuantityChanged(CartItem cartItem) {
                cartRepository.update(cartItem);
                calculateTotals();
            }

            @Override
            public void onRemoveItem(CartItem cartItem) {
                cartRepository.delete(cartItem);
            }
        });

        binding.rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        binding.rvCartItems.setAdapter(cartAdapter);
    }

    private void setupButtons() {
        binding.btnCheckout.setOnClickListener(v -> {
            if (cartItems != null && !cartItems.isEmpty()) {
                Intent intent = new Intent(this, BillingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadCartItems() {
        int userId = sessionManager.getUserId();
        cartRepository.getCartItemsByUserId(userId).observe(this, items -> {
            cartItems = items;
            if (items != null && !items.isEmpty()) {
                cartAdapter.setCartItems(items);
                binding.rvCartItems.setVisibility(View.VISIBLE);
                binding.tvEmptyCart.setVisibility(View.GONE);
                calculateTotals();
            } else {
                binding.rvCartItems.setVisibility(View.GONE);
                binding.tvEmptyCart.setVisibility(View.VISIBLE);
                binding.btnCheckout.setEnabled(false);
            }
        });
    }

    private void calculateTotals() {
        if (cartItems == null || cartItems.isEmpty()) {
            return;
        }

        double subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getSubtotal();
        }

        double total = subtotal + DELIVERY_FEE;

        binding.tvSubtotal.setText(String.format(Locale.US, "$%.2f", subtotal));
        binding.tvDeliveryFee.setText(String.format(Locale.US, "$%.2f", DELIVERY_FEE));
        binding.tvTotal.setText(String.format(Locale.US, "$%.2f", total));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
