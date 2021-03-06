package com.nux.dhoan9.firstmvvm.viewmodel;

import android.databinding.ObservableField;
import com.nux.dhoan9.firstmvvm.manager.CartManager;
import com.nux.dhoan9.firstmvvm.model.Dish;
import com.nux.dhoan9.firstmvvm.utils.CurrencyUtil;

/**
 * Created by hoang on 08/05/2017.
 */

public class DishViewModel {
    public final int id;
    public final String name;
    public final float price;
    public final String image;
    public final String description;
    public ObservableField<Boolean> isOrder = new ObservableField<>(false);
    public ObservableField<String> priceView = new ObservableField<>();
    public ObservableField<String> quantityView = new ObservableField<>();
    private CartManager cartManager;

    public DishViewModel(Dish dish, CartManager cartManager) {
        this.id = dish.getId();
        this.name = dish.getName();
        this.image = dish.getImage();
        this.description = dish.getDescription();
        this.price = dish.getPrice();
        this.cartManager = cartManager;
        isOrder.set(isInCart());
        priceView.set(String.valueOf(CurrencyUtil.formatVNDecimal(price)));
    }

    public Dish toModel(DishViewModel viewModel) {
        return new Dish(id, name, description, price, image);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DishViewModel) {
            DishViewModel other = (DishViewModel) obj;
            return other.id == this.id;
        }
        return super.equals(obj);
    }

    private boolean isInCart() {
        return cartManager.isInCart(this.id);
    }

    private void removeOutOfCart() {
        cartManager.removeOutOfCart(this.id);
        isOrder.set(false);
    }

    private void addToCart() {
        cartManager.plus(this.id, 1, "");
        isOrder.set(true);
    }

    private void addToCart(String description) {
        cartManager.plus(this.id, 1, description);
        isOrder.set(true);
    }

    public boolean onOrderClick() {
        isOrder.set(false);
        if (!isInCart()) {
            addToCart();
            quantityView.set("1");
        } else {
            int previousQuantity = getQuantityInCart();
            if (4 == previousQuantity) {
                return false;
            }
            addToCart();
            quantityView.set("" + (previousQuantity + 1));
        }
        return true;
    }

    public boolean onOrderClick(String description) {
        isOrder.set(false);
        if (!isInCart()) {
            addToCart(description);
            quantityView.set("1");
        } else {
            int previousQuantity = getQuantityInCart();
            if (4 == previousQuantity) {
                return false;
            }
            addToCart(description);
            quantityView.set("" + (previousQuantity + 1));
        }
        return true;
    }

    private int getQuantityInCart() {
        return cartManager.getQuantityById(this.id);
    }

    public void syncCart() {
        isOrder.set(cartManager.isInCart(this.id));
    }
}
