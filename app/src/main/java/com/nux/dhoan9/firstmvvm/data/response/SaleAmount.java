package com.nux.dhoan9.firstmvvm.data.response;

/**
 * Author: hoang
 * Created by: ModelGenerator on 05/07/2017
 */
public class SaleAmount {
    private String total;
    private String currency;
    private Details details;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }
}