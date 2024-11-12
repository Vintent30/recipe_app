package com.example.recipe_app.Model;

public class Favourite {

    private String name;
    private String imageUrl;
    private String calories;

    // Constructor
    public Favourite(String name, String imageUrl, String calories) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.calories = calories;
    }

    // Getter và Setter
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

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }
}
