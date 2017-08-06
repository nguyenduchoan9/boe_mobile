package com.nux.dhoan9.firstmvvm.data.repo;

import com.nux.dhoan9.firstmvvm.data.request.OrderBaseRequest;
import com.nux.dhoan9.firstmvvm.data.request.OrderPaypalRequest;
import com.nux.dhoan9.firstmvvm.data.response.CanOrder;
import com.nux.dhoan9.firstmvvm.data.response.StatusResponse;
import com.nux.dhoan9.firstmvvm.model.OrderCreateResponse;
import com.nux.dhoan9.firstmvvm.model.OrderInfo;
import com.nux.dhoan9.firstmvvm.model.OrderView;
import java.util.List;
import rx.Observable;

/**
 * Created by hoang on 05/06/2017.
 */

public interface OrderRepo {
    Observable<OrderCreateResponse> makeOrder(OrderPaypalRequest request);

    Observable<StatusResponse> makeOrderByCash(OrderBaseRequest request);

    Observable<StatusResponse> makeOrderByVoucher(OrderBaseRequest request);

    Observable<List<OrderView>> getOrder();

    Observable<OrderInfo> getOrderInfo(String orderId);

    Observable<CanOrder> isAvailable();

    Observable<StatusResponse> fullyRefund(int id);

    Observable<StatusResponse> partialRefund(int id, float totla, String dishList);
}
