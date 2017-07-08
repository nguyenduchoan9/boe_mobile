package com.nux.dhoan9.firstmvvm.data.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hoang on 28/06/2017.
 */

public class CartDishAvailable implements Parcelable{
    private int id;
    private String name;

    protected CartDishAvailable(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<CartDishAvailable> CREATOR = new Creator<CartDishAvailable>() {
        @Override
        public CartDishAvailable createFromParcel(Parcel in) {
            return new CartDishAvailable(in);
        }

        @Override
        public CartDishAvailable[] newArray(int size) {
            return new CartDishAvailable[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
}
