package com.example.recipe_app.Model;

public class User {
    private String name;
    private int avatarResourceId;
    private String email;
    public User(String name, int avatarResourceId) {
        this.name = name;
        this.avatarResourceId = avatarResourceId;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvatarResourceId() {
        return avatarResourceId;
    }

    public void setAvatarResourceId(int avatarResourceId) {
        this.avatarResourceId = avatarResourceId;
    }
}