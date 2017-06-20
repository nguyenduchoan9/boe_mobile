package com.nux.dhoan9.firstmvvm.viewmodel;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.nux.dhoan9.firstmvvm.data.repo.CartRepo;
import com.nux.dhoan9.firstmvvm.data.request.CartItemRequest;
import com.nux.dhoan9.firstmvvm.manager.CartManager;
import com.nux.dhoan9.firstmvvm.model.CartItem;
import com.nux.dhoan9.firstmvvm.model.Dish;
import com.nux.dhoan9.firstmvvm.utils.ThreadScheduler;
import com.nux.dhoan9.firstmvvm.utils.support.ListBinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by hoang on 12/05/2017.
 */

public class CartItemListViewModel extends BaseViewModel {
    private final ListBinder<CartItemViewModel> listBinder;
    private final CartRepo cartRepo;
    private final List<CartItemViewModel> cartItems = new ArrayList<>();
    private final CartManager cartManager;

    public CartItemListViewModel(@NonNull ListBinder<CartItemViewModel> listBinder,
                                 @NonNull CartRepo cartRepo,
                                 @NonNull Resources resources,
                                 @NonNull ThreadScheduler threadScheduler,
                                 @NonNull CartManager cartManager) {
        super(threadScheduler, resources);
        this.listBinder = listBinder;
        this.cartRepo = cartRepo;
        this.cartManager = cartManager;
    }

    public ListBinder<CartItemViewModel> getListBinder() {
        return listBinder;
    }

    public List<CartItemViewModel> getCartItems() {
        return cartItems;
    }

    public Observable<Void> initialize() {
        cartItems.clear();
        listBinder.notifyDataChange(cartItems);
        if (0 == cartManager.getCart().size()) {
            return Observable.create(subscriber -> {
                subscriber.onNext(null);
                subscriber.onCompleted();
            });
        } else {
            return Observable.create(subscriber ->
                    cartRepo.getCart(getCartParams())
                            .debounce(300, TimeUnit.MILLISECONDS)
                            .compose(withScheduler())
                            .subscribe(dishCart -> {
                                cartItems.addAll(toCartItemViewModels(dishCart));
                                listBinder.notifyDataChange(cartItems);
                                subscriber.onNext(null);
                                subscriber.onCompleted();
                            })
            );
        }
    }

    private String getCartParams() {
        StringBuilder cartParamsBuilder = new StringBuilder();
        List<Integer> cartOrder = cartManager.getCartOrder();
        for (int i = 0; i < cartOrder.size(); i++) {
            int dishId = cartOrder.get(i);
            for (Map.Entry<Integer, Integer> cartItem : cartManager.getCart().entrySet()) {
                if (cartItem.getKey() == dishId) {
                    cartParamsBuilder.append(String.valueOf(cartItem.getKey()))
                            .append("_")
                            .append(String.valueOf(cartItem.getValue()))
                            .append("_");
                }
            }
        }
        String orderParams = cartParamsBuilder.deleteCharAt(cartParamsBuilder.length() - 1).toString();
        return orderParams;
    }

    private List<CartItemRequest> toCartItemRequests(Map<Integer, Integer> cart) {
        List<CartItemRequest> cartItemRequests = new ArrayList<>();
        for (Map.Entry<Integer, Integer> cartItem : cart.entrySet()) {
            cartItemRequests.add(new CartItemRequest(cartItem.getKey(), cartItem.getValue()));
        }
        return cartItemRequests;
    }

    private List<CartItemViewModel> toCartItemViewModels(List<CartItem> cartItems) {
        List<CartItemViewModel> cartItemViewModels = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Dish dish = new Dish(cartItem.getId(), cartItem.getName(),
                    cartItem.getDescription(), cartItem.getPrice(), cartItem.getImage());
            cartItemViewModels.add(new CartItemViewModel(dish, cartItem.getQuantity(), cartManager));
        }

        return cartItemViewModels;
    }

    public float remove(int pos) {
        CartItemViewModel item = cartItems.get(pos);
        float minus = item.price * item.quantity;
        cartManager.removeOutOfCart(item.id);
        cartItems.remove(pos);
        listBinder.notifyDataChange(cartItems);
        return minus;
    }
}

