package com.nux.dhoan9.firstmvvm.model;

/**
 * Created by hoang on 12/05/2017.
 */

public class CartItem extends Dish {
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public CartItem(Dish dish, int quantity) {
        super(dish.getId(), dish.getName(), "description", dish.getPrice(), dish.getImage());
        this.quantity = quantity;
    }
}
