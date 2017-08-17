package com.nux.dhoan9.firstmvvm.data.repo;

import com.nux.dhoan9.firstmvvm.data.request.OrderBaseRequest;
import com.nux.dhoan9.firstmvvm.data.request.OrderPaypalRequest;
import com.nux.dhoan9.firstmvvm.data.response.CanOrder;
import com.nux.dhoan9.firstmvvm.data.response.StatusResponse;
import com.nux.dhoan9.firstmvvm.model.OrderCreateResponse;
import com.nux.dhoan9.firstmvvm.model.OrderInfo;
import com.nux.dhoan9.firstmvvm.model.OrderView;
import com.nux.dhoan9.firstmvvm.services.OrderServices;
import java.util.List;
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
    public Observable<OrderCreateResponse> makeOrder(OrderPaypalRequest request) {
        return services.makeOrder(request);
    }

    @Override
    public Observable<StatusResponse> makeOrderByCash(OrderBaseRequest request) {
        return services.makeOrderByCash(request);
    }

    @Override
    public Observable<StatusResponse> makeOrderByVoucher(OrderBaseRequest request) {
        return services.makeOrderByVouher(request);
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

    @Override
    public Observable<StatusResponse> partialRefundInCash(int orderId) {
        return services.partialRefundInCash(orderId);
    }

    @Override
    public Observable<StatusResponse> fullRefundInCash(int orderId) {
        return services.fullyRefundInCash(orderId);
    }

}
