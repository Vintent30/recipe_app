package com.example.recipe_app.Model;

import java.util.HashMap;
import java.util.Map;

public class Account {
    private String name; // Tên người dùng
    private String email; // Email người dùng
    private String password;
    private String phone;
    private String avatar;
    private Map<String, Boolean> followers;
    private Map<String, Boolean> following;

    public Account(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.followers = new HashMap<>();
        this.following = new HashMap<>();
    }

    public Account(String name, String email, String password, String phone, String avatar) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.avatar = avatar;
        this.followers = new HashMap<>();
        this.following = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Map<String, Boolean> getFollowers() {
        return followers;
    }

    public void setFollowers(Map<String, Boolean> followers) {
        this.followers = followers;
    }

    public Map<String, Boolean> getFollowing() {
        return following;
    }

    public void setFollowing(Map<String, Boolean> following) {
        this.following = following;
    }
}
