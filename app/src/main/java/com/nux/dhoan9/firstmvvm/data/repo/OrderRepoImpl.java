package com.nux.dhoan9.firstmvvm.data.repo;

import com.nux.dhoan9.firstmvvm.data.response.CanOrder;
import com.nux.dhoan9.firstmvvm.data.response.CartDishAvailable;
import com.nux.dhoan9.firstmvvm.data.response.OrderResponse;
import com.nux.dhoan9.firstmvvm.data.response.StatusResponse;
import com.nux.dhoan9.firstmvvm.model.Dish;
import com.nux.dhoan9.firstmvvm.model.OrderCreateResponse;
import com.nux.dhoan9.firstmvvm.model.OrderInfo;
import com.nux.dhoan9.firstmvvm.model.OrderView;
import com.nux.dhoan9.firstmvvm.services.DishServices;
import com.nux.dhoan9.firstmvvm.services.OrderServices;
import com.nux.dhoan9.firstmvvm.utils.RxUtils;
import java.util.List;
import java.util.Map;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by hoang on 05/06/2017.
 */

public class OrderRepoImpl implements OrderRepo {
    private final String LOG_TAG = OrderRepoImpl.class.getSimpleName();
    Retrofit retrofit;
    OrderServices services;

    public OrderRepoImpl(Retrofit retrofit) {
        this.retrofit = retrofit;
        services = retrofit.create(OrderServices.class);
    }

    @Override
    public Observable<OrderCreateResponse> makeOrder(String cart, int tableNumber, String paymentId) {
        return services.makeOrder(cart, tableNumber, paymentId);
    }

    @Override
    public Observable<List<OrderView>> getOrder() {
        return services.getOrder();
    }

    @Override
    public Observable<OrderInfo> getOrderInfo(String orderId) {
        return services.getSepecificOrder(orderId);
    }

    @Override
    public Observable<CanOrder> isAvailable() {
        return services.canOrder();
    }

    @Override
    public Observable<StatusResponse> fullyRefund(int id) {
        return services.fullyRefund(id);
    }

    @Override
    public Observable<StatusResponse> partialRefund(int id, float totla, String dishList) {
        return services.partialRefund(id, totla, dishList);
    }

}
