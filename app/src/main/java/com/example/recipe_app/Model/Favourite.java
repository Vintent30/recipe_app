package com.example.recipe_app.Model;

public class Favourite {
    private String recipeId;  // Recipe ID (used as a primary key)
    private String name;      // Name of the dish
    private String imageUrl;  // Image URL of the dish
    private int calories;     // Calorie count
    private boolean isFavorite; // Favorite status

    // Default constructor (required for Firebase)
    public Favourite() {}

    // Full constructor
    public Favourite(String recipeId, String name, String imageUrl, int calories) {
        this.recipeId = recipeId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.calories = calories;
        this.isFavorite = false; // Default to not favorited
    }

    // Getters and setters
    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getCaloriesText() {
        return calories + " calo";
    }
}
