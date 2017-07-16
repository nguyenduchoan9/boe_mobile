package com.nux.dhoan9.firstmvvm.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/**
 * Created by hoang on 16/07/2017.
 */

public class ParcelDishList implements Parcelable {
    private ArrayList<Dish> dishList = new ArrayList<>();

    public ArrayList<Dish> getDishList() {
        return dishList;
    }

    public void setDishList(ArrayList<Dish> dishList) {
        this.dishList = dishList;
    }

    public ParcelDishList() {
        dishList = new ArrayList<>();
    }

    public ParcelDishList(ArrayList<Dish> dishList) {
        this.dishList = dishList;
    }

    protected ParcelDishList(Parcel in) {
        in.readTypedList(dishList, Dish.CREATOR);
    }

    public static final Creator<ParcelDishList> CREATOR = new Creator<ParcelDishList>() {
        @Override
        public ParcelDishList createFromParcel(Parcel in) {
            return new ParcelDishList(in);
        }

        @Override
        public ParcelDishList[] newArray(int size) {
            return new ParcelDishList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(dishList);
    }
}
