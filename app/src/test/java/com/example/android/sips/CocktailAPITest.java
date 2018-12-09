package com.example.android.sips;

import org.junit.Test;

import static org.junit.Assert.*;

public class CocktailAPITest {

    @Test
    public void buildURL() {
        CocktailAPI c = new CocktailAPI(null);
        String actual = c.buildURL("vodka");
        assertEquals("https://www.thecocktaildb.com/api/json/v1/1/search.php?s=vodka", actual);
    }

    @Test
    public void buildURL2() {
        CocktailAPI c = new CocktailAPI(null);
        String actual = c.buildURL("moscow mule");
        assertEquals("https://www.thecocktaildb.com/api/json/v1/1/search.php?s=moscow_mule", actual);
    }

    @Test
    public void buildURL3() {
        CocktailAPI c = new CocktailAPI(null);
        String actual = c.buildURL("long island iced tea");
        assertEquals("https://www.thecocktaildb.com/api/json/v1/1/search.php?s=long_island_iced_tea", actual);
    }

    @Test
    public void buildURL4() {
        CocktailAPI c = new CocktailAPI(null);
        String actual = c.buildURL(" vodka");
        assertEquals("https://www.thecocktaildb.com/api/json/v1/1/search.php?s=vodka", actual);
    }

    @Test
    public void buildURL5() {
        CocktailAPI c = new CocktailAPI(null);
        String actual = c.buildURL("   vodka");
        assertEquals("https://www.thecocktaildb.com/api/json/v1/1/search.php?s=vodka", actual);
    }
}