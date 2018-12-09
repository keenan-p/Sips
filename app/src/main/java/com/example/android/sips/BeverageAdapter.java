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

class BeverageAdapter extends ArrayAdapter<Beverage> {

    private Context context;

    public BeverageAdapter(Context context, ArrayList<Beverage> bevs) {
        super(context, 0, bevs);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // get the
        Beverage currBeverage = getItem(position);

        View listElement = convertView;
        // if the current view is not being used, inflate the view
        if (listElement == null) {
            listElement = LayoutInflater.from(getContext()).inflate(
                    R.layout.beverage_list_item, parent, false);
        }

        TextView beverageName = listElement.findViewById(R.id.bev_name_text_view);
        beverageName.setText(Objects.requireNonNull(currBeverage).getName());

        Glide.with(context)
                .load(currBeverage.getImageSource())
                .into((ImageView) listElement.findViewById(R.id.bev_thumbnail));

        return listElement;
    }
}
