package com.nux.dhoan9.firstmvvm.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hoang on 14/06/2017.
 */

public class OrderInfoItem implements Parcelable {
    private int id;
    @SerializedName("dish_name")
    private String name;
    private int quantity;
    private float price;
    @SerializedName("create_at")
    private String createAt;

    public OrderInfoItem(int id, String name, int quantity, float price, String createAt) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.createAt = createAt;
    }

    protected OrderInfoItem(Parcel in) {
        id = in.readInt();
        name = in.readString();
        quantity = in.readInt();
        price = in.readFloat();
        createAt = in.readString();
    }

    public static final Creator<OrderInfoItem> CREATOR = new Creator<OrderInfoItem>() {
        @Override
        public OrderInfoItem createFromParcel(Parcel in) {
            return new OrderInfoItem(in);
        }

        @Override
        public OrderInfoItem[] newArray(int size) {
            return new OrderInfoItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(quantity);
        dest.writeFloat(price);
        dest.writeString(createAt);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getPrice() {
        return price;
    }

    public String getCreateAt() {
        return createAt;
    }
}
