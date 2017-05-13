package com.nux.dhoan9.firstmvvm.data.repo;

import com.nux.dhoan9.firstmvvm.data.request.CartItemRequest;
import com.nux.dhoan9.firstmvvm.model.CartItem;

import java.util.List;
/**
 * Created by hoang on 12/05/2017.
 */

public interface CartRepo {
    List<CartItem> getCart(List<CartItemRequest> cartItemRequests);
}
