package com.example.recipe_app.Model;

public class CommentMd {
    private String postId;
    private String commentId;
    private String commenterId;
    private String commentText;
    private long timestamp;
    private int likecmt;

    public CommentMd() {

    }

    public CommentMd(String postId, String commentId, String commenterId, String commentText, long timestamp, int likecmt) {
        this.postId = postId;
        this.commentId = commentId;
        this.commenterId = commenterId;
        this.commentText = commentText;
        this.timestamp = timestamp;
        this.likecmt = likecmt;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommenterId() {
        return commenterId;
    }

    public void setCommenterId(String commenterId) {
        this.commenterId = commenterId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getLikecmt() {
        return likecmt;
    }

    public void setLikecmt(int likecmt) {
        this.likecmt = likecmt;
    }
}