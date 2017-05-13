package com.nux.dhoan9.firstmvvm.manager;

import java.util.Map;
/**
 * Created by hoang on 12/05/2017.
 */

public interface CartManager {
    void plus(int idDish);

    void minus(int idDish);

    void clear();

    Map<Integer, Integer> getCart();
}
