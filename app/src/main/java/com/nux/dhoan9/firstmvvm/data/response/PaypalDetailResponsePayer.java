package com.nux.dhoan9.firstmvvm.data.response;

/**
 * Author: hoang
 * Created by: ModelGenerator on 05/07/2017
 */
public class PaypalDetailResponsePayer {
    private String paymentMethod;
    private String status;
    private PayerInfo payerInfo;

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PayerInfo getPayerInfo() {
        return payerInfo;
    }

    public void setPayerInfo(PayerInfo payerInfo) {
        this.payerInfo = payerInfo;
    }
}