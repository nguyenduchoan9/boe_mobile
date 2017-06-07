package com.nux.dhoan9.firstmvvm.viewmodel;

import android.support.annotation.NonNull;

import com.nux.dhoan9.firstmvvm.model.Category;
import com.nux.dhoan9.firstmvvm.model.Dish;

import java.util.List;
/**
 * Created by hoang on 09/05/2017.
 */

public class MenuCategoriesViewModel {
    public final DishListViewModel dishViewModels;
    public final Category category;


    public MenuCategoriesViewModel(@NonNull DishListViewModel viewModel,
                                   @NonNull Category category) {
        this.dishViewModels = viewModel;
        this.category = category;
    }

    public DishListViewModel getDishViewModels() {
        return dishViewModels;
    }

    public Category getCategory(){
        return category;
    }

    public void synCartInList(){
        dishViewModels.syncAllCart();
    }
}
