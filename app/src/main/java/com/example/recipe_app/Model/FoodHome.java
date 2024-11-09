package com.example.recipe_app.Model;

public class FoodHome {

    private String resourceId;
    private String title;
    private String save;

    public FoodHome(String resourceId, String title, String save) {
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

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }
}
