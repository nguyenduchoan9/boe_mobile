package com.nux.dhoan9.firstmvvm.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hoang on 08/05/2017.
 */

public class Dish {
    private int id;
    @SerializedName("dish_name")
    private String name;
    private String description;
    private float price;
    private String image;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return 10000;
    }

    public String getImage() {
        return image;
    }

    public Dish(int id, String name, String description, float price, String image) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
    }
}
