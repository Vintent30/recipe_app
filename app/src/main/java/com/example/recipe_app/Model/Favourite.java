package com.example.recipe_app.Model;

public class Favourite {
    private String recipeId;  // ID của công thức (làm khóa chính)
    private String name;      // Tên món ăn
    private String imageUrl;  // URL hình ảnh món ăn
    private int calories;     // Lượng calo
    private boolean isFavorite; // Trạng thái yêu thích

    // Constructor không tham số (bắt buộc cho Firebase)
    public Favourite() {}

    // Constructor đầy đủ
    public Favourite(String recipeId, String name, String imageUrl, int calories) {
        this.recipeId = recipeId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.calories = calories;
        this.isFavorite = false; // Mặc định không nằm trong danh sách yêu thích
    }

    // Getters và Setters
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
