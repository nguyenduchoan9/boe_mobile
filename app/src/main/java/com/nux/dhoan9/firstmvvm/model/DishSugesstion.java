package com.nux.dhoan9.firstmvvm.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hoang on 16/07/2017.
 */

public class DishSugesstion {
    private int id;
    @SerializedName("dish_name")
    private String dishName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }
}
