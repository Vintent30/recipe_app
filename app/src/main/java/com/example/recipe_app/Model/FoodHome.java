package com.example.recipe_app.Model;

public class FoodHome {

    private String resourceId;
    private String title;
    private int save;

    public FoodHome(String resourceId, String title, int save) {
        this.resourceId = resourceId;
        this.title = title;
        this.save = save;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSave() {
        return save;
    }

    public void setSave(int save) {
        this.save = save;
    }
}
