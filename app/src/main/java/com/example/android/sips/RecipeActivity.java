package com.example.android.sips;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class RecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_layout);

        Intent recipeIntent = getIntent();
        String name = recipeIntent.getStringExtra("name");
        String imgURL = recipeIntent.getStringExtra("imgURL");
        String ingredients = recipeIntent.getStringExtra("ingredients");
        String recipe = recipeIntent.getStringExtra("recipe");

        setTitle(name);

        TextView beverageTextView = findViewById(R.id.beverage_name);
        ImageView beverageImageView = findViewById(R.id.beverage_image);
        TextView ingredientsTextView = findViewById(R.id.ingredients);
        TextView recipeTextView = findViewById(R.id.recipe);

        beverageTextView.setText(name);
        ingredientsTextView.setText(ingredients);
        recipeTextView.setText(recipe);

        Glide.with(this)
                .load(imgURL)
                .into(beverageImageView);
    }
}
