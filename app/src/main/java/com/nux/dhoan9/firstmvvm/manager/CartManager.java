package com.nux.dhoan9.firstmvvm.manager;

import java.util.Map;
/**
 * Created by hoang on 12/05/2017.
 */

public interface CartManager {
    void plus(int dishId, int quantity);

    void minus(int dishId, int quantity);

    int getItamTotal();

    void clear();

    Map<Integer, Integer> getCart();
}
