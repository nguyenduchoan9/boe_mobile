package com.nux.dhoan9.firstmvvm.dependency.module;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.nux.dhoan9.firstmvvm.data.repo.DishRepo;
import com.nux.dhoan9.firstmvvm.dependency.scope.ActivityScope;
import com.nux.dhoan9.firstmvvm.dependency.scope.ForActivity;
import com.nux.dhoan9.firstmvvm.utils.ThreadScheduler;
import com.nux.dhoan9.firstmvvm.utils.support.ListBinder;
import com.nux.dhoan9.firstmvvm.view.adapter.DishesByCategoryAdapter;
import com.nux.dhoan9.firstmvvm.view.diffCallBack.DishDiffCallback;
import com.nux.dhoan9.firstmvvm.viewmodel.DishListViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.DishViewModel;

import dagger.Module;
import dagger.Provides;
/**
 * Created by hoang on 13/05/2017.
 */
@Module
public class DishByCategoryModule {
    private Activity activity;

    public DishByCategoryModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ForActivity
    @ActivityScope
    public Context provideContext() {
        return activity;
    }

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
    public DishListViewModel provideDishListViewModel(@NonNull ListBinder<DishViewModel> dishListBinder,
                                                      @NonNull DishRepo dishRepo,
                                                      @NonNull Resources resources,
                                                      @NonNull ThreadScheduler threadSchedulert) {
        return new DishListViewModel(dishListBinder, dishRepo, resources, threadSchedulert);
    }

    @Provides
    @ActivityScope
    public DishesByCategoryAdapter provideDishesByCategoryAdapter(@NonNull DishListViewModel viewModel,
                                                                  @ForActivity @NonNull Context context) {
        return new DishesByCategoryAdapter(viewModel, context);
    }
}
