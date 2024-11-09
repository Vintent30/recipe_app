package com.example.recipe_app.Model;

public class Item {
    private int imageResId;
    private String title;
    private String subtitle;
    private int likeIconResId;

    public Item(int imageResId, String title, String subtitle, int likeIconResId) {
        this.imageResId = imageResId;
        this.title = title;
        this.subtitle = subtitle;
        this.likeIconResId = likeIconResId;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public int getLikeIconResId() {
        return likeIconResId;
    }
}
