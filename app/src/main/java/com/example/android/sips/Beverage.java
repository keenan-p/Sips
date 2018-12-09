package com.example.android.sips;

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
        this.ingredients = createIngredientsString(ingredientsArray); //to simplify the constructor, added a method to do the string creation

    }

    public String createIngredientsString(ArrayList<String> ingredientsArray){ //decided to transform the input from an ArrayList to String for easier display
        String ingredients = "";
        if (ingredientsArray != null) {
            for (int i = 0; i < ingredientsArray.size(); i++) {
                ingredients = ingredients + "- " + ingredientsArray.get(i) + "\n"; //after this method, [1 oz tequila, 1 oz lime] will look like "- 1 oz tequila
                                                                                                                                                //- 1 oz lime"
            }
            ingredients = ingredients.substring(0, ingredients.lastIndexOf('\n')); //remove the last newline
        }
        return ingredients;
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
