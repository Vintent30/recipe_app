package com.example.recipe_app.Model;

public class Account {
    private String name; // Tên người dùng
    private String email; // Email người dùng
    private String password;
    private String avatar;
    private String Phone;

    public Account(String name, String email, String password) {
    }

    public Account(String name, String email, String password, String avatar, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        Phone = phone;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
