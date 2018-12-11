package com.example.android.sips;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Objects;

/**
 * BeverageAdapter is a subclass of ArrayAdapter. It provides Views to the
 * ListView in MainActivity. Each View contains the beverage name and a
 * thumbnail.
 */
class BeverageAdapter extends ArrayAdapter<Beverage> {

    // context instance variable used to create list element from layout file
    // and load image using Glide library
    private Context context;

    /**
     * Constructs a BeverageAdapter object.
     * @param context
     * @param bevs
     */
    public BeverageAdapter(Context context, ArrayList<Beverage> bevs) {
        super(context, 0, bevs);
        this.context = context;
    }

    /**
     * Gets a View that represents a beverage at a particular position in
     * the list.
     * @param position
     * @param convertView
     * @param parent
     * @return View representing an element in list of beverages
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // get the data item at the specified position and store it in currBeverage
        Beverage currBeverage = getItem(position);

        View listElement = convertView;
        // if the current view is not being used, inflate the view
        if (listElement == null) {
            listElement = LayoutInflater.from(getContext()).inflate(
                    R.layout.beverage_list_item, parent, false);
        }

        // set the name of the beverage
        TextView beverageName = listElement.findViewById(R.id.bev_name_text_view);
        beverageName.setText(Objects.requireNonNull(currBeverage).getName());

        // use Glide library to load thumbnail
        Glide.with(context)
                .load(currBeverage.getImageSource())
                .into((ImageView) listElement.findViewById(R.id.bev_thumbnail));

        return listElement;
    }
}
