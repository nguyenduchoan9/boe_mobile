package com.nux.dhoan9.firstmvvm.services;

import com.nux.dhoan9.firstmvvm.data.response.CanOrder;
import com.nux.dhoan9.firstmvvm.data.response.OrderResponse;
import com.nux.dhoan9.firstmvvm.model.OrderInfo;
import com.nux.dhoan9.firstmvvm.model.OrderView;
import java.util.List;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hoang on 05/06/2017.
 */

public interface OrderServices {

    @POST("orders")
    @FormUrlEncoded
    Observable<OrderResponse> makeOrder(@Field("order") String orderParams,
                                        @Field("table_number") int tableNumber);

    @GET("orders")
    Observable<List<OrderView>> getOrder();

    @GET("orders/order_in_time")
    Observable<OrderInfo> getSepecificOrder(@Query("list_order") String listOrder);

    @GET("orders/is_in_time_order")
    Observable<CanOrder> canOrder();

}
