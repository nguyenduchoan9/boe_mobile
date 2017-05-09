package com.nux.dhoan9.firstmvvm.viewmodel;

import android.support.annotation.NonNull;

import com.nux.dhoan9.firstmvvm.data.repo.DishRepo;
import com.nux.dhoan9.firstmvvm.model.Dish;
import com.nux.dhoan9.firstmvvm.utils.support.ListBinder;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;
/**
 * Created by hoang on 08/05/2017.
 */

public class DishListViewModel {
    private final ListBinder<DishViewModel> listBinder;
    private final DishRepo dishRepo;
    private final List<DishViewModel> dishes = new ArrayList<>();
    private PublishSubject<Integer> scrollTo = PublishSubject.create();

    public DishListViewModel(@NonNull ListBinder<DishViewModel> listBinder,
                             @NonNull DishRepo dishRepo) {
        this.listBinder = listBinder;
        this.dishRepo = dishRepo;
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

    public void initialize() {
        dishes.addAll(dishesViewModel(dishRepo.getDishes()));
        listBinder.notifyDataChange(dishes);
    }

    private List<DishViewModel> dishesViewModel(List<Dish> dishesData) {
        List<DishViewModel> dishViewModels = new ArrayList<>();
        for (Dish dish : dishesData) {
            dishViewModels.add(new DishViewModel(dish));
        }

        return dishViewModels;
    }
}
