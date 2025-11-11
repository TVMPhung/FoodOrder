package com.example.foodorder.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "foods")
public class Food {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private byte[] imageData; // Store image as byte array in database
    private String category;
    private String ingredients;
    private boolean isAvailable;
    private double averageRating;
    private int reviewCount;
    
    // Additional fields for chatbot functionality
    private int categoryId;
    private int priceId;
    private int timeId;
    private int timeValue; // Preparation time in minutes
    private int locationId;
    private double star; // Rating from database
    private String imagePath; // URL for food image
    private boolean bestFood; // Flag for best food recommendations

    public Food(String name, String description, double price, String imageUrl, String category, String ingredients) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.ingredients = ingredients;
        this.isAvailable = true;
        this.averageRating = 0.0;
        this.reviewCount = 0;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getPriceId() {
        return priceId;
    }

    public void setPriceId(int priceId) {
        this.priceId = priceId;
    }

    public int getTimeId() {
        return timeId;
    }

    public void setTimeId(int timeId) {
        this.timeId = timeId;
    }

    public int getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(int timeValue) {
        this.timeValue = timeValue;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public double getStar() {
        return star;
    }

    public void setStar(double star) {
        this.star = star;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isBestFood() {
        return bestFood;
    }

    public void setBestFood(boolean bestFood) {
        this.bestFood = bestFood;
    }
}
