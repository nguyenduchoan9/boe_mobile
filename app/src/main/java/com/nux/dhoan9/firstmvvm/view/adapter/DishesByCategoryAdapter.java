package com.nux.dhoan9.firstmvvm.view.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.DishByCategoryItemBinding;
import com.nux.dhoan9.firstmvvm.dependency.scope.ActivityScope;
import com.nux.dhoan9.firstmvvm.dependency.scope.ForActivity;
import com.nux.dhoan9.firstmvvm.utils.support.ListBinder;
import com.nux.dhoan9.firstmvvm.view.activity.DishDetailActivity;
import com.nux.dhoan9.firstmvvm.viewmodel.DishListViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.DishViewModel;

import java.util.List;

import javax.inject.Inject;
/**
 * Created by hoang on 12/05/2017.
 */
@ActivityScope
public class DishesByCategoryAdapter extends RecyclerView.Adapter<DishesByCategoryAdapter.DishByCategoryViewHolder> {
    private final DishListViewModel viewModel;
    private final LayoutInflater inflater;
    private Context mContext;

    @Inject
    public DishesByCategoryAdapter(@NonNull DishListViewModel viewModel,
                                   @ForActivity @NonNull Context context) {
        this.viewModel = viewModel;
        inflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public DishByCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DishByCategoryItemBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.dish_by_category_item, parent, false);
        return new DishByCategoryViewHolder(binding);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(DishByCategoryViewHolder holder, int position) {
        DishViewModel viewModelItem = getDishes().get(position);
        holder.binding.setViewModel(viewModelItem);
        holder.binding.executePendingBindings();
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, DishDetailActivity.class);
            intent.putExtra(DishDetailActivity.DISH_ID, viewModelItem.id);
            ActivityOptions ops = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext,
                    Pair.create(holder.binding.ivCover,
                            holder.itemView.getContext().getString(R.string.item_image_transition))
            );
            mContext.startActivity(intent, ops.toBundle());
        });
    }

    private List<DishViewModel> getDishes() {
        return viewModel.getDishes();
    }

    public ListBinder<DishViewModel> getListBinder() {
        return viewModel.getDishListBinder();
    }

    @Override
    public int getItemCount() {
        return getDishes().size();
    }

    public class DishByCategoryViewHolder extends RecyclerView.ViewHolder {
        DishByCategoryItemBinding binding;

        public DishByCategoryViewHolder(DishByCategoryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
