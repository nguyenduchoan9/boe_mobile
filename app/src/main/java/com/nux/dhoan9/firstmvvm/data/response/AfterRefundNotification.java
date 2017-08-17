package com.nux.dhoan9.firstmvvm.data.response;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.nux.dhoan9.firstmvvm.model.Dish;
import java.util.List;

/**
 * Created by hoang on 28/07/2017.
 */

public class AfterRefundNotification implements Parcelable {
    @SerializedName("total")
    private float total;
    @SerializedName("dishes")
    private List<DishAndQuantity> dishes;
    @SerializedName("order_id")
    private int orderId;
    @SerializedName("table_number")
    private int tableNumber;
    @SerializedName("date")
    private String billDate;



    public static class DishAndQuantity implements Parcelable {
        @SerializedName("dish_name")
        private String dishName;
        private float price;
        private int quantity;

        protected DishAndQuantity(Parcel in) {
            dishName = in.readString();
            price = in.readFloat();
            quantity = in.readInt();
        }

        public static final Creator<DishAndQuantity> CREATOR = new Creator<DishAndQuantity>() {
            @Override
            public DishAndQuantity createFromParcel(Parcel in) {
                return new DishAndQuantity(in);
            }

            @Override
            public DishAndQuantity[] newArray(int size) {
                return new DishAndQuantity[size];
            }
        };

        public int getQuantity() {
            return quantity;
        }

        public String getDishName() {
            return dishName;
        }

        public float getPrice() {
            return price;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(dishName);
            dest.writeFloat(price);
            dest.writeInt(quantity);
        }
    }

    protected AfterRefundNotification(Parcel in) {
        total = in.readFloat();
        dishes = in.createTypedArrayList(DishAndQuantity.CREATOR);
        orderId = in.readInt();
        tableNumber = in.readInt();
        billDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(total);
        dest.writeTypedList(dishes);
        dest.writeInt(orderId);
        dest.writeInt(tableNumber);
        dest.writeString(billDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AfterRefundNotification> CREATOR = new Creator<AfterRefundNotification>() {
        @Override
        public AfterRefundNotification createFromParcel(Parcel in) {
            return new AfterRefundNotification(in);
        }

        @Override
        public AfterRefundNotification[] newArray(int size) {
            return new AfterRefundNotification[size];
        }
    };

    public float getTotal() {
        return total;
    }

    public List<DishAndQuantity> getDishes() {
        return dishes;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setDishes(List<DishAndQuantity> dishes) {
        this.dishes = dishes;
    }
}
