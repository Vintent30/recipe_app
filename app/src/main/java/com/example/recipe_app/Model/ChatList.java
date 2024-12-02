package com.example.recipe_app.Model;

public class ChatList extends ChatMessage {
    private String avatarUrl;
    private String senderName;
    private String lastMessage;
    private long lastMessageTimestamp;

    public ChatList() {
        super();
    }

    public ChatList(String senderId, String receiverId, String messageText, long timestamp,
                    String recipeId, String recipeName, String recipeImage, String authorId,
                    String avatarUrl, String senderName, String lastMessage, long lastMessageTimestamp) {
        super(senderId, receiverId, messageText, timestamp, recipeId, recipeName, recipeImage, authorId);
        this.avatarUrl = avatarUrl;
        this.senderName = senderName;
        this.lastMessage = lastMessage;
        this.lastMessageTimestamp = lastMessageTimestamp;
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
    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(long lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }
}
