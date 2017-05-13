package com.nux.dhoan9.firstmvvm.data.request;

/**
 * Created by hoang on 12/05/2017.
 */

public class CartItemRequest {
    public int dishId;
    public int quantity;

    public CartItemRequest(int dishId, int quantity) {
        this.dishId = dishId;
        this.quantity = quantity;
    }
}
