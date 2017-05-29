package com.nux.dhoan9.firstmvvm.data.repo;

import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.data.request.CartItemRequest;
import com.nux.dhoan9.firstmvvm.model.CartItem;
import com.nux.dhoan9.firstmvvm.model.Dish;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
/**
 * Created by hoang on 12/05/2017.
 */

public class CartRepoImpl implements CartRepo {
    Retrofit retrofit;

    public CartRepoImpl(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    @Override
    public List<CartItem> getCart(List<CartItemRequest> cartItemRequests) {
        List<CartItem> cartItems = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Dish dish = new Dish(i, "Cart - " + i,"description",  4202, "image");
            cartItems.add(new CartItem(dish, i + 1));
        }
        return cartItems;
    }
}
