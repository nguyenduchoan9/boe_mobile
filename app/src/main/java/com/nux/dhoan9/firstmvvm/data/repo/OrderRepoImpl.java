package com.nux.dhoan9.firstmvvm.data.repo;

import com.nux.dhoan9.firstmvvm.data.response.OrderResponse;
import com.nux.dhoan9.firstmvvm.services.DishServices;
import com.nux.dhoan9.firstmvvm.services.OrderServices;
import com.nux.dhoan9.firstmvvm.utils.RxUtils;
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
    public Observable<OrderResponse> makeOrder(Map<Integer, Integer> cart, int tableNumber) {
        StringBuilder cartParamsBuilder = new StringBuilder();
        for (Map.Entry<Integer, Integer> cartItem : cart.entrySet()) {
            cartParamsBuilder.append(String.valueOf(cartItem.getKey()))
                    .append("_")
                    .append(String.valueOf(cartItem.getValue()))
                    .append("_");
        }
        String orderParams = cartParamsBuilder.deleteCharAt(cartParamsBuilder.length() - 1).toString();
        return services.makeOrder(orderParams, tableNumber);
    }
}
