package com.example.recipe_app.Model;

public class User {
    private String nickname;          // Biệt danh
    private String email;             // Email
    private String phone;             // Số điện thoại
    private String password;          // Mật khẩu
    private String profilePictureUrl; // URL for profile picture, if stored online

    // Constructor
    public User(String nickname, String email, String phone, String password, String profilePictureUrl) {
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.profilePictureUrl = profilePictureUrl;
    }

    // Default constructor
    public User() {}

    // Getters and Setters
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    // Optional: Method to display user information
    public void displayUserInfo() {
        System.out.println("Nickname: " + nickname);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phone);
        System.out.println("Password: " + password);
        System.out.println("Profile Picture URL: " + profilePictureUrl);
    }
}
