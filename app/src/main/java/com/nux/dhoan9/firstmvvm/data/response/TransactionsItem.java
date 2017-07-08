package com.nux.dhoan9.firstmvvm.data.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Author: hoang
 * Created by: ModelGenerator on 03/07/2017
 */
public class TransactionsItem {
    private Amount amount;
    private String description;
    private String custom;
    @SerializedName("invoice_number")
    private String invoiceNumber;
    @SerializedName("item_list")
    private ItemList itemList;
    @SerializedName("related_resources")
    private List<RelatedResourcesItem> relatedResource;

    public List<RelatedResourcesItem> getRelatedResource() {
        return relatedResource;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public ItemList getItemList() {
        return itemList;
    }

    public void setItemList(ItemList itemList) {
        this.itemList = itemList;
    }
}