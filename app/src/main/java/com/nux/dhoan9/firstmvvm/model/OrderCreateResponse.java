package com.nux.dhoan9.firstmvvm.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by hoang on 06/07/2017.
 */

public class OrderCreateResponse {
    @SerializedName("order_id")
    private int orderId;
    private List<Dish> dish;

    public int getOrderId() {
        return orderId;
    }

    public List<Dish> getDish() {
        return dish;
    }
}
