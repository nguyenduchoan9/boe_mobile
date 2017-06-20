package com.nux.dhoan9.firstmvvm.viewmodel;

import android.databinding.ObservableField;
import com.nux.dhoan9.firstmvvm.model.OrderView;
import java.math.BigDecimal;

/**
 * Created by hoang on 13/06/2017.
 */

public class HistoryViewModel {
    public String listOrderId;
    public float total;
    public ObservableField<String> totalView = new ObservableField<>();
    public String orderDate;

    public HistoryViewModel(OrderView view) {
        this.listOrderId = view.getListOrderId();
        this.total = view.getTotal();
        this.orderDate = view.getOrderDate();
        totalView.set(String.valueOf(new BigDecimal(total)));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof HistoryViewModel) {
            HistoryViewModel other = (HistoryViewModel) obj;
            return other.listOrderId.equals(this.listOrderId);
        }
        return super.equals(obj);
    }


}
