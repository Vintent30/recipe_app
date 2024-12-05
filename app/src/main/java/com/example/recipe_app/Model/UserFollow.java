package com.example.recipe_app.Model;

public class UserFollow {
    private String userId;
    private String userName;
    private String avatarUrl;

    public UserFollow(String userId, String userName, String avatarUrl) {
        this.userId = userId;
        this.userName = userName;
        this.avatarUrl = avatarUrl;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}