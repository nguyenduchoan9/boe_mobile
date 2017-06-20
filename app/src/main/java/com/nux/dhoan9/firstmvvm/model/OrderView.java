package com.nux.dhoan9.firstmvvm.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hoang on 13/06/2017.
 */

public class OrderView {
    @SerializedName("list_order_id")
    private String listOrderId;
    private float total;
    @SerializedName("create_at")
    private String orderDate;

    public String getListOrderId() {
        return listOrderId;
    }

    public float getTotal() {
        return total;
    }

    public String getOrderDate() {
        return orderDate;
    }
}
