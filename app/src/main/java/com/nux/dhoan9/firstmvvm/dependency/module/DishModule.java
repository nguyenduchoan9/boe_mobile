package com.nux.dhoan9.firstmvvm.dependency.module;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.nux.dhoan9.firstmvvm.data.repo.DishRepo;
import com.nux.dhoan9.firstmvvm.data.repo.TodoRepo;
import com.nux.dhoan9.firstmvvm.dependency.scope.ActivityScope;
import com.nux.dhoan9.firstmvvm.dependency.scope.ForActivity;
import com.nux.dhoan9.firstmvvm.utils.ThreadScheduler;
import com.nux.dhoan9.firstmvvm.utils.support.ListBinder;
import com.nux.dhoan9.firstmvvm.view.adapter.DishListAdapter;
import com.nux.dhoan9.firstmvvm.view.adapter.MenuCategoryListAdapter;
import com.nux.dhoan9.firstmvvm.view.adapter.TodoListAdapter;
import com.nux.dhoan9.firstmvvm.view.diffCallBack.DishDiffCallback;
import com.nux.dhoan9.firstmvvm.view.diffCallBack.TodoDiffCallBack;
import com.nux.dhoan9.firstmvvm.viewmodel.DishListViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.DishViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.MenuCateListViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.TodoListViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.TodoViewModel;

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
    public ListBinder<DishViewModel> provideDishListBinder(DishDiffCallback dishDiffCallback) {
        return new ListBinder<>(dishDiffCallback);
    }

    @Provides
    @ActivityScope
    public DishListViewModel provideDishListViewModel(ListBinder<DishViewModel> dishListBinder, DishRepo dishRepo) {
        return new DishListViewModel(dishListBinder, dishRepo);
    }

    @Provides
    @ActivityScope
    public DishListAdapter provideDishListAdapter(DishListViewModel dishListViewModel, @ForActivity Context context) {
        return new DishListAdapter(dishListViewModel, context);
    }

    @Provides
    @ActivityScope
    public MenuCateListViewModel provideMenuCateListViewModel(@NonNull ThreadScheduler threadScheduler,
                                                              @NonNull Resources resources,
                                                              @NonNull ListBinder<DishViewModel> listBinder,
                                                              @NonNull DishRepo dishRepo) {
        return new MenuCateListViewModel(threadScheduler, resources, listBinder, dishRepo);
    }

    @Provides
    @ActivityScope
    public MenuCategoryListAdapter provideMenuCategoryListAdapter(MenuCateListViewModel menuCateListViewModel,
                                                                  @ForActivity Context context) {
        return new MenuCategoryListAdapter(menuCateListViewModel, context);
    }
}