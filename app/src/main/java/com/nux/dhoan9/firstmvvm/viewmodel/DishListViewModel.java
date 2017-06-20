package com.nux.dhoan9.firstmvvm.viewmodel;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.nux.dhoan9.firstmvvm.data.repo.DishRepo;
import com.nux.dhoan9.firstmvvm.manager.CartManager;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.model.Dish;
import com.nux.dhoan9.firstmvvm.utils.ThreadScheduler;
import com.nux.dhoan9.firstmvvm.utils.support.ListBinder;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

/**
 * Created by hoang on 08/05/2017.
 */

public class DishListViewModel extends BaseViewModel {
    public final ListBinder<DishViewModel> listBinder;
    private final DishRepo dishRepo;
    private final List<DishViewModel> dishes = new ArrayList<>();
    private PublishSubject<Integer> scrollTo = PublishSubject.create();
    private final CartManager cartManager;

    public DishListViewModel(@NonNull ListBinder<DishViewModel> listBinder,
                             @NonNull DishRepo dishRepo,
                             @NonNull Resources resources,
                             @NonNull ThreadScheduler threadScheduler,
                             @NonNull CartManager cartManager) {
        super(threadScheduler, resources);
        this.listBinder = listBinder;
        this.dishRepo = dishRepo;
        this.cartManager = cartManager;
    }

    public Observable<Integer> scrollTo() {
        return scrollTo.observeOn(AndroidSchedulers.mainThread());
    }

    public ListBinder<DishViewModel> getDishListBinder() {
        return listBinder;
    }

    public List<DishViewModel> getDishes() {
        return dishes;
    }

    public void initialize(List<Dish> dishesItem) {
        dishes.addAll(dishesViewModel(dishesItem));
        listBinder.notifyDataChange(dishes);
    }

    public void removeAllData() {
        dishes.clear();
        listBinder.notifyDataChange(dishes);
    }

    public void initializeCategory(int idCategory, String keySearch) {
        dishRepo.getDishesByCategory(idCategory, keySearch)
                .compose(withScheduler())
                .subscribe(dishesResult -> {
                    dishes.addAll(dishesViewModel(dishesResult));
                    listBinder.notifyDataChange(dishes);
                });

    }

    public void syncAllCart() {
        if (null != dishes) {
            for (DishViewModel dishViewModel : dishes) {
                dishViewModel.syncCart();
            }
            listBinder.notifyDataChange(dishes);
        }
    }

    private List<DishViewModel> dishesViewModel(List<Dish> dishesData) {
        List<DishViewModel> dishViewModels = new ArrayList<>();
        for (Dish dish : dishesData) {
            dishViewModels.add(new DishViewModel(dish, cartManager));
        }

        return dishViewModels;
    }
}
