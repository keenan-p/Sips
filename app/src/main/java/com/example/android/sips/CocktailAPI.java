package com.example.android.sips;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class that connects to and queries API
 */
public class CocktailAPI {

    private String param;

    /**
     * Constructor for the class
     * @param param the search terms which will be asked for from the API
     */
    public CocktailAPI(String param) {
        this.param = param;
    }

    /**
     * Method to connect to and query the API
     * @return a String representing the JSON returned by the API
     */
    public String getCocktailJson() {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String beverageJsonStr = null;

        try {
            URL url = new URL(buildURL(param)); //call method to get properly formatted URL

            urlConnection = (HttpURLConnection) url.openConnection(); //open connection
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream(); // read the input stream into the string
            StringBuilder buffer = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) { //add each line of the queried page to the string
                buffer.append(line).append("\n");
            }

            beverageJsonStr = buffer.toString();
        }
        catch (MalformedURLException e) {  //must handle various exceptions that may occur
            Log.e("MalformedURLException", "error");
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally { //these must take place in any case to make sure that the connection/reader is closed
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

    /**
     * Method to construct a URL to query
     * @param param the search terms entered by the user
     * @return String representing URL which will be contacted
     */
    public String buildURL(String param){
        final String DRINK_BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/search.php?"; //this appears for all queries
        final String QUERY_PARAM = "s="; //for all drinks, we use this parameter
        if (param.startsWith(" ")){ //must format the entered search terms into a proper URL readable by the API
             param = param.trim(); //accidentally typing in spaces to start will be handled
        }
        if (param.contains(" ")){ //any spaces between words (long island iced tea) must be replaced with _ to be read properly
            param = param.replaceAll(" ", "_");
        }
        return DRINK_BASE_URL+QUERY_PARAM+param; //construct and return the final url
    }
}
