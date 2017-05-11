package com.nux.dhoan9.firstmvvm.model;

import java.util.List;
/**
 * Created by hoang on 09/05/2017.
 */

public class MenuCategories {
    private String title;
    private List<Dish> dishes;

    public MenuCategories(String title, List<Dish> dishes) {
        this.title = title;
        this.dishes = dishes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Dish> getDish() {
        return dishes;
    }

    public void setDish(List<Dish> dishes) {
        this.dishes = dishes;
    }
}
