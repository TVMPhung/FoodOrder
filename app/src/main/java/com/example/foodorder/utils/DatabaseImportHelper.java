package com.example.foodorder.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.foodorder.model.Food;
import com.example.foodorder.repository.FoodRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for importing food data with images into the database.
 * Supports batch import and handles various image formats (JPEG, PNG, etc.)
 */
public class DatabaseImportHelper {
    private static final String TAG = "DatabaseImportHelper";
    private static final int MAX_IMAGE_SIZE = 500; // Maximum width/height for images
    
    private final Context context;
    private final FoodRepository foodRepository;
    
    public DatabaseImportHelper(Context context, FoodRepository foodRepository) {
        this.context = context;
        this.foodRepository = foodRepository;
    }
    
    /**
     * Import a single food item with image from drawable resource
     * @param name Food name
     * @param description Food description
     * @param price Food price
     * @param drawableResourceId Image resource ID from drawable
     * @param category Food category
     * @param ingredients Ingredients list
     * @return true if import successful, false otherwise
     */
    public boolean importFoodWithImage(String name, String description, double price, 
                                      int drawableResourceId, String category, String ingredients) {
        try {
            // Validate inputs
            if (!validateFoodData(name, description, price, category, ingredients)) {
                Log.e(TAG, "Invalid food data");
                return false;
            }
            
            // Load and compress image
            byte[] imageData = loadImageFromResource(drawableResourceId);
            if (imageData == null) {
                Log.e(TAG, "Failed to load image for: " + name);
                return false;
            }
            
            // Create Food object
            Food food = new Food(name, description, price, "", category, ingredients);
            food.setImageData(imageData);
            
            // Insert into database
            foodRepository.insert(food);
            Log.d(TAG, "Successfully imported food: " + name);
            return true;
            
        } catch (Exception e) {
            Log.e(TAG, "Error importing food: " + name, e);
            return false;
        }
    }
    
    /**
     * Import a single food item with image from input stream
     * @param name Food name
     * @param description Food description
     * @param price Food price
     * @param imageStream Input stream for image data
     * @param category Food category
     * @param ingredients Ingredients list
     * @return true if import successful, false otherwise
     */
    public boolean importFoodWithImageStream(String name, String description, double price,
                                            InputStream imageStream, String category, String ingredients) {
        try {
            // Validate inputs
            if (!validateFoodData(name, description, price, category, ingredients)) {
                Log.e(TAG, "Invalid food data");
                return false;
            }
            
            // Load and compress image
            byte[] imageData = loadImageFromStream(imageStream);
            if (imageData == null) {
                Log.e(TAG, "Failed to load image for: " + name);
                return false;
            }
            
            // Create Food object
            Food food = new Food(name, description, price, "", category, ingredients);
            food.setImageData(imageData);
            
            // Insert into database
            foodRepository.insert(food);
            Log.d(TAG, "Successfully imported food: " + name);
            return true;
            
        } catch (Exception e) {
            Log.e(TAG, "Error importing food: " + name, e);
            return false;
        }
    }
    
    /**
     * Batch import multiple food items
     * @param foodItems List of FoodImportItem objects
     * @return Number of successfully imported items
     */
    public int batchImportFoods(List<FoodImportItem> foodItems) {
        if (foodItems == null || foodItems.isEmpty()) {
            Log.w(TAG, "No food items to import");
            return 0;
        }
        
        int successCount = 0;
        for (FoodImportItem item : foodItems) {
            boolean success = importFoodWithImage(
                item.name, item.description, item.price,
                item.drawableResourceId, item.category, item.ingredients
            );
            if (success) {
                successCount++;
            }
        }
        
        Log.d(TAG, "Batch import completed: " + successCount + "/" + foodItems.size() + " items");
        return successCount;
    }
    
    /**
     * Load image from drawable resource
     */
    private byte[] loadImageFromResource(int resourceId) {
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
            if (bitmap == null) {
                return null;
            }
            return compressImage(bitmap);
        } catch (Exception e) {
            Log.e(TAG, "Error loading image from resource", e);
            return null;
        }
    }
    
    /**
     * Load image from input stream
     */
    private byte[] loadImageFromStream(InputStream inputStream) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null) {
                return null;
            }
            return compressImage(bitmap);
        } catch (Exception e) {
            Log.e(TAG, "Error loading image from stream", e);
            return null;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error closing input stream", e);
            }
        }
    }
    
    /**
     * Compress image to reduce database size
     */
    private byte[] compressImage(Bitmap bitmap) {
        try {
            // Resize if image is too large
            if (bitmap.getWidth() > MAX_IMAGE_SIZE || bitmap.getHeight() > MAX_IMAGE_SIZE) {
                float scale = Math.min(
                    (float) MAX_IMAGE_SIZE / bitmap.getWidth(),
                    (float) MAX_IMAGE_SIZE / bitmap.getHeight()
                );
                int newWidth = Math.round(bitmap.getWidth() * scale);
                int newHeight = Math.round(bitmap.getHeight() * scale);
                bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
            }
            
            // Compress to JPEG
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            Log.e(TAG, "Error compressing image", e);
            return null;
        }
    }
    
    /**
     * Validate food data before import
     */
    private boolean validateFoodData(String name, String description, double price, 
                                     String category, String ingredients) {
        if (name == null || name.trim().isEmpty()) {
            Log.e(TAG, "Food name is required");
            return false;
        }
        if (description == null || description.trim().isEmpty()) {
            Log.e(TAG, "Food description is required");
            return false;
        }
        if (price < 0) {
            Log.e(TAG, "Price must be positive");
            return false;
        }
        if (category == null || category.trim().isEmpty()) {
            Log.e(TAG, "Category is required");
            return false;
        }
        if (ingredients == null || ingredients.trim().isEmpty()) {
            Log.e(TAG, "Ingredients are required");
            return false;
        }
        return true;
    }
    
    /**
     * Data class for batch import
     */
    public static class FoodImportItem {
        public String name;
        public String description;
        public double price;
        public int drawableResourceId;
        public String category;
        public String ingredients;
        
        public FoodImportItem(String name, String description, double price,
                             int drawableResourceId, String category, String ingredients) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.drawableResourceId = drawableResourceId;
            this.category = category;
            this.ingredients = ingredients;
        }
    }
}
