package com.nux.dhoan9.firstmvvm.services;

import com.nux.dhoan9.firstmvvm.data.response.OrderResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by hoang on 05/06/2017.
 */

public interface OrderServices {

    @POST("orders")
    @FormUrlEncoded
    Observable<OrderResponse> makeOrder(@Field("order") String orderParams,
                                        @Field("table_number") int tableNumber);

    @POST
    Call<OrderResponse> makeORder();
}
