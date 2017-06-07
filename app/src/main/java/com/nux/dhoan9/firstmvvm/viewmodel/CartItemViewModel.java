package com.nux.dhoan9.firstmvvm.viewmodel;

import android.databinding.ObservableField;

import com.google.android.gms.wallet.Cart;
import com.nux.dhoan9.firstmvvm.manager.CartManager;
import com.nux.dhoan9.firstmvvm.model.CartItem;
import com.nux.dhoan9.firstmvvm.model.Dish;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by hoang on 12/05/2017.
 */

public class CartItemViewModel extends DishViewModel {
    public int quantity;
    public ObservableField<String> quantityView = new ObservableField<>();
    public ObservableField<String> totalView = new ObservableField<>();
    private CartManager cartManager;

    public CartItemViewModel(Dish dish, int quantity, CartManager cartManager) {
        super(dish, cartManager);
        this.quantity = quantity;
        quantityView.set(String.valueOf(quantity));
        totalView.set(String.valueOf(price * quantity));
        this.cartManager = cartManager;
    }

    public CartItem toModel() {
        return new CartItem(new Dish(id, name, "des", price, image), quantity);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CartItemViewModel) {
            CartItemViewModel other = (CartItemViewModel) obj;
            return other.id == this.id;
        }
        return super.equals(obj);
    }

    public Observable<Oops> onPlusClick() {
        Oops oops = new Oops(id, price);
        if (15 == quantity) {
            oops.isUpperQuantityBound = true;
            return Observable.create(subscriber -> {
                subscriber.onNext(oops);
                subscriber.onCompleted();
            });
        }
        quantity += 1;
        quantityView.set(String.valueOf(quantity));
        totalView.set(String.valueOf(price * quantity));
        cartManager.plus(this.id, 1);
        return Observable.create(subscriber -> {
            subscriber.onNext(oops);
            subscriber.onCompleted();
        });
    }

    public Observable<Oops> onMinusClick() {
        Oops oops = new Oops(id, 0);
        if (1 == quantity) {
            oops.isLowerQuantityBound = true;
            return Observable.create(subscriber -> {
                subscriber.onNext(oops);
                subscriber.onCompleted();
            });
        }
        quantity -= 1;
        this.cartManager.minus(this.id, 1);
        quantityView.set(String.valueOf(quantity));
        totalView.set(String.valueOf(price * quantity));
        oops.price = price;
        return Observable.create(subscriber -> {
            subscriber.onNext(oops);
            subscriber.onCompleted();
        });
    }

    public Observable<Oops> onRemoveClick() {
        cartManager.removeOutOfCart(this.id);
        Oops oops = new Oops(id, price * quantity);
        return Observable.create(subscriber -> {
            subscriber.onNext(oops);
            subscriber.onCompleted();
        });
    }

    public class Oops {
        public int dishId;
        public float price;
        public boolean isLowerQuantityBound;
        public boolean isUpperQuantityBound;

        public Oops(int dishId, float price) {
            this.dishId = dishId;
            this.price = price;
            isLowerQuantityBound = false;
            isUpperQuantityBound = false;
        }
    }

//    public String getImage(){
//        return image;
//    }
}
