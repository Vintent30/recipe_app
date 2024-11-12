package com.example.recipe_app.Model;

public class Category {
    private String name;
    private String image; // Đổi tên thành 'image' để phù hợp với Firebase

    // Constructor rỗng cần thiết cho Firebase
    public Category() {
    }

    public Category(String name, String image) {
        this.name = name;
        this.image = image;
    }

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
}
