package com.nux.dhoan9.firstmvvm.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hoang on 29/05/2017.
 */

public class Category {
    private int id;
    @SerializedName("category_name")
    private String name;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
