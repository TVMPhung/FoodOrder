package com.example.foodorder.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorder.databinding.ActivitySignupBinding;
import com.example.foodorder.model.User;
import com.example.foodorder.repository.UserRepository;
import com.example.foodorder.utils.ValidationUtils;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRepository = new UserRepository(getApplication());

        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.btnSignUp.setOnClickListener(v -> attemptSignUp());
        
        binding.tvLogin.setOnClickListener(v -> {
            finish();
        });
    }

    private void attemptSignUp() {
        String username = binding.etUsername.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String phoneNumber = binding.etPhoneNumber.getText().toString().trim();
        String address = binding.etAddress.getText().toString().trim();

        // Validate inputs
        if (!ValidationUtils.isValidUsername(username)) {
            binding.etUsername.setError("Username must be at least 3 characters");
            binding.etUsername.requestFocus();
            return;
        }

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

        if (!ValidationUtils.isValidPhoneNumber(phoneNumber)) {
            binding.etPhoneNumber.setError("Invalid phone number");
            binding.etPhoneNumber.requestFocus();
            return;
        }

        if (!ValidationUtils.isValidAddress(address)) {
            binding.etAddress.setError("Address is required");
            binding.etAddress.requestFocus();
            return;
        }

        // Check if email already exists
        binding.btnSignUp.setEnabled(false);
        userRepository.getUserByEmail(email, existingUser -> {
            if (existingUser != null) {
                runOnUiThread(() -> {
                    binding.btnSignUp.setEnabled(true);
                    Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show();
                });
                return;
            }

            // Check if username already exists
            userRepository.getUserByUsername(username, existingUsernameUser -> {
                if (existingUsernameUser != null) {
                    runOnUiThread(() -> {
                        binding.btnSignUp.setEnabled(true);
                        Toast.makeText(this, "Username already taken", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }

                // Create new user
                User newUser = new User(username, email, password, phoneNumber, address);
                userRepository.insert(newUser, userId -> {
                    runOnUiThread(() -> {
                        binding.btnSignUp.setEnabled(true);
                        if (userId > 0) {
                            Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
