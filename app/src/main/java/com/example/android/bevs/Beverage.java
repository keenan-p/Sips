package com.example.android.bevs;

import android.graphics.Bitmap;

import java.util.ArrayList;

class Beverage {

    private String name;
    private String imageURL;
    private Bitmap thumbnail;
    private ArrayList<String> ingredients;
    private String imageSource;
    private String recipe;

    public Beverage (String name, ArrayList<String> ingredients, String recipe, String imageSource) {
        this.name = name;
        this.ingredients = ingredients;
        this.imageSource = imageSource;
        this.recipe = recipe;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public String getImageSource() {
        return imageSource;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }
}
