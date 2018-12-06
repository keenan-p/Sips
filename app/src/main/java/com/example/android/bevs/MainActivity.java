package com.example.android.bevs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Beverage> beverages;
    private BeverageAdapter adapter;
    private EditText searchBar;

    private boolean isConnectedToInternet() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();

        return (activeNetworkInfo != null && activeNetworkInfo.isConnected());

    }

    // credit to the community wiki on stackoverflow for this function
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Bevs");

        beverages = new ArrayList<>();
        beverages.add(new Beverage("Use the search bar to find beverages.", null, "", ""));

        ListView beveragesListView = findViewById(R.id.drink_list);

        adapter = new BeverageAdapter(this, beverages);

        beveragesListView.setAdapter(adapter);

        searchBar = findViewById(R.id.search_bar);
        Button submit = findViewById(R.id.submit);

        beveragesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Beverage beverage = beverages.get(position);
                Intent recipeIntent = new Intent(MainActivity.this, RecipeActivity.class);
                recipeIntent.putExtra("imgURL", beverage.getImageSource());
                recipeIntent.putExtra("ingredients", beverage.getIngredients());
                recipeIntent.putExtra("recipe", beverage.getRecipe());
                startActivity(recipeIntent);
            }
        });

        final Activity mainActivity = this;

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnectedToInternet()) {
                    String searchQuery = searchBar.getText().toString();
                    FetchBeveragesTask beveragesTask = new FetchBeveragesTask();
                    beveragesTask.execute(searchQuery);
                }
                else {
                    beverages.clear();
                    beverages.add(new Beverage("No internet connection.", null, "", ""));
                    adapter.notifyDataSetChanged();
                }
                hideKeyboard(mainActivity);
            }
        });
    }

    private class FetchBeveragesTask extends AsyncTask<String, Void, ArrayList<Beverage>> {

        private ArrayList<Beverage> getBeverageDataFromJson(String beverageJsonStr) throws JSONException {

            JSONObject jObj = new JSONObject(beverageJsonStr);
            JSONArray jArray = jObj.getJSONArray("drinks");
            ArrayList<Beverage> recipes = new ArrayList<>();

            for (int i = 0; i < jArray.length(); i++){
                String name = jArray.getJSONObject(i).getString("strDrink");
                ArrayList<String> ingredients = new ArrayList<>();
                int j = 0;
                while (!jArray.getJSONObject(i).getString("strIngredient" + (j + 1)).equals("")){
                    String part1 = jArray.getJSONObject(i).getString("strMeasure"+(j+1));

                    if (!part1.endsWith(" ")){
                        part1 = part1 + " ";
                    }
                    String part2;
                    part2 = jArray.getJSONObject(i).getString("strIngredient"+(j+1));

                    if (part1.contains("\n")){
                        part1 = part1.substring(0, part1.indexOf("\n"));
                    } else if (part2.contains("\n")) {
                        part2 = part2.substring(0, part2.indexOf("\n"));
                    } else if (part1.equals(" ")){
                        part1 = "";
                    }

                    String finalString = part1 + part2;
                    ingredients.add(finalString);
                    j++;
                    }

                    String instructions = jArray.getJSONObject(i).getString("strInstructions");
                    String thumbnailUrl = jArray.getJSONObject(i).getString("strDrinkThumb");

                    System.out.println(name);
                    Beverage beverage = new Beverage(name, ingredients, instructions, thumbnailUrl);
                    recipes.add(beverage);
                }
                return recipes;
            }



        @Override
        protected ArrayList<Beverage> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String beverageJsonStr = null;

            final String DRINK_BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/search.php?";
            final String QUERY_PARAM = "s";
            Uri builtUri = Uri.parse(DRINK_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, params[0]).build();

            try {
                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // read the input stream into the string
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                beverageJsonStr = buffer.toString();

            } catch (MalformedURLException e) {
                Log.e("MalformedURLException", "error");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    }
                    catch (final IOException e){
                        Log.e("FetchBeverages", "Error closing stream", e);
                        e.printStackTrace();
                    }
                }
            }

            try {
                return getBeverageDataFromJson(beverageJsonStr);
            } catch (JSONException e) {
                Log.e("FetchBeverages", e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Beverage> result) {
            if (result != null) {
                beverages.clear();

                // Load the text first.
                int componentIndex = 0;

                System.out.println(result.size());

                while (componentIndex < result.size()) {
                    beverages.add(result.get(componentIndex));
                    componentIndex++;
                }
                adapter.notifyDataSetChanged();
            }
            else {
                beverages.clear();
                beverages.add(new Beverage("There are no results that match your search", null, "", ""));
                adapter.notifyDataSetChanged();
            }
        }
    }

}
