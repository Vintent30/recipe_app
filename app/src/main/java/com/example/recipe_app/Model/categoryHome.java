package com.example.recipe_app.Model;

import java.util.List;

public class categoryHome {

    private String nameCategory;
    private int imgForward;
    private List<Recipe> foodHomes;

    public categoryHome(String nameCategory, int imgForward, List<Recipe> foodHomes) {
        this.nameCategory = nameCategory;
        this.imgForward = imgForward;
        this.foodHomes = foodHomes;
    }

    public List<Recipe> getFoods() {
        return foodHomes;
    }

    public void setFoods(List<Recipe> foodHomes) {
        this.foodHomes = foodHomes;
    }

    public int getImgForward() {
        return imgForward;
    }

    public void setImgForward(int imgForward) {
        this.imgForward = imgForward;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }
}
