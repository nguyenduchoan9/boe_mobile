package com.nux.dhoan9.firstmvvm.viewmodel;

import android.content.res.Resources;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import com.nux.dhoan9.firstmvvm.data.repo.DishRepo;
import com.nux.dhoan9.firstmvvm.manager.CartManager;
import com.nux.dhoan9.firstmvvm.model.Dish;
import com.nux.dhoan9.firstmvvm.utils.ThreadScheduler;
import rx.Observable;

/**
 * Created by hoang on 28/05/2017.
 */

public class DishDetailViewModel extends BaseViewModel {
    public int id;
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> description = new ObservableField<>();
    public ObservableField<String> price = new ObservableField<>();
    public ObservableField<String> image = new ObservableField<>();
    public ObservableField<Boolean> isOrder = new ObservableField<>(false);
    private final DishRepo dishRepo;
    private final CartManager cartManager;

    public DishDetailViewModel(@NonNull ThreadScheduler threadScheduler,
                               @NonNull Resources resources,
                               @NonNull DishRepo dishRepo,
                               @NonNull CartManager cartManager) {
        super(threadScheduler, resources);
        this.dishRepo = dishRepo;
        this.cartManager = cartManager;
    }

    public Observable<Dish> getDishDetail(int id) {
        this.id = id;
        return dishRepo.getDishDetail(id)
                .compose(withScheduler())
                .doOnNext(dish -> getDishInfo(dish));
    }

    public void getDishInfo(Dish dish) {
        this.name.set(dish.getName());
        this.description.set(dish.getDescription());
        this.price.set(String.valueOf(dish.getPrice()));
        this.image.set(dish.getImage());
        isOrder.set(cartManager.isInCart(dish.getId()));
    }

    public void onOrderClick(){
        cartManager.plus(id, 1);
    }

    public void onCancelClick(){
        cartManager.removeOutOfCart(id);
    }
}
