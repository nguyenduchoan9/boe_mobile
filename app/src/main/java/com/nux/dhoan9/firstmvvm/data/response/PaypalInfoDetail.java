package com.nux.dhoan9.firstmvvm.data.response;

import java.util.List;

/**
 * Author: hoang
 * Created by: ModelGenerator on 05/07/2017
 */
public class PaypalInfoDetail {
    private String id;
    private String intent;
    private String state;
    private String cart;
    private Payer payer;
    private List<TransactionsItem> transactions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCart() {
        return cart;
    }

    public void setCart(String cart) {
        this.cart = cart;
    }

    public Payer getPayer() {
        return payer;
    }

    public void setPayer(Payer payer) {
        this.payer = payer;
    }

    public List<TransactionsItem> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionsItem> transactions) {
        this.transactions = transactions;
    }
}