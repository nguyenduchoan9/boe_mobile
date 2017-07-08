package com.nux.dhoan9.firstmvvm.data.response;

/**
 * Author: hoang
 * Created by: ModelGenerator on 05/07/2017
 */
public class TransactionsTransactionsItem1 {
    private Amount amount;
    private Payee payee;
    private String description;
    private ItemList itemList;

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public Payee getPayee() {
        return payee;
    }

    public void setPayee(Payee payee) {
        this.payee = payee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemList getItemList() {
        return itemList;
    }

    public void setItemList(ItemList itemList) {
        this.itemList = itemList;
    }
}