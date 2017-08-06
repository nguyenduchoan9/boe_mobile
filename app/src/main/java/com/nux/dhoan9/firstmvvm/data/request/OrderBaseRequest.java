package com.nux.dhoan9.firstmvvm.data.request;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by hoang on 06/08/2017.
 */

public class OrderBaseRequest {
    private String order;
    @SerializedName("table_number")
    private int tableNumber;
    private List<DescriptionRequest> list;

    public OrderBaseRequest(String order, int tableNumber, List<DescriptionRequest> list) {
        this.order = order;
        this.tableNumber = tableNumber;
        this.list = list;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

}
