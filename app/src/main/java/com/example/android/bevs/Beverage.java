package com.example.android.bevs;

import android.graphics.Bitmap;

import java.util.ArrayList;

class Beverage {

    private String name;
    private Bitmap thumbnail;
    private String ingredients;
    private String imageSource;
    private String recipe;

    public Beverage (String name, ArrayList<String> ingredientsArray, String recipe, String imageSource) {
        this.name = name;
        this.imageSource = imageSource;
        this.recipe = recipe;
        String ingredients = "";

        if (ingredientsArray != null) {
            for (int i = 0; i < ingredientsArray.size(); i++) {
                ingredients = ingredients + "- " + ingredientsArray.get(i) + "\n";
            }
            ingredients = ingredients.substring(0, ingredients.lastIndexOf('\n'));
        }
        this.ingredients = ingredients;
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

    public String getIngredients() {
        return ingredients;
    }

    public String getImageSource() {
        return imageSource;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }
}
