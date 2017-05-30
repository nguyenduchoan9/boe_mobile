package com.nux.dhoan9.firstmvvm.dependency.module;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import com.nux.dhoan9.firstmvvm.data.repo.DishRepo;
import com.nux.dhoan9.firstmvvm.dependency.scope.ActivityScope;
import com.nux.dhoan9.firstmvvm.dependency.scope.ForActivity;
import com.nux.dhoan9.firstmvvm.manager.CartManager;
import com.nux.dhoan9.firstmvvm.utils.ThreadScheduler;
import com.nux.dhoan9.firstmvvm.utils.support.ListBinder;
import com.nux.dhoan9.firstmvvm.view.adapter.DishListAdapter;
import com.nux.dhoan9.firstmvvm.view.adapter.MenuCategoryListAdapter;
import com.nux.dhoan9.firstmvvm.view.diffCallBack.DishDiffCallback;
import com.nux.dhoan9.firstmvvm.view.diffCallBack.MenuDiffCallback;
import com.nux.dhoan9.firstmvvm.viewmodel.DishDetailViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.DishListViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.DishViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.MenuCateListViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.MenuCategoriesViewModel;
import javax.inject.Named;
import dagger.Module;
import dagger.Provides;

/**
 * Created by hoang on 08/05/2017.
 */
@Module
public class DishModule {

    @Provides
    public DishDiffCallback provideDishDiffCallback() {
        return new DishDiffCallback();
    }

    @Provides
    public MenuDiffCallback provideMenuDiffCallback() {
        return new MenuDiffCallback();
    }

    @Provides
    public ListBinder<DishViewModel> provideDishListBinder(DishDiffCallback dishDiffCallback) {
        return new ListBinder<>(dishDiffCallback);
    }

    @Provides
    public ListBinder<MenuCategoriesViewModel> provideMenuListBinder(MenuDiffCallback menuDiffCallback) {
        return new ListBinder<>(menuDiffCallback);
    }

    @Provides
    @ActivityScope
    public DishListViewModel provideDishListViewModel(@NonNull ListBinder<DishViewModel> dishListBinder,
                                                      @NonNull DishRepo dishRepo,
                                                      @NonNull Resources resources,
                                                      @NonNull ThreadScheduler threadSchedulert) {
        return new DishListViewModel(dishListBinder, dishRepo, resources, threadSchedulert);
    }

    @Provides
    @ActivityScope
    public DishListAdapter provideDishListAdapter(DishListViewModel dishListViewModel, @ForActivity Context context) {
        return new DishListAdapter(dishListViewModel, context);
    }

    @Provides
    @Named("cutlery")
    @ActivityScope
    public MenuCateListViewModel provideMenuCateListViewModelCutlery(@NonNull ThreadScheduler threadScheduler,
                                                                     @NonNull Resources resources,
                                                                     @NonNull ListBinder<DishViewModel> listBinder,
                                                                     @NonNull DishRepo dishRepo,
                                                                     @NonNull ListBinder<MenuCategoriesViewModel> menuListBinder) {
        return new MenuCateListViewModel(threadScheduler, resources, listBinder, dishRepo, menuListBinder);
    }

    @Provides
    @Named("drinking")
    @ActivityScope
    public MenuCateListViewModel provideMenuCateListViewModelDrinking(@NonNull ThreadScheduler threadScheduler,
                                                                      @NonNull Resources resources,
                                                                      @NonNull ListBinder<DishViewModel> listBinder,
                                                                      @NonNull DishRepo dishRepo,
                                                                      @NonNull ListBinder<MenuCategoriesViewModel> menuListBinder) {
        return new MenuCateListViewModel(threadScheduler, resources, listBinder, dishRepo, menuListBinder);
    }

    @Provides
    @Named("cutlery")
    @ActivityScope
    public MenuCategoryListAdapter provideMenuCategoryListAdapterCutlery(@Named("cutlery") MenuCateListViewModel menuCateListViewModel,
                                                                         @ForActivity Context context) {
        return new MenuCategoryListAdapter(menuCateListViewModel, context);
    }

    @Provides
    @Named("drinking")
    @ActivityScope
    public MenuCategoryListAdapter provideMenuCategoryListAdapterDrinking(@Named("drinking") MenuCateListViewModel menuCateListViewModel,
                                                                          @ForActivity Context context) {
        return new MenuCategoryListAdapter(menuCateListViewModel, context);
    }

    @Provides
    public DishDetailViewModel provideDishDetailViewmodel(@NonNull ThreadScheduler threadScheduler,
                                                          @NonNull Resources resources,
                                                          @NonNull DishRepo dishRepo,
                                                          @NonNull CartManager cartManager) {
        return new DishDetailViewModel(threadScheduler, resources, dishRepo, cartManager);
    }
}
