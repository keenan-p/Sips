package com.example.android.sips;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Represents the screen displaying a beverage recipe
 */
public class RecipeActivity extends AppCompatActivity {

    /**
     * Method is called when activity starts.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the layout of the activity
        setContentView(R.layout.recipe_layout);

        // get the beverage data passed from the main activity
        Intent recipeIntent = getIntent();
        String name = recipeIntent.getStringExtra("name");
        String imgURL = recipeIntent.getStringExtra("imgURL");
        String ingredients = recipeIntent.getStringExtra("ingredients");
        String recipe = recipeIntent.getStringExtra("recipe");

        // set the title that will appear in the ActionBar
        setTitle(name);

        // declare and initialize Views
        ImageView beverageImageView = findViewById(R.id.beverage_image);
        TextView ingredientsTextView = findViewById(R.id.ingredients);
        TextView recipeTextView = findViewById(R.id.recipe);

        // display ingredients and recipe instructions
        ingredientsTextView.setText(ingredients);
        recipeTextView.setText(recipe);

        // load image
        Glide.with(this)
                .load(imgURL)
                .into(beverageImageView);
    }
}
