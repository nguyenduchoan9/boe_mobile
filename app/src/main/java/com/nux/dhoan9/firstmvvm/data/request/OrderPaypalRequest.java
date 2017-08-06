package com.nux.dhoan9.firstmvvm.data.request;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by hoang on 06/08/2017.
 */

public class OrderPaypalRequest extends OrderBaseRequest {
    @SerializedName("payment_id")
    private String paymentId;

    public OrderPaypalRequest(String order, int tableNumber, List<DescriptionRequest> list, String paymentId) {
        super(order, tableNumber, list);
        this.paymentId = paymentId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
}
