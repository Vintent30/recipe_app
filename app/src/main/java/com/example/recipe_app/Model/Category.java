package com.example.recipe_app.Model;

public class Category {
    private String id;  // Add the id field to store category ID
    private String name;
    private String image;

    // Default constructor needed for Firebase
    public Category() {
    }

    // Constructor with id, name, and image
    public Category(String id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    // Getter and Setter for id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for image
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
