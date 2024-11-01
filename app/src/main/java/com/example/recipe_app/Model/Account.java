package com.example.recipe_app.Model;

public class Account {
    private String name; // Tên người dùng
    private String email; // Email người dùng

    public Account(String name, String email) {
        this.name = name;
        this.email = email;
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
}
