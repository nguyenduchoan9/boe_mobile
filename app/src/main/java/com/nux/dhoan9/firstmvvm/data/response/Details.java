package com.nux.dhoan9.firstmvvm.data.response;

/**
 * Author: hoang
 * Created by: ModelGenerator on 03/07/2017
 */
public class Details {
    private String subtotal;
    private String tax;
    private String shipping;
    private String shippingDiscount;

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getShippingDiscount() {
        return shippingDiscount;
    }

    public void setShippingDiscount(String shippingDiscount) {
        this.shippingDiscount = shippingDiscount;
    }
}