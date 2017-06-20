package com.nux.dhoan9.firstmvvm.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.model.OrderInfoItem;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by hoang on 14/06/2017.
 */

public class OrderInfoAdapter extends RecyclerView.Adapter<OrderInfoAdapter.OrderViewHolder> {
    List<OrderInfoItem> list;
    LayoutInflater layoutInflater;

    public OrderInfoAdapter(Context context, List<OrderInfoItem> list) {
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.layout_dialog_order_info_item, parent, false);
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        OrderInfoItem item = list.get(position);
        float total = item.getPrice() * item.getQuantity();
        holder.tvDate.setText(item.getCreateAt());
        holder.tvTotal.setText(String.valueOf(new BigDecimal(item.getPrice()))
                + " x "
                + String.valueOf(item.getQuantity())
                + " = "
                + String.valueOf(String.valueOf(new BigDecimal(total)))
                + " VND");
        holder.tvDishName.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvDishName;
        TextView tvTotal;
        TextView tvDate;

        public OrderViewHolder(View itemView) {
            super(itemView);
            tvDishName = (TextView) itemView.findViewById(R.id.tvDishName);
            tvTotal = (TextView) itemView.findViewById(R.id.tvTotal);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
        }
    }
}
