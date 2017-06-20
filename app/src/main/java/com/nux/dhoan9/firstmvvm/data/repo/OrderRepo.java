package com.nux.dhoan9.firstmvvm.data.repo;

import com.nux.dhoan9.firstmvvm.data.response.CanOrder;
import com.nux.dhoan9.firstmvvm.data.response.OrderResponse;
import com.nux.dhoan9.firstmvvm.model.OrderInfo;
import com.nux.dhoan9.firstmvvm.model.OrderView;
import java.util.List;
import java.util.Map;
import rx.Observable;

/**
 * Created by hoang on 05/06/2017.
 */

public interface OrderRepo {
    Observable<OrderResponse> makeOrder(String cart, int tableNumber);
    Observable<List<OrderView>> getOrder();
    Observable<OrderInfo> getOrderInfo(String orderId);
    Observable<CanOrder> isAvailable();
}
