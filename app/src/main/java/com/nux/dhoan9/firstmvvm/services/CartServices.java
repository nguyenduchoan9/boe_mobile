package com.nux.dhoan9.firstmvvm.services;

import com.nux.dhoan9.firstmvvm.model.CartDishItem;
import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hoang on 01/06/2017.
 */

public interface CartServices {

    @GET("dishes/as_cart")
    Observable<List<CartDishItem>> getDishAsCart(@Query("cart") String cart);
}
