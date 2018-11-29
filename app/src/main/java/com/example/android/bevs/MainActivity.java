package com.example.android.bevs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

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
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting() &&
                manager.getActiveNetworkInfo().isAvailable() &&
                manager.getActiveNetworkInfo().isConnected();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Bevs");

        beverages = new ArrayList<>();
        beverages.add(new Beverage("Use the search bar to find beverages."));

        ListView beveragesListView = findViewById(R.id.drink_list);

        adapter = new BeverageAdapter(this, beverages);

        beveragesListView.setAdapter(adapter);

        searchBar = (EditText) findViewById(R.id.search_bar);
        Button submit = (Button) findViewById(R.id.submit);

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
                    beverages.add(new Beverage("No internet connection."));
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private class FetchBeveragesTask extends AsyncTask<String, Void, String[]> {

        private String[] getBeverageDataFromJson(String beverageJsonStr) throws JSONException {

            final String DRINKS_ARRAY = "drinks";
            final String STR_DRINK = "strDrink";

            final String NO_IMAGE_AVAILABLE = "https://upload.wikimedia.org/wikipedia/commons" +
                    "/thumb/a/ac/No_image_available.svg/300px-No_image_available.svg.png";

            int resultStringsIndex = 0;

            JSONObject drinkJson = new JSONObject(beverageJsonStr);
            JSONArray drinksList = drinkJson.getJSONArray(DRINKS_ARRAY);

            final int MAX_NUM_DRINKS = drinksList.length();
            final int NUM_DRINK_COMPONENTS = 1;
            String[] resultStrings = new String[MAX_NUM_DRINKS * NUM_DRINK_COMPONENTS];

            // get info from the first 2 search results
            for (int searchResultIndex = 0; searchResultIndex < drinksList.length(); searchResultIndex++) {
                String drinkName;
                String thumbnailURL;

                JSONObject drinkInfo = drinksList.getJSONObject(searchResultIndex);
                drinkName = drinkInfo.getString(STR_DRINK);

                resultStrings[resultStringsIndex] = drinkName;
                resultStringsIndex += NUM_DRINK_COMPONENTS;
            }

            return resultStrings;
        }

        @Override
        protected String[] doInBackground(String... params) {

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
                if (inputStream == null) {
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line = "";
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
        protected void onPostExecute(String[] result) {
            if (result != null) {
                beverages.clear();

                // Load the text first.
                int componentIndex = 0;

                while (componentIndex < result.length) {
                    beverages.add(new Beverage(result[componentIndex]));
                    componentIndex++;
                }
                adapter.notifyDataSetChanged();
            }
            else {
                beverages.clear();
                beverages.add(new Beverage("There are no results that match your search"));
                adapter.notifyDataSetChanged();
            }
        }
    }

}
