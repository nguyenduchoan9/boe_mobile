package com.nux.dhoan9.firstmvvm.data.request;

import com.google.gson.annotations.SerializedName;
import com.nux.dhoan9.firstmvvm.data.response.Amount;

/**
 * Created by hoang on 03/07/2017.
 */

public class PaypalPartialReq {
    public Amount amount;
    @SerializedName("invoice_number")
    public String invoiceNumber;
}
