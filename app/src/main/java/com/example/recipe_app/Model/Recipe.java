package com.example.recipe_app.Model;

public class Recipe {
    private String recipeId;
    private String userId;
    private String name;
    private String calories;
    private String description;
    private String category;
    private String imageUrl;

    // No-argument constructor required for Firebase
    public Recipe() {}

    // Constructor with parameters
    public Recipe(String recipeId, String userId, String name, String calories, String description, String category, String imageUrl) {
        this.recipeId = recipeId;
        this.userId = userId;
        this.name = name;
        this.calories = calories;
        this.description = description;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
