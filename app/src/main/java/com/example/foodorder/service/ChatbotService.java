package com.example.foodorder.service;

import android.content.Context;
import android.util.Log;

import com.example.foodorder.model.Category;
import com.example.foodorder.model.Food;
import com.example.foodorder.model.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Service class for handling intelligent chatbot queries
 * Loads and processes food database for natural language understanding
 */
public class ChatbotService {
    private static final String TAG = "ChatbotService";
    private static ChatbotService instance;
    
    private List<Food> foods;
    private List<Category> categories;
    private List<Location> locations;
    private Context context;
    
    private ChatbotService(Context context) {
        this.context = context.getApplicationContext();
        loadDatabase();
    }
    
    public static synchronized ChatbotService getInstance(Context context) {
        if (instance == null) {
            instance = new ChatbotService(context);
        }
        return instance;
    }
    
    /**
     * Load database.json from assets
     */
    private void loadDatabase() {
        foods = new ArrayList<>();
        categories = new ArrayList<>();
        locations = new ArrayList<>();
        
        try {
            String jsonString = loadJSONFromAsset("database.json");
            JSONObject jsonObject = new JSONObject(jsonString);
            
            // Load categories
            JSONArray categoriesArray = jsonObject.getJSONArray("Categories");
            for (int i = 0; i < categoriesArray.length(); i++) {
                JSONObject catObj = categoriesArray.getJSONObject(i);
                categories.add(new Category(
                    catObj.getInt("Id"),
                    catObj.getString("Name")
                ));
            }
            
            // Load locations
            JSONArray locationsArray = jsonObject.getJSONArray("Locations");
            for (int i = 0; i < locationsArray.length(); i++) {
                JSONObject locObj = locationsArray.getJSONObject(i);
                locations.add(new Location(
                    locObj.getInt("Id"),
                    locObj.getString("Name"),
                    locObj.getString("Address"),
                    locObj.getString("Phone"),
                    locObj.getString("Hours")
                ));
            }
            
            // Load foods
            JSONArray foodsArray = jsonObject.getJSONArray("Foods");
            for (int i = 0; i < foodsArray.length(); i++) {
                JSONObject foodObj = foodsArray.getJSONObject(i);
                Food food = new Food(
                    foodObj.getString("Name"),
                    foodObj.getString("Description"),
                    foodObj.getDouble("Price"),
                    foodObj.optString("ImagePath", ""),
                    getCategoryNameById(foodObj.getInt("CategoryId")),
                    foodObj.getString("Ingredients")
                );
                food.setId(foodObj.getInt("Id"));
                food.setCategoryId(foodObj.getInt("CategoryId"));
                food.setPriceId(foodObj.getInt("PriceId"));
                food.setTimeId(foodObj.getInt("TimeId"));
                food.setTimeValue(foodObj.getInt("TimeValue"));
                food.setLocationId(foodObj.getInt("LocationId"));
                food.setStar(foodObj.getDouble("Star"));
                food.setImagePath(foodObj.getString("ImagePath"));
                food.setBestFood(foodObj.getBoolean("BestFood"));
                food.setAvailable(foodObj.getBoolean("IsAvailable"));
                food.setAverageRating(foodObj.getDouble("Star"));
                
                foods.add(food);
            }
            
            Log.d(TAG, "Database loaded: " + foods.size() + " foods, " + 
                  categories.size() + " categories, " + locations.size() + " locations");
            
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing database JSON", e);
        }
    }
    
    private String loadJSONFromAsset(String fileName) {
        StringBuilder json = new StringBuilder();
        try {
            InputStream is = context.getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            reader.close();
        } catch (IOException e) {
            Log.e(TAG, "Error reading JSON file", e);
        }
        return json.toString();
    }
    
    /**
     * Process user query and generate intelligent response
     */
    public String processQuery(String query) {
        String lowerQuery = query.toLowerCase().trim();
        
        // Greeting
        if (isGreeting(lowerQuery)) {
            return "Hello! Welcome to our restaurant! 🍕 I'm here to help you find delicious food. " +
                   "You can ask me about our menu, fast food options, healthy choices, locations, or anything else!";
        }
        
        // Menu/Browse
        if (lowerQuery.contains("menu") || lowerQuery.contains("browse") || 
            lowerQuery.contains("what do you have") || lowerQuery.contains("what's available")) {
            return generateMenuResponse();
        }
        
        // Fast food
        if (lowerQuery.contains("fast") || lowerQuery.contains("quick") || 
            lowerQuery.contains("in a hurry") || lowerQuery.contains("fast food")) {
            return generateFastFoodResponse();
        }
        
        // Healthy food
        if (lowerQuery.contains("healthy") || lowerQuery.contains("salad") || 
            lowerQuery.contains("vegetarian") || lowerQuery.contains("veggie") ||
            lowerQuery.contains("light")) {
            return generateHealthyFoodResponse();
        }
        
        // Location info
        if (lowerQuery.contains("location") || lowerQuery.contains("address") || 
            lowerQuery.contains("where") || lowerQuery.contains("branch")) {
            return generateLocationResponse();
        }
        
        // Best food / Recommendations
        if (lowerQuery.contains("recommend") || lowerQuery.contains("best") || 
            lowerQuery.contains("popular") || lowerQuery.contains("top rated")) {
            return generateBestFoodResponse();
        }
        
        // Price range queries
        if (lowerQuery.contains("cheap") || lowerQuery.contains("affordable") || 
            lowerQuery.contains("budget") || lowerQuery.contains("under")) {
            return generateAffordableResponse();
        }
        
        if (lowerQuery.contains("expensive") || lowerQuery.contains("premium") || 
            lowerQuery.contains("high-end")) {
            return generatePremiumResponse();
        }
        
        // Category specific
        for (Category category : categories) {
            if (lowerQuery.contains(category.getName().toLowerCase())) {
                return generateCategoryResponse(category);
            }
        }
        
        // Search by food name
        for (Food food : foods) {
            if (lowerQuery.contains(food.getName().toLowerCase())) {
                return generateFoodDetailResponse(food);
            }
        }
        
        // Help/How can you help
        if (lowerQuery.contains("help") || lowerQuery.contains("what can you do")) {
            return generateHelpResponse();
        }
        
        // Default response
        return "I can help you with:\n" +
               "• Browsing our menu\n" +
               "• Finding fast food (ready in 10 mins or less)\n" +
               "• Recommending healthy options\n" +
               "• Sharing location information\n" +
               "• Showing our best dishes\n" +
               "• Filtering by category (Pizza, Burger, Chicken, Sushi, etc.)\n\n" +
               "What would you like to know?";
    }
    
    private boolean isGreeting(String query) {
        return query.matches(".*(hello|hi|hey|greetings|good morning|good afternoon|good evening).*");
    }
    
    private String generateMenuResponse() {
        StringBuilder response = new StringBuilder();
        response.append("🍽️ Here's our complete menu organized by category:\n\n");
        
        for (Category category : categories) {
            List<Food> categoryFoods = getFoodsByCategory(category.getId());
            if (!categoryFoods.isEmpty()) {
                response.append("📌 ").append(category.getName()).append(":\n");
                for (Food food : categoryFoods) {
                    response.append("  • ").append(food.getName())
                           .append(" - $").append(String.format("%.2f", food.getPrice()))
                           .append(" ⭐").append(food.getStar())
                           .append("\n");
                }
                response.append("\n");
            }
        }
        
        response.append("Ask me about any item for more details!");
        return response.toString();
    }
    
    private String generateFastFoodResponse() {
        List<Food> fastFoods = getFastFoods();
        StringBuilder response = new StringBuilder();
        response.append("⚡ Fast Food Options (Ready in 10 minutes or less):\n\n");
        
        for (Food food : fastFoods) {
            response.append("🍴 ").append(food.getName()).append("\n");
            response.append("   ⏱️ ").append(food.getTimeValue()).append(" minutes\n");
            response.append("   💰 $").append(String.format("%.2f", food.getPrice())).append("\n");
            response.append("   ⭐ ").append(food.getStar()).append("/5\n");
            response.append("   📍 ").append(getLocationNameById(food.getLocationId())).append("\n\n");
        }
        
        if (fastFoods.isEmpty()) {
            response.append("Sorry, no fast food options are currently available.");
        }
        
        return response.toString();
    }
    
    private String generateHealthyFoodResponse() {
        List<Food> healthyFoods = getHealthyFoods();
        StringBuilder response = new StringBuilder();
        response.append("🥗 Healthy Food Options:\n\n");
        
        for (Food food : healthyFoods) {
            response.append("🍃 ").append(food.getName()).append("\n");
            response.append("   ").append(food.getDescription()).append("\n");
            response.append("   💰 $").append(String.format("%.2f", food.getPrice())).append("\n");
            response.append("   ⭐ ").append(food.getStar()).append("/5\n");
            response.append("   📍 ").append(getLocationNameById(food.getLocationId())).append("\n\n");
        }
        
        if (healthyFoods.isEmpty()) {
            response.append("Sorry, no healthy options are currently available.");
        } else {
            response.append("All these options are nutritious and delicious! 🌱");
        }
        
        return response.toString();
    }
    
    private String generateLocationResponse() {
        StringBuilder response = new StringBuilder();
        response.append("📍 Our Restaurant Locations:\n\n");
        
        for (Location location : locations) {
            response.append("🏪 ").append(location.getName()).append("\n");
            response.append("   📮 ").append(location.getAddress()).append("\n");
            response.append("   📞 ").append(location.getPhone()).append("\n");
            response.append("   🕒 ").append(location.getHours()).append("\n\n");
        }
        
        response.append("We're happy to serve you at both locations! 😊");
        return response.toString();
    }
    
    private String generateBestFoodResponse() {
        List<Food> bestFoods = getBestFoods();
        StringBuilder response = new StringBuilder();
        response.append("⭐ Our Best & Most Popular Dishes:\n\n");
        
        for (Food food : bestFoods) {
            response.append("👑 ").append(food.getName()).append("\n");
            response.append("   ").append(food.getDescription()).append("\n");
            response.append("   💰 $").append(String.format("%.2f", food.getPrice())).append("\n");
            response.append("   ⭐ ").append(food.getStar()).append("/5 - Highly Rated!\n");
            response.append("   ⏱️ Ready in ").append(food.getTimeValue()).append(" minutes\n");
            response.append("   📍 ").append(getLocationNameById(food.getLocationId())).append("\n\n");
        }
        
        return response.toString();
    }
    
    private String generateAffordableResponse() {
        List<Food> affordableFoods = getAffordableFoods();
        StringBuilder response = new StringBuilder();
        response.append("💵 Budget-Friendly Options (Under $10):\n\n");
        
        for (Food food : affordableFoods) {
            response.append("🍽️ ").append(food.getName()).append("\n");
            response.append("   💰 Only $").append(String.format("%.2f", food.getPrice())).append("!\n");
            response.append("   ⭐ ").append(food.getStar()).append("/5\n");
            response.append("   📍 ").append(getLocationNameById(food.getLocationId())).append("\n\n");
        }
        
        return response.toString();
    }
    
    private String generatePremiumResponse() {
        List<Food> premiumFoods = getPremiumFoods();
        StringBuilder response = new StringBuilder();
        response.append("💎 Premium Dining Options:\n\n");
        
        for (Food food : premiumFoods) {
            response.append("👨‍🍳 ").append(food.getName()).append("\n");
            response.append("   ").append(food.getDescription()).append("\n");
            response.append("   💰 $").append(String.format("%.2f", food.getPrice())).append("\n");
            response.append("   ⭐ ").append(food.getStar()).append("/5\n");
            response.append("   📍 ").append(getLocationNameById(food.getLocationId())).append("\n\n");
        }
        
        return response.toString();
    }
    
    private String generateCategoryResponse(Category category) {
        List<Food> categoryFoods = getFoodsByCategory(category.getId());
        StringBuilder response = new StringBuilder();
        response.append("🍴 ").append(category.getName()).append(" Menu:\n\n");
        
        for (Food food : categoryFoods) {
            response.append("• ").append(food.getName()).append("\n");
            response.append("  ").append(food.getDescription()).append("\n");
            response.append("  💰 $").append(String.format("%.2f", food.getPrice()))
                   .append(" | ⭐ ").append(food.getStar())
                   .append(" | ⏱️ ").append(food.getTimeValue()).append(" min\n\n");
        }
        
        return response.toString();
    }
    
    private String generateFoodDetailResponse(Food food) {
        StringBuilder response = new StringBuilder();
        response.append("🍽️ ").append(food.getName()).append("\n\n");
        response.append("📝 ").append(food.getDescription()).append("\n\n");
        response.append("💰 Price: $").append(String.format("%.2f", food.getPrice())).append("\n");
        response.append("⭐ Rating: ").append(food.getStar()).append("/5\n");
        response.append("⏱️ Preparation Time: ").append(food.getTimeValue()).append(" minutes\n");
        response.append("📍 Location: ").append(getLocationNameById(food.getLocationId())).append("\n");
        response.append("🥘 Ingredients: ").append(food.getIngredients()).append("\n");
        
        if (food.isBestFood()) {
            response.append("\n⭐ This is one of our BEST dishes! Highly recommended! ⭐");
        }
        
        return response.toString();
    }
    
    private String generateHelpResponse() {
        return "👋 I'm your food ordering assistant! I can help you with:\n\n" +
               "📋 Menu Browsing:\n" +
               "  • Show complete menu\n" +
               "  • Browse by category (Pizza, Burger, Chicken, Sushi, etc.)\n\n" +
               "⚡ Fast Food Options:\n" +
               "  • Items ready in 10 minutes or less\n\n" +
               "🥗 Healthy Choices:\n" +
               "  • Salads, veggie options, grilled items\n\n" +
               "📍 Location Information:\n" +
               "  • Our two locations (LA & NY)\n" +
               "  • Addresses and operating hours\n\n" +
               "⭐ Recommendations:\n" +
               "  • Best dishes and popular items\n" +
               "  • Highly rated foods\n\n" +
               "💰 Price Filtering:\n" +
               "  • Budget-friendly options\n" +
               "  • Premium dining\n\n" +
               "Just ask me anything! 😊";
    }
    
    // Helper methods to filter foods
    
    private List<Food> getFoodsByCategory(int categoryId) {
        List<Food> result = new ArrayList<>();
        for (Food food : foods) {
            if (food.getCategoryId() == categoryId && food.isAvailable()) {
                result.add(food);
            }
        }
        return result;
    }
    
    private List<Food> getFastFoods() {
        List<Food> result = new ArrayList<>();
        for (Food food : foods) {
            if (food.getTimeValue() <= 10 && food.isAvailable()) {
                result.add(food);
            }
        }
        return result;
    }
    
    private List<Food> getHealthyFoods() {
        List<Food> result = new ArrayList<>();
        String[] healthyKeywords = {"salad", "quinoa", "veggie", "vegetarian", 
                                     "grilled chicken", "smoothie", "fresh"};
        
        for (Food food : foods) {
            String nameLower = food.getName().toLowerCase();
            for (String keyword : healthyKeywords) {
                if (nameLower.contains(keyword) && food.isAvailable()) {
                    result.add(food);
                    break;
                }
            }
        }
        return result;
    }
    
    private List<Food> getBestFoods() {
        List<Food> result = new ArrayList<>();
        for (Food food : foods) {
            if (food.isBestFood() && food.isAvailable()) {
                result.add(food);
            }
        }
        return result;
    }
    
    private List<Food> getAffordableFoods() {
        List<Food> result = new ArrayList<>();
        for (Food food : foods) {
            if (food.getPrice() < 10 && food.isAvailable()) {
                result.add(food);
            }
        }
        return result;
    }
    
    private List<Food> getPremiumFoods() {
        List<Food> result = new ArrayList<>();
        for (Food food : foods) {
            if (food.getPrice() >= 30 && food.isAvailable()) {
                result.add(food);
            }
        }
        return result;
    }
    
    private String getCategoryNameById(int id) {
        for (Category category : categories) {
            if (category.getId() == id) {
                return category.getName();
            }
        }
        return "Other";
    }
    
    private String getLocationNameById(int id) {
        for (Location location : locations) {
            if (location.getId() == id) {
                return location.getName();
            }
        }
        return "Unknown Location";
    }
    
    public List<Food> getAllFoods() {
        return new ArrayList<>(foods);
    }
    
    public List<Category> getAllCategories() {
        return new ArrayList<>(categories);
    }
    
    public List<Location> getAllLocations() {
        return new ArrayList<>(locations);
    }
}
