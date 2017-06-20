package com.nux.dhoan9.firstmvvm.dependency.module;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.nux.dhoan9.firstmvvm.data.repo.CartRepo;
import com.nux.dhoan9.firstmvvm.data.repo.OrderRepo;
import com.nux.dhoan9.firstmvvm.dependency.scope.ActivityScope;
import com.nux.dhoan9.firstmvvm.dependency.scope.ForActivity;
import com.nux.dhoan9.firstmvvm.manager.CartManager;
import com.nux.dhoan9.firstmvvm.utils.ThreadScheduler;
import com.nux.dhoan9.firstmvvm.utils.support.ListBinder;
import com.nux.dhoan9.firstmvvm.view.adapter.HistoryAdapter;
import com.nux.dhoan9.firstmvvm.view.adapter.OrderAdapter;
import com.nux.dhoan9.firstmvvm.view.diffCallBack.CartItemCallBack;
import com.nux.dhoan9.firstmvvm.view.diffCallBack.HistoryDiffCalBack;
import com.nux.dhoan9.firstmvvm.viewmodel.CartItemListViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.CartItemViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.HistoryListViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.HistoryViewModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hoang on 12/05/2017.
 */
@Module
public class OrderModule {

    @Provides
    public CartItemCallBack provideCartItemCallBack() {
        return new CartItemCallBack();
    }

    @Provides
    public ListBinder<CartItemViewModel> provideCartItemListBinder(CartItemCallBack cartItemCallBack) {
        return new ListBinder<>(cartItemCallBack);
    }

    @Provides
    @ActivityScope
    public CartItemListViewModel provideCartItemListViewModel(@NonNull ListBinder<CartItemViewModel> listBinder,
                                                              @NonNull CartRepo cartRepo,
                                                              @NonNull Resources resources,
                                                              @NonNull ThreadScheduler threadScheduler,
                                                              @NonNull CartManager cartManager) {
        return new CartItemListViewModel(listBinder, cartRepo, resources, threadScheduler, cartManager);
    }

    @Provides
    @ActivityScope
    public OrderAdapter provideCartAdapter(@NonNull CartItemListViewModel cartItemListViewModel,
                                           @NonNull @ForActivity Context context) {
        return new OrderAdapter(context, cartItemListViewModel);
    }



    @Provides
    public HistoryDiffCalBack provideHistoryDiffCalBack() {
        return new HistoryDiffCalBack();
    }

    @Provides
    public ListBinder<HistoryViewModel> provideHistoryItemListBinder(HistoryDiffCalBack historyDiffCalBack) {
        return new ListBinder<>(historyDiffCalBack);
    }

    @Provides
    @ActivityScope
    public HistoryListViewModel provideHistoryListViewModel(@NonNull ThreadScheduler threadScheduler,
                                                            @NonNull Resources resources,
                                                            @NonNull ListBinder<HistoryViewModel> listBinder,
                                                            @NonNull OrderRepo orderRepo) {
        return new HistoryListViewModel(threadScheduler, resources, listBinder, orderRepo);
    }

    @Provides
    @ActivityScope
    public HistoryAdapter provideHistoryAdapter(@ForActivity Context context,
                                                HistoryListViewModel viewModel) {
        return new HistoryAdapter(context, viewModel);
    }

}
