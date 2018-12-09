package com.example.android.sips;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BeverageTest {

    @Test
    public void createIngredientsString() {
        ArrayList<String> ingredients = new ArrayList<String>();
        ingredients.add("1 oz vodka");
        ingredients.add("1 oz lemon juice");
        Beverage b = new Beverage(null, ingredients, null, null);
        String result = b.createIngredientsString(ingredients);

        assertEquals("- 1 oz vodka\n- 1 oz lemon juice", result);
    }

    @Test
    public void createIngredientsString2() {
        ArrayList<String> ingredients = new ArrayList<String>();
        ingredients.add("1 oz vodka");
        ingredients.add("1 oz tequila");
        ingredients.add("1 oz gin");
        ingredients.add("1 oz rum");
        ingredients.add("splash of coke");
        ingredients.add("1/2 oz lime juice");
        Beverage b = new Beverage(null, ingredients, null, null);
        String result = b.createIngredientsString(ingredients);

        assertEquals("- 1 oz vodka\n- 1 oz tequila\n- 1 oz gin\n- 1 oz rum\n- splash of coke\n- 1/2 oz lime juice", result);
    }

    @Test
    public void createIngredientsString3() {
        ArrayList<String> ingredients = new ArrayList<String>();
        ingredients.add("2 oz rum");
        ingredients.add("2 oz coke");
        ingredients.add("1/2 oz lime juice");
        Beverage b = new Beverage(null, ingredients, null, null);
        String result = b.createIngredientsString(ingredients);

        assertEquals("- 2 oz rum\n- 2 oz coke\n- 1/2 oz lime juice", result);
    }

    @Test
    public void createIngredientsString4() {
        ArrayList<String> ingredients = null;
        Beverage b = new Beverage(null, ingredients, null, null);
        String result = b.createIngredientsString(ingredients);

        assertEquals("", result);
    }

    @Test
    public void createIngredientsString5() {
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("1.5 oz tequila");
        Beverage b = new Beverage(null, ingredients, null, null);
        String result = b.createIngredientsString(ingredients);

        assertEquals("- 1.5 oz tequila", result);
    }
}