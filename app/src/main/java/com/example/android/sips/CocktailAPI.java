package com.example.android.sips;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CocktailAPI {

    private String param;

    public CocktailAPI(String param) {
        this.param = param;
    }

    public String getCocktailJson() {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String beverageJsonStr = null;

        try {
            URL url = new URL(buildURL(param));

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

        return beverageJsonStr;
    }

    public String buildURL(String param){
        final String DRINK_BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/search.php?";
        final String QUERY_PARAM = "s=";
        if (param.startsWith(" ")){
            while (param.startsWith(" ")){
                param = param.replaceFirst(" ", "");
            }
        }
        if (param.contains(" ")){
            param = param.replaceAll(" ", "_");
        }
        return DRINK_BASE_URL+QUERY_PARAM+param;
    }
}
