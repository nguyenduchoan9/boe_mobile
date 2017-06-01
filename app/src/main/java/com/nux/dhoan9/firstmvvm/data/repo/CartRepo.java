package com.nux.dhoan9.firstmvvm.data.repo;

import android.content.Intent;
import com.nux.dhoan9.firstmvvm.data.request.CartItemRequest;
import com.nux.dhoan9.firstmvvm.model.CartItem;

import java.util.List;
import java.util.Map;
import rx.Observable;

/**
 * Created by hoang on 12/05/2017.
 */

public interface CartRepo {
    Observable<List<CartItem>> getCart(Map<Integer, Integer> cart);
}
