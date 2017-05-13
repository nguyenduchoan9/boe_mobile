package com.nux.dhoan9.firstmvvm.manager;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by hoang on 12/05/2017.
 */

public class CartManagerImpl implements CartManager {
    Map<Integer, Integer> cart;

    public CartManagerImpl() {
        this.cart = new HashMap<>();
    }

    @Override
    public void plus(int idDish) {
        if (cart.containsKey(idDish)) {
            int currentQuantity = cart.get(idDish);
            cart.replace(idDish, currentQuantity, currentQuantity + 1);
        } else {
            cart.put(idDish, 1);
        }

    }

    @Override
    public void minus(int idDish) {
        if (cart.containsKey(idDish)) {
            int currentQuantity = cart.get(idDish);
            if (1 == currentQuantity) {
                cart.remove(idDish);
                return;
            }
            cart.replace(idDish, currentQuantity, currentQuantity - 1);
        }
    }

    @Override
    public void clear() {
        cart.clear();
    }

    @Override
    public Map<Integer, Integer> getCart() {
        return cart;
    }
}
