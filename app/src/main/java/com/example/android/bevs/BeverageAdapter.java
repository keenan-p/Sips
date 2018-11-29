package com.example.android.bevs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

class BeverageAdapter extends ArrayAdapter<Beverage> {

    public BeverageAdapter(Context context, ArrayList<Beverage> bevs) {
        super(context, 0, bevs);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Beverage currBeverage = getItem(position);

        View listElement = convertView;

        if (listElement == null) {
            listElement = LayoutInflater.from(getContext()).inflate(
                    R.layout.beverage_list_item, parent, false);
        }

        TextView beverageName = listElement.findViewById(R.id.bev_name_text_view);
        beverageName.setText(Objects.requireNonNull(currBeverage).getName());

//        ImageView beverageThumbnail = (ImageView) listElement.findViewById(R.id.bev_thumbnail);
//        beverageThumbnail.setImageBitmap(currBeverage.getThumbnail());

        return listElement;
    }
}
