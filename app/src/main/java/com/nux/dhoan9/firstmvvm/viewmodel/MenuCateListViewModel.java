package com.nux.dhoan9.firstmvvm.viewmodel;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.nux.dhoan9.firstmvvm.data.repo.DishRepo;
import com.nux.dhoan9.firstmvvm.model.MenuCategories;
import com.nux.dhoan9.firstmvvm.utils.ThreadScheduler;
import com.nux.dhoan9.firstmvvm.utils.support.ListBinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoang on 09/05/2017.
 */

public class MenuCateListViewModel extends BaseViewModel {
    private List<MenuCategoriesViewModel> menuCategoriesViewModels;
    private final ListBinder<DishViewModel> listBinder;
    private final DishRepo dishRepo;

    public MenuCateListViewModel(@NonNull ThreadScheduler threadScheduler,
                                 @NonNull Resources resources,
                                 @NonNull ListBinder<DishViewModel> listBinder,
                                 @NonNull DishRepo dishRepo) {
        super(threadScheduler, resources);
        this.listBinder = listBinder;
        this.dishRepo = dishRepo;
    }

    public void initialize() {
        menuCategoriesViewModels = new ArrayList<>();
        dishRepo.getMenu()
                .compose(withScheduler())
                .subscribe(menu -> {
                    for (MenuCategories menuCategories : menu) {
                        DishListViewModel dishListViewModel = new DishListViewModel(listBinder, dishRepo, resources, threadScheduler);
                        MenuCategoriesViewModel menuCategoriesViewModel =
                                new MenuCategoriesViewModel(dishListViewModel, menuCategories.getCategory());

                        menuCategoriesViewModels.add(menuCategoriesViewModel);
                        dishListViewModel.initialize(menuCategories.getDish());
                    }
                });

    }

    public void initializeDrinking() {
        menuCategoriesViewModels = new ArrayList<>();
        dishRepo.getMenuDrinking()
                .compose(withScheduler())
                .subscribe(menu -> {
                    for (MenuCategories menuCategories : menu) {
                        DishListViewModel dishListViewModel = new DishListViewModel(listBinder, dishRepo, resources, threadScheduler);
                        MenuCategoriesViewModel menuCategoriesViewModel =
                                new MenuCategoriesViewModel(dishListViewModel, menuCategories.getCategory());

                        menuCategoriesViewModels.add(menuCategoriesViewModel);
                        dishListViewModel.initialize(menuCategories.getDish());
                    }
                });

    }

    public MenuCategoriesViewModel getPosition(int pos) {
        return menuCategoriesViewModels.get(pos);
    }

    public int getSize() {
        return menuCategoriesViewModels.size();
    }
}
