package com.example.foodorder.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorder.databinding.ActivityLoginBinding;
import com.example.foodorder.repository.UserRepository;
import com.example.foodorder.ui.food.FoodListActivity;
import com.example.foodorder.utils.SessionManager;
import com.example.foodorder.utils.ValidationUtils;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private UserRepository userRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRepository = new UserRepository(getApplication());
        sessionManager = new SessionManager(this);

        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            navigateToFoodList();
            return;
        }

        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.btnLogin.setOnClickListener(v -> attemptLogin());
        
        binding.tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void attemptLogin() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        // Validate inputs
        if (!ValidationUtils.isValidEmail(email)) {
            binding.etEmail.setError("Invalid email address");
            binding.etEmail.requestFocus();
            return;
        }

        if (!ValidationUtils.isValidPassword(password)) {
            binding.etPassword.setError("Password must be at least 6 characters");
            binding.etPassword.requestFocus();
            return;
        }

        // Attempt login
        binding.btnLogin.setEnabled(false);
        userRepository.login(email, password, user -> {
            runOnUiThread(() -> {
                binding.btnLogin.setEnabled(true);
                if (user != null) {
                    // Update user login status
                    user.setLoggedIn(true);
                    userRepository.update(user);
                    
                    // Create session
                    sessionManager.createLoginSession(user.getId(), user.getUsername(), user.getEmail());
                    
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                    navigateToFoodList();
                } else {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void navigateToFoodList() {
        Intent intent = new Intent(this, FoodListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
