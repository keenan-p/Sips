package com.example.android.sips;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Class the parse a JSON object
 */
public class JsonParser {

    private String beverageJsonStr;

    /**
     * Constructor for the class
     * @param beverageJsonStr the JSON string which will be used to make JSON object
     */
    public JsonParser(String beverageJsonStr) {
        this.beverageJsonStr = beverageJsonStr;
    }

    /**
     * Method to parse JSON
     * @return ArrayList<Beverage> beverages found in the JSON
     * @throws JSONException if JSON is not found
     */
    public ArrayList<Beverage> parse() throws JSONException {
        JSONObject jObj = new JSONObject(beverageJsonStr); //create a new JSON object and array
        JSONArray jArray = jObj.getJSONArray("drinks");
        ArrayList<Beverage> recipes = new ArrayList<>();

        for (int i = 0; i < jArray.length(); i++){ //for each elemnt of the JSON array
            String name = jArray.getJSONObject(i).getString("strDrink"); //grab the name of the drink
            ArrayList<String> ingredients = new ArrayList<>();
            int j = 0;
            while (!jArray.getJSONObject(i).getString("strIngredient" + (j + 1)).equals("")){ //while there are still more ingredients to read in
                String part1 = jArray.getJSONObject(i).getString("strMeasure"+(j+1)); //grab the measure
                String part2 = jArray.getJSONObject(i).getString("strIngredient"+(j+1)); //grab the ingredient
                String finalString = formatString(part1, part2); //call method to format properly
                ingredients.add(finalString);
                j++;
            }

            String instructions = jArray.getJSONObject(i).getString("strInstructions"); //grab the instructions
            String thumbnailUrl = jArray.getJSONObject(i).getString("strDrinkThumb"); //grab the drink thumbnail

            Beverage beverage = new Beverage(name, ingredients, instructions, thumbnailUrl); //create a new Beverage object with the previously gathered data
            recipes.add(beverage); //add to the ArrayList
        }
        return recipes;
    }

    /**
     * Method to format ingredients string
     * @param part1 the "measure" part of the string
     * @param part2 the "ingredient" part of the string
     * @return a string formatting and combining parts 1 and 2
     */
    public String formatString (String part1, String part2){ //some of the database's strings were irregularly formatted
        if (!part1.endsWith(" ")){ //add a space to any measures that do not end in a space
            part1 = part1 + " ";
        }
        if (part1.contains("\n")){ //if measure contains a newline, remove anything following the newline and add a space to follow it
            part1 = part1.substring(0, part1.indexOf("\n"))+" ";
        }
        if (part2.contains("\n")) { //no need to add space following part2, since it is the end of the total string
            part2 = part2.substring(0, part2.indexOf("\n"));
        }
        if (part1.equals(" ")){ //sometimes, no measure is provided (just "Salt"). In this case, we eliminate this part of the string
            part1 = "";
        }
        String finalString = part1 + part2; //combined the two halves
        return finalString;
    }
}
