package com.example.recipe_app.Model;

public class Detail {
    private String name;
    private String image; // Đây là trường chứa URL ảnh
    private int calories;

    // Constructor rỗng cho Firebase
    public Detail() {}

    // Constructor với tất cả thông tin
    public Detail(String name, String image, int calories) {
        this.name = name;
        this.image = image;
        this.calories = calories;
    }

    // Getter và setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }
}
