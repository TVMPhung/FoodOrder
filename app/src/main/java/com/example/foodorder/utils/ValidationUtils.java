package com.example.foodorder.utils;

import android.text.TextUtils;
import android.util.Patterns;

public class ValidationUtils {

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        // Password must be at least 6 characters
        return !TextUtils.isEmpty(password) && password.length() >= 6;
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        // Basic phone validation - at least 10 digits
        return !TextUtils.isEmpty(phoneNumber) && phoneNumber.replaceAll("[^0-9]", "").length() >= 10;
    }

    public static boolean isValidUsername(String username) {
        // Username must be at least 3 characters
        return !TextUtils.isEmpty(username) && username.length() >= 3;
    }

    public static boolean isValidAddress(String address) {
        // Address must not be empty
        return !TextUtils.isEmpty(address) && address.trim().length() > 0;
    }

    public static String getPasswordStrength(String password) {
        if (TextUtils.isEmpty(password)) {
            return "Empty";
        }
        
        if (password.length() < 6) {
            return "Weak";
        } else if (password.length() < 8) {
            return "Medium";
        } else {
            boolean hasUpperCase = !password.equals(password.toLowerCase());
            boolean hasLowerCase = !password.equals(password.toUpperCase());
            boolean hasDigit = password.matches(".*\\d.*");
            boolean hasSpecialChar = !password.matches("[A-Za-z0-9 ]*");
            
            int strength = 0;
            if (hasUpperCase) strength++;
            if (hasLowerCase) strength++;
            if (hasDigit) strength++;
            if (hasSpecialChar) strength++;
            
            if (strength >= 3) {
                return "Strong";
            } else {
                return "Medium";
            }
        }
    }
}
