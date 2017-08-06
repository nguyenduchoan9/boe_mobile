package com.nux.dhoan9.firstmvvm.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hoang on 12/05/2017.
 */

public class CartManagerImpl implements CartManager {
    Map<Integer, Integer> cart;
    List<Integer> listOrder;
    Map<Integer, List<String>> descriptionMap;

    public CartManagerImpl() {
        this.cart = new HashMap<>();
        listOrder = new ArrayList<>();
        descriptionMap = new HashMap<>();
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
    public void plus(int idDish, int quantity, String description) {
        if (cart.containsKey(idDish)) {
            int currentQuantity = cart.get(idDish);
            cart.put(idDish, currentQuantity + 1);
        } else {
            cart.put(idDish, 1);
            listOrder.add(idDish);
        }

        if (descriptionMap.containsKey(idDish)) {
            List<String> previousDes = descriptionMap.get(idDish);
            previousDes.add(description);
            descriptionMap.put(idDish, previousDes);
        } else {
            List<String> newDes = new ArrayList<>();
            newDes.add(description);
            descriptionMap.put(idDish, newDes);
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
        descriptionMap.clear();
    }

    @Override
    public Map<Integer, Integer> getCart() {
        return cart;
    }

    @Override
    public List<Integer> getCartOrder() {
        return listOrder;
    }

    @Override
    public int getQuantityById(int dishId) {
        int pos = -1;
        if (cart.containsKey(dishId)) {
            pos = cart.get(dishId);
        }
        return pos;
    }

    @Override
    public float getTotal() {
        if (null != cart) {
            float total = 0;
            for (Map.Entry<Integer, Integer> item : cart.entrySet()) {
                total += item.getValue();
            }
            return total;
        }
        return 0;
    }

    @Override
    public boolean isHaveDescription(int dishId) {
        return descriptionMap.containsKey(dishId);
    }

    @Override
    public List<String> getDescriptionByDishId(int dishId) {
        if (descriptionMap.containsKey(dishId)) {
            return descriptionMap.get(dishId);
        }
        return null;
    }

    @Override
    public void setDescriptionByDishId(int dishId, List<String> descriptions) {
        if (descriptions.size() == 0) {
            return;
        }
        if (descriptionMap.containsKey(dishId)) {
            descriptionMap.put(dishId, descriptions);
        }
    }

    @Override
    public Map<Integer, List<String>> getAllDEscription() {
        return descriptionMap;
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
        if (cart.containsKey(id))
            cart.remove(id);
        if (descriptionMap.containsKey(id))
            descriptionMap.remove(id);
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
