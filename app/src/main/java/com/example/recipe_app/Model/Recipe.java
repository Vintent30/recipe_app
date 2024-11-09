package com.example.recipe_app.Model;

public class Recipe {
    private String recipeId;
    private String name;
    private String calories;
    private String description;
    private String category;
    private String image;
    private String video;
    private String status;
    private String userId;       // Thêm userId
    private String categoryId;
    private int like;

    public Recipe(String recipeId, String string, String s, String string1, String s1, String imageUrl, String videoUrl, String active, String userId, String selectedCategoryId) {

    }

    public Recipe(String recipeId, String name, String calories, String description, String category, String image, String video, String status, String userId, String categoryId, int like) {
        this.recipeId = recipeId;
        this.name = name;
        this.calories = calories;
        this.description = description;
        this.category = category;
        this.image = image;
        this.video = video;
        this.status = status;
        this.userId = userId;
        this.categoryId = categoryId;
        this.like = like;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
