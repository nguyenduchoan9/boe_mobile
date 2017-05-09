package com.nux.dhoan9.firstmvvm.view.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.DishItemLayoutBinding;
import com.nux.dhoan9.firstmvvm.dependency.scope.ActivityScope;
import com.nux.dhoan9.firstmvvm.dependency.scope.ForActivity;
import com.nux.dhoan9.firstmvvm.viewmodel.DishListViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.DishViewModel;

import java.util.List;

import javax.inject.Inject;
/**
 * Created by hoang on 08/05/2017.
 */
@ActivityScope
public class DishListAdapter extends Adapter<DishListAdapter.DishViewHolder> {
    private final DishListViewModel viewModel;
    private final LayoutInflater inflater;
    private Context mContext;

    @Inject
    public DishListAdapter(DishListViewModel viewModel, @ForActivity Context context) {
        this.viewModel = viewModel;
        this.inflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public DishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DishItemLayoutBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.dish_item_layout, parent, false);
        return new DishViewHolder(binding);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(DishViewHolder holder, int position) {
        DishViewModel viewModel = getDishes().get(position);
        holder.binding.tvName.setText(viewModel.name);
        holder.binding.tvPrice.setText(String.valueOf(viewModel.price));
        int drawable = viewModel.image.equals("chocolate_ball.jpg")
                ? R.drawable.chocolate_ball
                : R.drawable.chocolate_cake;
        Drawable drawable1 = mContext.getDrawable(drawable);
        int pixel = drawable1.getIntrinsicHeight();
        Glide.with(mContext)
                .load(drawable)
                .override(400, (int) (pixel/3))
                .placeholder(R.drawable.dish_placeholder)
                .crossFade()
                .into(holder.binding.ivImage);
        holder.binding.executePendingBindings();
    }

    private List<DishViewModel> getDishes() {
        return viewModel.getDishes();
    }

    @Override
    public int getItemCount() {
        return getDishes().size();
    }

    public class DishViewHolder extends RecyclerView.ViewHolder {
        final DishItemLayoutBinding binding;

        public DishViewHolder(DishItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            setupListeners();
        }

        private void setupListeners() {

        }
    }
}
