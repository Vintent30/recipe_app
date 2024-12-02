package com.example.recipe_app.Model;

public class ChatList extends ChatMessage {
    private String avatarUrl;
    private String senderName;

    public ChatList() {
        super();
    }

    public ChatList(String senderId, String receiverId, String messageText, long timestamp,
                    String recipeId, String recipeName, String recipeImage, String authorId,
                    String avatarUrl, String senderName) {
        super(senderId, receiverId, messageText, timestamp, recipeId, recipeName, recipeImage, authorId);
        this.avatarUrl = avatarUrl;
        this.senderName = senderName;
    }

    // Getter và Setter
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

}
