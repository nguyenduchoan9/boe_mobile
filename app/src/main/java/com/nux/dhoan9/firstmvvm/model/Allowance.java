package com.nux.dhoan9.firstmvvm.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hoang on 8/19/17.
 */

public class Allowance implements Parcelable{
    private float amount;
    private String description;
    private String date;

    protected Allowance(Parcel in) {
        amount = in.readFloat();
        description = in.readString();
        date = in.readString();
    }

    public static final Creator<Allowance> CREATOR = new Creator<Allowance>() {
        @Override
        public Allowance createFromParcel(Parcel in) {
            return new Allowance(in);
        }

        @Override
        public Allowance[] newArray(int size) {
            return new Allowance[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(amount);
        parcel.writeString(description);
        parcel.writeString(date);
    }

    public float getAmmount() {
        return amount;
    }

    public void setAmmount(float ammount) {
        this.amount = ammount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
