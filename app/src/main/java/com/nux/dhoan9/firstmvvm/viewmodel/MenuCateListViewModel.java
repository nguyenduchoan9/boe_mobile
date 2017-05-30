package com.nux.dhoan9.firstmvvm.viewmodel;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import com.nux.dhoan9.firstmvvm.data.repo.DishRepo;
import com.nux.dhoan9.firstmvvm.model.MenuCategories;
import com.nux.dhoan9.firstmvvm.utils.ThreadScheduler;
import com.nux.dhoan9.firstmvvm.utils.support.ListBinder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;

/**
 * Created by hoang on 09/05/2017.
 */

public class MenuCateListViewModel extends BaseViewModel {
    private List<MenuCategoriesViewModel> menuCategoriesViewModels;
    private final ListBinder<DishViewModel> listBinder;
    private final ListBinder<MenuCategoriesViewModel> menuListBinder;
    private final DishRepo dishRepo;

    public MenuCateListViewModel(@NonNull ThreadScheduler threadScheduler,
                                 @NonNull Resources resources,
                                 @NonNull ListBinder<DishViewModel> listBinder,
                                 @NonNull DishRepo dishRepo,
                                 @NonNull ListBinder<MenuCategoriesViewModel> menuListBinder) {
        super(threadScheduler, resources);
        this.listBinder = listBinder;
        this.dishRepo = dishRepo;
        this.menuListBinder = menuListBinder;
    }

    public Observable<Void> initialize(boolean isRefresh) {
        if (isRefresh) {
            for (MenuCategoriesViewModel menu : menuCategoriesViewModels) {
                menu.dishViewModels.removeAllData();
            }
            menuCategoriesViewModels.clear();
            menuListBinder.notifyDataChange(menuCategoriesViewModels);
        } else {
            menuCategoriesViewModels = new ArrayList<>();
        }
        return Observable.create(subscriber ->
                dishRepo.getMenu()
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .compose(withScheduler())
                        .subscribe(menu -> {
                            for (MenuCategories menuCategories : menu) {
                                DishListViewModel dishListViewModel = new DishListViewModel(listBinder, dishRepo, resources, threadScheduler);
                                MenuCategoriesViewModel menuCategoriesViewModel =
                                        new MenuCategoriesViewModel(dishListViewModel, menuCategories.getCategory());

                                menuCategoriesViewModels.add(menuCategoriesViewModel);
                                dishListViewModel.initialize(menuCategories.getDish());
                            }
                            menuListBinder.notifyDataChange(menuCategoriesViewModels);
                            subscriber.onNext(null);
                            subscriber.onCompleted();
                        }));
    }

    public Observable<Void> initializeDrinking(boolean isRefresh) {
        if (isRefresh) {
            for (MenuCategoriesViewModel menu : menuCategoriesViewModels) {
                menu.dishViewModels.removeAllData();
            }
            menuCategoriesViewModels.clear();
            menuListBinder.notifyDataChange(menuCategoriesViewModels);
        } else {
            menuCategoriesViewModels = new ArrayList<>();
        }
        return Observable.create(subscriber -> dishRepo.getMenuDrinking()
                .compose(withScheduler())
                .subscribe(menu -> {
                    for (MenuCategories menuCategories : menu) {
                        DishListViewModel dishListViewModel = new DishListViewModel(listBinder, dishRepo, resources, threadScheduler);
                        MenuCategoriesViewModel menuCategoriesViewModel =
                                new MenuCategoriesViewModel(dishListViewModel, menuCategories.getCategory());

                        menuCategoriesViewModels.add(menuCategoriesViewModel);
                        dishListViewModel.initialize(menuCategories.getDish());
                    }
                    menuListBinder.notifyDataChange(menuCategoriesViewModels);
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                }));

    }

    public MenuCategoriesViewModel getFirstItem() {
        if (null != menuCategoriesViewModels) {
            return menuCategoriesViewModels.get(0);
        }
        return null;
    }

    public ListBinder<DishViewModel> getListBinder() {
        return listBinder;
    }

    public ListBinder<MenuCategoriesViewModel> getMenuListBinder() {
        return menuListBinder;
    }

    public MenuCategoriesViewModel getPosition(int pos) {
        return menuCategoriesViewModels.get(pos);
    }

    public int getSize() {
        return menuCategoriesViewModels.size();
    }
}
