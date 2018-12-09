package com.example.android.sips;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Class that models a beverage/cocktail
 */
class Beverage {

    private String name;
    private Bitmap thumbnail;
    private String ingredients;
    private String imageSource;
    private String recipe;

    /**
     * Constructor for the class
     * @param name name of the cocktail
     * @param ingredientsArray ingredients used to make drink
     * @param recipe instructions for making the drink
     * @param imageSource URL for thumbnail of drink
     */
    public Beverage (String name, ArrayList<String> ingredientsArray, String recipe, String imageSource) {
        this.name = name;
        this.imageSource = imageSource;
        this.recipe = recipe;
        this.ingredients = createIngredientsString(ingredientsArray); //to simplify the constructor, added a method to do the string creation

    }

    /**
     * Method to create the ingredient String
     * @param ingredientsArray array from which information will be pulled to make String
     * @return String representation of ingredients
     */
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

    /**
     * Setter for name
     * @param name String name you want to change to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for name
     * @return String name of drink
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for thumbnail
     * @param thumbnail bitmap to change thumbnail to
     */
    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * Getter for thumbnail
     * @return bitmap of thumbnail
     */
    public Bitmap getThumbnail() {
        return thumbnail;
    }

    /**
     * Getter for ingredients
     * @return String of ingredients used to make the drink
     */
    public String getIngredients() {
        return ingredients;
    }

    /**
     * Getter for image source
     * @return String URL of the image's source
     */
    public String getImageSource() {
        return imageSource;
    }

    /**
     * Getter for recipe
     * @return String of recipe used to make drink
     */
    public String getRecipe() {
        return recipe;
    }

    /**
     * Setter for ingredients
     * @param ingredients String of ingredients to change to
     */
    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Setter for image source
     * @param imageSource String URL of new image source
     */
    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    /**
     * Setter for recipe
     * @param recipe String recipe to change to
     */
    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }
}
