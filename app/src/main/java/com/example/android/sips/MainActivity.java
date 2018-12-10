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

import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Beverage> beverages;
    private BeverageAdapter adapter;
    private ListView beveragesListView;

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

        setTitle("Sips");

        beverages = new ArrayList<>();
        beverages.add(new Beverage("Use the search bar to find beverages.", null, "",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Vector_search_icon.svg/200px-Vector_search_icon.svg.png"));

        beveragesListView = findViewById(R.id.beverage_list);

        adapter = new BeverageAdapter(this, beverages);

        beveragesListView.setAdapter(adapter);
//        ListView beveragesListView = findViewById(R.id.drink_list);
//
//        adapter = new BeverageAdapter(this, beverages);
//
//        beveragesListView.setAdapter(adapter);
//
//        beveragesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Beverage beverage = beverages.get(position);
//                Intent recipeIntent = new Intent(MainActivity.this, RecipeActivity.class);
//                recipeIntent.putExtra("name", beverage.getName());
//                recipeIntent.putExtra("imgURL", beverage.getImageSource());
//                recipeIntent.putExtra("ingredients", beverage.getIngredients());
//                recipeIntent.putExtra("recipe", beverage.getRecipe());
//                startActivity(recipeIntent);
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // inflate menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchMenuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);

        final Activity mainActivity = this;

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (isConnectedToInternet()) {

                    beveragesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Beverage beverage = beverages.get(position);
                            Intent recipeIntent = new Intent(MainActivity.this, RecipeActivity.class);
                            recipeIntent.putExtra("name", beverage.getName());
                            recipeIntent.putExtra("imgURL", beverage.getImageSource());
                            recipeIntent.putExtra("ingredients", beverage.getIngredients());
                            recipeIntent.putExtra("recipe", beverage.getRecipe());
                            startActivity(recipeIntent);
                        }
                    });

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

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                hideKeyboard(mainActivity);
                return true;
            }
        });

        return true;
    }

    private class FetchBeveragesTask extends AsyncTask<String, Void, ArrayList<Beverage>> {

        private ArrayList<Beverage> getBeverageDataFromJson(String beverageJsonStr) throws JSONException {

            JsonParser jsonParser = new JsonParser(beverageJsonStr);
            return jsonParser.parse();

        }



        @Override
        protected ArrayList<Beverage> doInBackground(String... params) {

            CocktailAPI api = new CocktailAPI(params[0]);
            String beverageJsonStr = api.getCocktailJson();

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
