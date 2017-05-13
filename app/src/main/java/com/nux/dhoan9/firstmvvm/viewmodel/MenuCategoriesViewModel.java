package com.nux.dhoan9.firstmvvm.viewmodel;

import android.support.annotation.NonNull;

import com.nux.dhoan9.firstmvvm.model.Dish;

import java.util.List;
/**
 * Created by hoang on 09/05/2017.
 */

public class MenuCategoriesViewModel {
    public final DishListViewModel dishViewModels;
    public final String title;


    public MenuCategoriesViewModel(@NonNull DishListViewModel viewModel,
                                   @NonNull String title) {
        this.dishViewModels = viewModel;
        this.title = title;
    }

    public DishListViewModel getDishViewModels() {
        return dishViewModels;
    }
}
