package com.nux.dhoan9.firstmvvm.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.model.Dish;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hoang on 16/07/2017.
 */

public class DishNotServeAdapter extends RecyclerView.Adapter<DishNotServeAdapter.NotServeViewHolder> {
    List<Dish> dishes;
    LayoutInflater inflater;

    public DishNotServeAdapter(Context context) {
        this.dishes = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<Dish> data) {
        dishes = data;
        notifyDataSetChanged();
    }

    @Override
    public NotServeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.layout_dish_not_serve_item, parent, false);
        return new NotServeViewHolder(v);
    }

    private Dish getDishByPos(int pos) {
        return dishes.get(pos);
    }

    @Override
    public void onBindViewHolder(NotServeViewHolder holder, int position) {
        Dish dish = getDishByPos(position);
        holder.tvDishName.setText((position + 1) + " - " + dish.getName());
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    public class NotServeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvDishName)
        TextView tvDishName;

        public NotServeViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
