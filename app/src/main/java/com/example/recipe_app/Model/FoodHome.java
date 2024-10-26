package com.example.recipe_app.Model;

public class FoodHome {

    private int resourceId;
    private String title;
    private String save;

    public FoodHome(int resourceId, String title, String save) {
        this.resourceId = resourceId;
        this.title = title;
        this.save = save;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }
}
