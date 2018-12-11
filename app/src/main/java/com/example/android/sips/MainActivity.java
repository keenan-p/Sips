package com.example.android.sips;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Represents the main screen of the Android application
 */
public class MainActivity extends AppCompatActivity {

    private ArrayList<Beverage> beverages;
    private BeverageAdapter adapter;
    private ListView beveragesListView;

    /**
     * Checks if device is connected to internet
     * @return boolean value true if connected
     */
    private boolean isConnectedToInternet() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();

        return (activeNetworkInfo != null && activeNetworkInfo.isConnected());

    }

    /**
     * Hides soft keyboard.
     * Credit to stackoverflow community wiki for this method
     * @param activity Activity in which the View resides
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window
        // token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Method is called when activity starts.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // use activity_main.xml as layout for this activity
        setContentView(R.layout.activity_main);

        // set the title of this activity to "Sips"
        setTitle("Sips");

        // create a new ArrayList of Beverage objects and store it in beverages
        beverages = new ArrayList<>();
        // add an element that will contain a message prompting the user to search for beverages
        beverages.add(new Beverage("Use the search bar to find beverages.", null,
                "", "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/" +
                "Vector_search_icon.svg/200px-Vector_search_icon.svg.png"));

        // let beveragesListView be the ListView component beverage_list created in
        // the activity_main layout resource file
        beveragesListView = findViewById(R.id.beverage_list);

        // create a new BeverageAdapter object
        adapter = new BeverageAdapter(this, beverages);

        // connect the ListView to our BeverageAdapter, which will provide Views with beverage
        // information to the ListView
        beveragesListView.setAdapter(adapter);
    }

    /**
     * Determine the contents of the options menu.
     * @param menu
     * @return true, to ensure that the menu will be displayed
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // create menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        // create SearchView widget in menu
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchMenuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        // ensure that search is visible and submit button is enabled
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);

        // used to hide soft keyboard after submitting search
        final Activity mainActivity = this;

        // determine behavior when user interacts with SearchView widget
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            /**
             * Code is executed when user submits search.
             * @param query
             * @return true so that default behavior is overridden
             */
            @Override
            public boolean onQueryTextSubmit(String query) {
                // make sure that device is connected to the internet
                if (isConnectedToInternet()) {

                    // make ListView elements clickable
                    beveragesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        /**
                         * Method uses an intent to open the recipe page for the clicked beverage.
                         * The putExtra() method is used to pass the beverage information to the
                         * recipe activity.
                         * @param parent
                         * @param view
                         * @param position
                         * @param id
                         */
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Beverage beverage = beverages.get(position);
                            // if statement ensures that list item is not clickable if the search
                            // returns no results.
                            if (!beverage.getName().equals("There are no results that match your search.")) {
                                Intent recipeIntent = new Intent(MainActivity.this, RecipeActivity.class);
                                recipeIntent.putExtra("name", beverage.getName());
                                recipeIntent.putExtra("imgURL", beverage.getImageSource());
                                recipeIntent.putExtra("ingredients", beverage.getIngredients());
                                recipeIntent.putExtra("recipe", beverage.getRecipe());
                                startActivity(recipeIntent);
                            }
                        }
                    });

                    //
                    FetchBeveragesTask beveragesTask = new FetchBeveragesTask();
                    beveragesTask.execute(query);
                    searchMenuItem.collapseActionView();
                    hideKeyboard(mainActivity);
                }
                else {
                    beverages.clear();
                    beverages.add(new Beverage("No internet connection.", null, "", ""));
                    adapter.notifyDataSetChanged();
                }

                return true;
            }

            /**
             * Code is executed when text field in SearchView is changed.
             * @param newText
             * @return false, let SearchView perform default action
             */
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // specify the application behavior when the SearchView widget is expanded and collapsed
        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            /**
             * Called when the SearchView menu item is expanded.
             * @param item
             * @return true, to ensure that the item expands
             */
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            /**
             * Called when the SearchView menu item is collapsed.
             * Hide soft keyboard.
             * @param item
             * @return true, to ensure that the item collapses
             */
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                hideKeyboard(mainActivity);
                return true;
            }
        });


        return true;
    }

    /**
     * FetchBeveragesTask is used by MainActivity to query TheCocktailDB for beverages matching the
     * user's search term. The beverage information is used to populate the beverage ListView.
     */
    private class FetchBeveragesTask extends AsyncTask<String, Void, ArrayList<Beverage>> {

        /**
         * Use JsonParser class to get the beverage data in the form of an ArrayList
         * of Beverage objects.
         * @param beverageJsonStr
         * @return ArrayList<Beverage>
         * @throws JSONException
         */
        private ArrayList<Beverage> getBeverageDataFromJson(String beverageJsonStr) throws JSONException {
            JsonParser jsonParser = new JsonParser(beverageJsonStr);
            return jsonParser.parse();
        }

        /**
         * Use the CockTailAPI class to query database and get Json string. Pass this string
         * as an argument to the getBeverageDataFromJson method to obtain an ArrayList containing
         * this data. Return this ArrayList.
         * @param params
         * @return ArrayList<Beverage> or null if no results
         */
        @Override
        protected ArrayList<Beverage> doInBackground(String... params) {
            CocktailAPI api = new CocktailAPI(params[0]);
            String beverageJsonStr = api.getCocktailJson();

            try {
                return getBeverageDataFromJson(beverageJsonStr);
            }
            catch (JSONException e) {
                Log.e("FetchBeverages", e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        /**
         * Runs on the UI thread after doInBackground. Uses return value, "result", from
         * doInBackground to populate the beverages ListView. If result is null, display a message
         * indicating that the search produced no results.
         * @param result
         */
        @Override
        protected void onPostExecute(ArrayList<Beverage> result) {
            if (result != null) {
                // delete all elements in the beverages ArrayList
                beverages.clear();

                // copy results to beverages ArrayList
                for (int resultIndex = 0; resultIndex < result.size(); resultIndex++) {
                    beverages.add(result.get(resultIndex));
                }

                // refresh the ListView
                adapter.notifyDataSetChanged();
            }
            else {
                // if no results, delete all elements from beverages ArrayList, add an element
                // containing a failure message and image, and refresh the ListView
                beverages.clear();
                beverages.add(new Beverage("There are no results that match your search.",
                        null, "", "https://upload.wikimedia.org/" +
                        "wikipedia/commons/thumb/a/a0/Font_Awesome_5_regular_frown.svg/200px-" +
                        "Font_Awesome_5_regular_frown.svg.png"));
                adapter.notifyDataSetChanged();
            }

            // reset scroll position after each search
            beveragesListView.smoothScrollToPosition(0);
        }
    }

}
