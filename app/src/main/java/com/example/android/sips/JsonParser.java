package com.example.android.sips;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParser {

    private String beverageJsonStr;

    public JsonParser(String beverageJsonStr) {
        this.beverageJsonStr = beverageJsonStr;
    }

    public ArrayList<Beverage> parse() throws JSONException {
        JSONObject jObj = new JSONObject(beverageJsonStr);
        JSONArray jArray = jObj.getJSONArray("drinks");
        ArrayList<Beverage> recipes = new ArrayList<>();

        for (int i = 0; i < jArray.length(); i++){
            String name = jArray.getJSONObject(i).getString("strDrink");
            ArrayList<String> ingredients = new ArrayList<>();
            int j = 0;
            while (!jArray.getJSONObject(i).getString("strIngredient" + (j + 1)).equals("")){
                String part1 = jArray.getJSONObject(i).getString("strMeasure"+(j+1));
                String part2 = jArray.getJSONObject(i).getString("strIngredient"+(j+1));
                String finalString = formatString(part1, part2);
                ingredients.add(finalString);
                j++;
            }

            String instructions = jArray.getJSONObject(i).getString("strInstructions");
            String thumbnailUrl = jArray.getJSONObject(i).getString("strDrinkThumb");

            Beverage beverage = new Beverage(name, ingredients, instructions, thumbnailUrl);
            recipes.add(beverage);
        }
        return recipes;
    }

    public String formatString (String part1, String part2){
        if (!part1.endsWith(" ")){
            part1 = part1 + " ";
        }
        if (part1.contains("\n")){
            part1 = part1.substring(0, part1.indexOf("\n"))+" ";
        }
        if (part2.contains("\n")) {
            part2 = part2.substring(0, part2.indexOf("\n"));
        }
        if (part1.equals(" ")){
            part1 = "";
        }
        String finalString = part1 + part2;
        return finalString;
    }
}
