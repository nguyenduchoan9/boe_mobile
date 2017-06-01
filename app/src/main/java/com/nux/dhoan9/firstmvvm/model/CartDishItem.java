package com.nux.dhoan9.firstmvvm.model;

/**
 * Created by hoang on 01/06/2017.
 */

public class CartDishItem {
    public int quantity;
    public Dish dish;

    public CartDishItem(int quantity, Dish dish) {
        this.quantity = quantity;
        this.dish = dish;
    }


}
