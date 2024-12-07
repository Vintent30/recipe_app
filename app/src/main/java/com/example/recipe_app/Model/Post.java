package com.example.recipe_app.Model;

public class Post {
    private String postId;
    private String image;
    private String content;
    private String status;
    private String userId;
    private int like;
    private int comment;

    // Constructor mặc định (bắt buộc cho Firebase)
    public Post() {
    }

    // Constructor đầy đủ tham số
    public Post(String postId, String image, String content, String status, String userId, int like,int comment) {
        this.postId = postId;
        this.image = image;
        this.content = content;
        this.status = status;
        this.userId = userId;
        this.like = like;
        this.comment = comment;
    }

    // Getter và Setter
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }
}
