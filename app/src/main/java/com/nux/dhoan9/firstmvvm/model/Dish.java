package com.nux.dhoan9.firstmvvm.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hoang on 08/05/2017.
 */

public class Dish implements Parcelable{
    private int id;
    @SerializedName("dish_name")
    private String name;
    private String description;
    private float price;
    private String image;
//    private Image image;

    protected Dish(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        price = in.readFloat();
        image = in.readString();
    }

    public static final Creator<Dish> CREATOR = new Creator<Dish>() {
        @Override
        public Dish createFromParcel(Parcel in) {
            return new Dish(in);
        }

        @Override
        public Dish[] newArray(int size) {
            return new Dish[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }
//    public String getImage() {
//        return image.url;
//    }

    public Dish(int id, String name, String description, float price, String image) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
//        this.image = new Image(image);
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeFloat(price);
        dest.writeString(image);
    }
}
