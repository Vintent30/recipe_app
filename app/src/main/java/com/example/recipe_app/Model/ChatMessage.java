package com.example.recipe_app.Model;

public class ChatMessage {
    private String senderId;  // Người gửi
    private String receiverId; // Người nhận

    // Constructor mặc định
    public ChatMessage() {}

    // Constructor với tham số
    public ChatMessage(String senderId, String receiverId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    // Getter và Setter cho senderId
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    // Getter và Setter cho receiverId
    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
}
