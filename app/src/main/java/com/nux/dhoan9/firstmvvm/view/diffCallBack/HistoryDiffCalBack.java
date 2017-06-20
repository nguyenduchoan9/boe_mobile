package com.nux.dhoan9.firstmvvm.view.diffCallBack;

import com.nux.dhoan9.firstmvvm.utils.support.DiffCallBack;
import com.nux.dhoan9.firstmvvm.viewmodel.HistoryViewModel;

/**
 * Created by hoang on 13/06/2017.
 */

public class HistoryDiffCalBack extends DiffCallBack<HistoryViewModel> {
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).listOrderId.equals(newList.get(newItemPosition).listOrderId);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        HistoryViewModel old = oldList.get(oldItemPosition);
        HistoryViewModel renew = newList.get(newItemPosition);

        return old.listOrderId.equals(renew.listOrderId)
                && old.orderDate.equals(renew.orderDate)
                && old.total == renew.total;
    }
}
