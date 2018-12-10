package com.example.android.sips;

import org.junit.Test;

import static org.junit.Assert.*;

public class CocktailAPITest {

    @Test
    public void buildURL() {
        CocktailAPI c = new CocktailAPI(null);                                                     //this test checks to make sure that the query URL is properly formatted
        String actual = c.buildURL("vodka");                                                       //adding a single term should result in a proper URL
        assertEquals("https://www.thecocktaildb.com/api/json/v1/1/search.php?s=vodka", actual);
    }

    @Test
    public void buildURL2() {
        CocktailAPI c = new CocktailAPI(null);
        String actual = c.buildURL("moscow mule");                                                      //search terms with a space need an _ to be searched properly
        assertEquals("https://www.thecocktaildb.com/api/json/v1/1/search.php?s=moscow_mule", actual); //otherwise, the query will be for just "moscow", which isn't technicall what we want
    }

    @Test
    public void buildURL3() {
        CocktailAPI c = new CocktailAPI(null);
        String actual = c.buildURL("long island iced tea");                                               //testing to see if it works for many terms
        assertEquals("https://www.thecocktaildb.com/api/json/v1/1/search.php?s=long_island_iced_tea", actual);
    }

    @Test
    public void buildURL4() {
        CocktailAPI c = new CocktailAPI(null);                                                    //checking that trim() is working properly
        String actual = c.buildURL(" vodka");
        assertEquals("https://www.thecocktaildb.com/api/json/v1/1/search.php?s=vodka", actual);
    }

    @Test
    public void buildURL5() {
        CocktailAPI c = new CocktailAPI(null);
        String actual = c.buildURL("   vodka   ");                                               //checking for even weirder case for trim()
        assertEquals("https://www.thecocktaildb.com/api/json/v1/1/search.php?s=vodka", actual);
    }
}