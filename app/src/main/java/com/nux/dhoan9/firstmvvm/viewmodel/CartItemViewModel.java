package com.nux.dhoan9.firstmvvm.viewmodel;

import android.databinding.ObservableField;

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

    public CartItemViewModel(Dish dish, int quantity) {
        super(dish);
        this.quantity = quantity;
        quantityView.set(String.valueOf(quantity));
        totalView.set(String.valueOf(price * quantity));
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

    public Observable<Float> onPlusClick() {
        quantity += 1;
        quantityView.set(String.valueOf(quantity));
        totalView.set(String.valueOf(price * quantity));
        return Observable.create(new Observable.OnSubscribe<Float>() {
            @Override
            public void call(Subscriber<? super Float> subscriber) {
                subscriber.onNext(price);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Float> onMinusClick() {
        if (0 == quantity) {
            return Observable.create(subscriber -> {
                subscriber.onNext(0F);
                subscriber.onCompleted();
            });
        }
        quantity -= 1;
        quantityView.set(String.valueOf(quantity));
        totalView.set(String.valueOf(price * quantity));
        return Observable.create(subscriber -> {
            subscriber.onNext(price);
            subscriber.onCompleted();
        });
    }
}
