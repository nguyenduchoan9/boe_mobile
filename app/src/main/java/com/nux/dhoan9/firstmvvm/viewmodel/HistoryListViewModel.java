package com.nux.dhoan9.firstmvvm.viewmodel;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import com.nux.dhoan9.firstmvvm.data.repo.OrderRepo;
import com.nux.dhoan9.firstmvvm.model.OrderView;
import com.nux.dhoan9.firstmvvm.utils.RxUtils;
import com.nux.dhoan9.firstmvvm.utils.ThreadScheduler;
import com.nux.dhoan9.firstmvvm.utils.support.ListBinder;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by hoang on 13/06/2017.
 */

public class HistoryListViewModel extends BaseViewModel {
    private final ListBinder<HistoryViewModel> listBinder;
    private final OrderRepo orderRepo;
    private final List<HistoryViewModel> cartItems = new ArrayList<>();

    public HistoryListViewModel(@NonNull ThreadScheduler threadScheduler,
                                @NonNull Resources resources,
                                @NonNull ListBinder<HistoryViewModel> listBinder,
                                @NonNull OrderRepo orderRepo) {
        super(threadScheduler, resources);
        this.listBinder = listBinder;
        this.orderRepo = orderRepo;
    }

    public ListBinder<HistoryViewModel> getListBinder() {
        return listBinder;
    }

    public Observable<Boolean> initialize() {
        return orderRepo.getOrder()
                .compose(RxUtils.onProcessRequest())
                .observeOn(AndroidSchedulers.mainThread())
                .map(orderViews -> {
                    setData(orderViews);
                    if (orderViews.size() == 0) {
                        return false;
                    }
                    return true;
                });
    }

    private void setData(List<OrderView> list) {
        cartItems.clear();
        for (OrderView orderView : list){
            cartItems.add(new HistoryViewModel(orderView));
        }
        listBinder.notifyDataChange(cartItems);
    }

    public List<HistoryViewModel> getCartItems() {
        return cartItems;
    }

}
