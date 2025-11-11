package com.example.foodorder.ui.billing;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorder.databinding.ActivityBillingBinding;
import com.example.foodorder.model.CartItem;
import com.example.foodorder.model.Order;
import com.example.foodorder.repository.CartRepository;
import com.example.foodorder.repository.OrderRepository;
import com.example.foodorder.repository.UserRepository;
import com.example.foodorder.ui.food.FoodListActivity;
import com.example.foodorder.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class BillingActivity extends AppCompatActivity {
    private ActivityBillingBinding binding;
    private CartRepository cartRepository;
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private SessionManager sessionManager;
    private List<CartItem> cartItems;
    private static final double DELIVERY_FEE = 2.99;
    private static final double TAX_RATE = 0.08; // 8% tax

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBillingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cartRepository = new CartRepository(getApplication());
        orderRepository = new OrderRepository(getApplication());
        userRepository = new UserRepository(getApplication());
        sessionManager = new SessionManager(this);

        setupToolbar();
        loadUserAddress();
        loadCartItems();
        setupButtons();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Checkout");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void loadUserAddress() {
        int userId = sessionManager.getUserId();
        userRepository.getLoggedInUser(user -> {
            runOnUiThread(() -> {
                if (user != null) {
                    binding.etAddress.setText(user.getAddress());
                }
            });
        });
    }

    private void loadCartItems() {
        int userId = sessionManager.getUserId();
        cartRepository.getCartItemsByUserId(userId).observe(this, items -> {
            if (items != null && !items.isEmpty()) {
                cartItems = items;
                displayOrderSummary(items);
                calculateTotals(items);
            }
        });
    }

    private void displayOrderSummary(List<CartItem> items) {
        StringBuilder summary = new StringBuilder();
        for (CartItem item : items) {
            summary.append(item.getFoodName())
                    .append(" x")
                    .append(item.getQuantity())
                    .append(" - $")
                    .append(String.format(Locale.US, "%.2f", item.getSubtotal()))
                    .append("\n");
        }
        binding.tvOrderItems.setText(summary.toString());
    }

    private void calculateTotals(List<CartItem> items) {
        double subtotal = 0;
        for (CartItem item : items) {
            subtotal += item.getSubtotal();
        }

        double tax = subtotal * TAX_RATE;
        double total = subtotal + DELIVERY_FEE + tax;

        binding.tvSubtotal.setText(String.format(Locale.US, "$%.2f", subtotal));
        binding.tvDeliveryFee.setText(String.format(Locale.US, "$%.2f", DELIVERY_FEE));
        binding.tvTax.setText(String.format(Locale.US, "$%.2f", tax));
        binding.tvTotal.setText(String.format(Locale.US, "$%.2f", total));
    }

    private void setupButtons() {
        binding.btnPlaceOrder.setOnClickListener(v -> placeOrder());
    }

    private void placeOrder() {
        String address = binding.etAddress.getText().toString().trim();
        if (address.isEmpty()) {
            binding.etAddress.setError("Address is required");
            binding.etAddress.requestFocus();
            return;
        }

        String paymentMethod;
        if (binding.rbCashOnDelivery.isChecked()) {
            paymentMethod = "Cash on Delivery";
        } else {
            paymentMethod = "Online Payment";
        }

        // Convert cart items to JSON string
        String orderItemsJson = convertCartItemsToJson(cartItems);

        // Calculate total
        double subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getSubtotal();
        }
        double tax = subtotal * TAX_RATE;
        double total = subtotal + DELIVERY_FEE + tax;

        // Create order
        int userId = sessionManager.getUserId();
        Order order = new Order(userId, orderItemsJson, total, address, paymentMethod);

        binding.btnPlaceOrder.setEnabled(false);
        orderRepository.insert(order, orderId -> {
            // Clear cart after order is placed
            cartRepository.clearCart(userId);

            runOnUiThread(() -> {
                binding.btnPlaceOrder.setEnabled(true);
                showOrderConfirmation(orderId);
            });
        });
    }

    private String convertCartItemsToJson(List<CartItem> items) {
        try {
            JSONArray jsonArray = new JSONArray();
            for (CartItem item : items) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("foodId", item.getFoodId());
                jsonObject.put("foodName", item.getFoodName());
                jsonObject.put("price", item.getFoodPrice());
                jsonObject.put("quantity", item.getQuantity());
                jsonArray.put(jsonObject);
            }
            return jsonArray.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }

    private void showOrderConfirmation(long orderId) {
        new AlertDialog.Builder(this)
                .setTitle("Order Placed")
                .setMessage("Your order #" + orderId + " has been placed successfully!")
                .setPositiveButton("OK", (dialog, which) -> {
                    Intent intent = new Intent(this, FoodListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
