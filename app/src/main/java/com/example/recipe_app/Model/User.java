package com.example.recipe_app.Model;

public class User {
    private String name;
    private int avatarResourceId;
    private String email;
    public User(String name, int avatarResourceId) {
        this.name = name;
        this.avatarResourceId = avatarResourceId;

    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
