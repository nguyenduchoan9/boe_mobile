package com.nux.dhoan9.firstmvvm.manager;

import java.util.List;
import java.util.Map;

/**
 * Created by hoang on 12/05/2017.
 */

public interface CartManager {
    void plus(int dishId, int quantity);

    void plus(int dishId, int quantity, String description);

    void minus(int dishId, int quantity);

    int getItemTotal();

    boolean isInCart(int id);

    void removeOutOfCart(int id);

    void clear();

    Map<Integer, Integer> getCart();

    List<Integer> getCartOrder();

    int getQuantityById(int dishId);

    float getTotal();

    boolean isHaveDescription(int dishId);

    List<String> getDescriptionByDishId(int dishId);

    void setDescriptionByDishId(int dishId, List<String> descriptions);

    Map<Integer, List<String>> getAllDEscription();
}
