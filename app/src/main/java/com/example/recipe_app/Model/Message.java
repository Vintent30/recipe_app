package com.example.recipe_app.Model;

public class Message {
    private String messageId;
    private String text;
    private long timestamp;
    private String senderId;
    private String recipeId;
    private String recipeName;
    private String recipeImage; // URL hình ảnh công thức

    public Message() { }

    public Message(String messageId, String text, long timestamp, String senderId, String recipeId, String recipeName, String recipeImage) {
        this.messageId = messageId;
        this.text = text;
        this.timestamp = timestamp;
        this.senderId = senderId;
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeImage = recipeImage;
    }

    // Getters and Setters
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }
}
