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
    public void plus(int idDish, int quantity) {
        if (cart.containsKey(idDish)) {
            int currentQuantity = cart.get(idDish);
            cart.put(idDish, currentQuantity + 1);
        } else {
            cart.put(idDish, 1);
        }

    }

    @Override
    public void minus(int idDish, int quantity) {
        if (cart.containsKey(idDish)) {
            int currentQuantity = cart.get(idDish);
            if (1 == currentQuantity) {
                cart.remove(idDish);
                return;
            }
            cart.put(idDish, currentQuantity - 1);
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

    public int getItemTotal(){return cart.size();}

    @Override
    public boolean isInCart(int id) {
        return cart.containsKey(id);
    }

    @Override
    public void removeOutOfCart(int id) {
        cart.remove(id);
    }
}
