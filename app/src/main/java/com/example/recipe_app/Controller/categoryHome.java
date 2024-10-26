package com.example.recipe_app.Controller;

import android.widget.ImageView;

import java.util.List;

public class categoryHome {

    private String nameCategory;
    private int imgForward;
    private List<FoodHome> foodHomes;

    public categoryHome(String nameCategory, int imgForward, List<FoodHome> foodHomes) {
        this.nameCategory = nameCategory;
        this.imgForward = imgForward;
        this.foodHomes = foodHomes;
    }

    public List<FoodHome> getFoods() {
        return foodHomes;
    }

    public void setFoods(List<FoodHome> foodHomes) {
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
