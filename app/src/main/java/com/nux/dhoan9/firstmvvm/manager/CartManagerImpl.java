package com.nux.dhoan9.firstmvvm.manager;

import android.content.Intent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by hoang on 12/05/2017.
 */

public class CartManagerImpl implements CartManager {
    Map<Integer, Integer> cart;
    List<Integer> listOrder;

    public CartManagerImpl() {
        this.cart = new HashMap<>();
        listOrder = new ArrayList<>();
    }

    @Override
    public void plus(int idDish, int quantity) {
        if (cart.containsKey(idDish)) {
            int currentQuantity = cart.get(idDish);
            cart.put(idDish, currentQuantity + 1);
        } else {
            cart.put(idDish, 1);
            listOrder.add(idDish);
        }

    }

    @Override
    public void minus(int idDish, int quantity) {
        if (cart.containsKey(idDish)) {
            int currentQuantity = cart.get(idDish);
            if (1 == currentQuantity) {
                return;
            }
            cart.put(idDish, currentQuantity - 1);
        }
    }

    @Override
    public void clear() {
        cart.clear();
        listOrder.clear();
    }

    @Override
    public Map<Integer, Integer> getCart() {
        return cart;
    }

    @Override
    public List<Integer> getCartOrder() {
        return listOrder;
    }

    public int getItemTotal() {
        return cart.size();
    }

    @Override
    public boolean isInCart(int id) {
        return cart.containsKey(id);
    }

    @Override
    public void removeOutOfCart(int id) {
        cart.remove(id);
        syncListOrder(id);
    }

    private void syncListOrder(int id) {
        int pos = -1;
        for (int i = 0; i < listOrder.size(); i++) {
            if (listOrder.get(i) == id) {
                pos = i;
                break;
            }
        }
        if (-1 != pos) {
            listOrder.remove(pos);
        }
    }
}
