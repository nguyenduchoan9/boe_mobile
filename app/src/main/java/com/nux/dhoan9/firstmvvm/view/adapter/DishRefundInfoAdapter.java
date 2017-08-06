package com.nux.dhoan9.firstmvvm.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.data.response.AfterRefundNotification;
import com.nux.dhoan9.firstmvvm.data.response.AfterRefundNotification.DishAndQuantity;
import com.nux.dhoan9.firstmvvm.model.Dish;
import com.nux.dhoan9.firstmvvm.utils.CurrencyUtil;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hoang on 16/07/2017.
 */

public class DishRefundInfoAdapter extends RecyclerView.Adapter<DishRefundInfoAdapter.NotServeViewHolder> {
    List<DishAndQuantity> dishes;
    LayoutInflater inflater;

    public DishRefundInfoAdapter(Context context) {
        this.dishes = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<DishAndQuantity> data) {
        dishes = data;
        notifyDataSetChanged();
    }

    @Override
    public NotServeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.layout_dish_refund_item, parent, false);
        return new NotServeViewHolder(v);
    }

    private DishAndQuantity getDishByPos(int pos) {
        return dishes.get(pos);
    }

    @Override
    public void onBindViewHolder(NotServeViewHolder holder, int position) {
        DishAndQuantity dish = getDishByPos(position);
        holder.tvDishName.setText((position + 1) + " - " + dish.getDishName());
        String dishInfo = CurrencyUtil.formatVNDecimal(dish.getPrice())
                + " x "
                + dish.getQuantity()
                + " = "
                + CurrencyUtil.formatVNDecimal(dish.getPrice() * dish.getQuantity());
        holder.tvInfoDish.setText(dishInfo);
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    public class NotServeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvDishName)
        TextView tvDishName;
        @BindView(R.id.tvInfoDish)
        TextView tvInfoDish;

        public NotServeViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
