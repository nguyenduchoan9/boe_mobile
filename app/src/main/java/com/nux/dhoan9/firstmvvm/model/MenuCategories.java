package com.nux.dhoan9.firstmvvm.model;

import java.util.List;
/**
 * Created by hoang on 09/05/2017.
 */

public class MenuCategories {
    private Category category;
    private List<Dish> dishes;

    public MenuCategories(Category category, List<Dish> dishes) {
        this.category = category;
        this.dishes = dishes;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Dish> getDish() {
        return dishes;
    }

    public void setDish(List<Dish> dishes) {
        this.dishes = dishes;
    }
}
