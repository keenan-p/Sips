package com.example.android.bevs;

import android.graphics.Bitmap;

class Beverage {

    private String name;
    private Bitmap thumbnail;

    public Beverage (String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }
}
