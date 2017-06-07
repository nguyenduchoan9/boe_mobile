package com.nux.dhoan9.firstmvvm.data.repo;

import com.nux.dhoan9.firstmvvm.data.response.OrderResponse;
import java.util.Map;
import rx.Observable;

/**
 * Created by hoang on 05/06/2017.
 */

public interface OrderRepo {
    Observable<OrderResponse> makeOrder(Map<Integer, Integer> cart);
}
