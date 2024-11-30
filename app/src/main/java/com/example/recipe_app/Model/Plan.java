package com.example.recipe_app.Model;

public class Plan {
    private String id;
    private String userId; // ID người dùng
    private String date;
    private String recipeName;
    private String description;

    public Plan() {
        // Constructor mặc định cho Firebase
    }

    public Plan(String id, String userId, String date, String recipeName, String description) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.recipeName = recipeName;
        this.description = description;
    }

    // Getter và Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
