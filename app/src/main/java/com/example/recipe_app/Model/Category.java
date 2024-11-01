package com.example.recipe_app.Model;

public class Category {
    private String title;
    private int imageResId;

    public Category(String title, int imageResId) {
        this.title = title;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResId() {
        return imageResId;
    }
}
