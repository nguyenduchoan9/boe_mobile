package com.nux.dhoan9.firstmvvm.data.repo;

import android.content.Intent;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.data.request.CartItemRequest;
import com.nux.dhoan9.firstmvvm.model.CartDishItem;
import com.nux.dhoan9.firstmvvm.model.CartItem;
import com.nux.dhoan9.firstmvvm.model.Dish;
import com.nux.dhoan9.firstmvvm.services.CartServices;
import com.nux.dhoan9.firstmvvm.services.DishServices;
import com.nux.dhoan9.firstmvvm.utils.RxUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by hoang on 12/05/2017.
 */

public class CartRepoImpl implements CartRepo {
    Retrofit retrofit;
    CartServices services;

    public CartRepoImpl(Retrofit retrofit) {
        this.retrofit = retrofit;
        services = retrofit.create(CartServices.class);
    }

    @Override
    public Observable<List<CartItem>> getCart(Map<Integer, Integer> cart) {
        StringBuilder cartParamsBuilder = new StringBuilder();
        for(Map.Entry<Integer, Integer> cartItem : cart.entrySet()){
            cartParamsBuilder.append(String.valueOf(cartItem.getKey()))
                    .append("_")
                    .append(String.valueOf(cartItem.getValue()))
                    .append("_");
        }
        String cartParams = cartParamsBuilder.deleteCharAt(cartParamsBuilder.length() - 1).toString();
        return Observable.create(subscriber -> {
            services.getDishAsCart(cartParams)
                    .compose(RxUtils.onProcessRequest())
                    .subscribe(new Subscriber<List<CartDishItem>>() {
                        @Override
                        public void onCompleted() {
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onError(Throwable e) {
                            subscriber.onError(e);
                        }

                        @Override
                        public void onNext(List<CartDishItem> cartDishItems) {
                            subscriber.onNext(toListCartItem(cartDishItems));
                        }
                    });
        });
    }

    private CartItem toCartItem(CartDishItem dishItem) {
        return new CartItem(dishItem.dish, dishItem.quantity);
    }

    private List<CartItem> toListCartItem(List<CartDishItem> cartDishItems) {
        List<CartItem> cartItemList = new ArrayList<>();
        for (CartDishItem c : cartDishItems) {
            cartItemList.add(toCartItem(c));
        }
        return cartItemList;
    }


}
