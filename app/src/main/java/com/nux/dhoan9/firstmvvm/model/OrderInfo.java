package com.nux.dhoan9.firstmvvm.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by hoang on 14/06/2017.
 */

public class OrderInfo {
    private float total;
    @SerializedName("list_order_detail")
    private List<OrderInfoItem> list;

    public float getTotal() {
        return total;
    }

    public List<OrderInfoItem> getList() {
        return list;
    }
}
