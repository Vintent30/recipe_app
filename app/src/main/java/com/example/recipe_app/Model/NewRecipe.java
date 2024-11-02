package com.example.recipe_app.Model;

public class NewRecipe {
    private String recipeId;
    private String cookName;
    private String category;
    private int calories;
    private String description;
    private String imageUrl; // URL for the image stored in Cloudinary
    private String videoUrl;

    public NewRecipe(String recipeId, String cookName, String category, int calories, String description, String imageUrl, String videoUrl) {
        this.recipeId = recipeId;
        this.cookName = cookName;
        this.category = category;
        this.calories = calories;
        this.description = description;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getCookName() {
        return cookName;
    }

    public void setCookName(String cookName) {
        this.cookName = cookName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
