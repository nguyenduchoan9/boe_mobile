package com.nux.dhoan9.firstmvvm.view.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.DishItemLayoutBinding;
import com.nux.dhoan9.firstmvvm.dependency.scope.ActivityScope;
import com.nux.dhoan9.firstmvvm.dependency.scope.ForActivity;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.view.activity.CustomerActivity;
import com.nux.dhoan9.firstmvvm.view.activity.DishDetailActivity;
import com.nux.dhoan9.firstmvvm.view.fragment.DescriptionDialogFragment;
import com.nux.dhoan9.firstmvvm.viewmodel.DishListViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.DishViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by hoang on 08/05/2017.
 */
@ActivityScope
public class DishListAdapter extends Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER || viewType == TYPE_FOOTER) {
            View view = inflater.inflate(R.layout.blank_8dp_layout, parent, false);
            BlankViewHolder viewHolder = new BlankViewHolder(view);

            return viewHolder;
        } else if (viewType == TYPE_ITEM) {
            DishItemLayoutBinding binding = DataBindingUtil
                    .inflate(inflater, R.layout.dish_item_layout, parent, false);
            return new DishViewHolder(binding);
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e("chocolate", String.valueOf(position));
        if (isPositionHeader(position)) {
            // nothing
            Log.e("chocolate-header", String.valueOf(position));
        } else if (isPositionFooter(position)) {
            // nothing
            Log.e("chocolate-footer", String.valueOf(position));
        } else {
            DishViewModel viewModel = getDishes().get(position - 1);
            ((DishViewHolder) holder).binding.setViewModel(viewModel);
            ((DishViewHolder) holder).binding.executePendingBindings();
            holder.itemView.setOnClickListener(v -> {
                boolean isUpperBound = viewModel.onOrderClick("");

                if (null != mListener) mListener.onOrderClick(!isUpperBound, viewModel.id);

            });
            holder.itemView.setOnLongClickListener(v -> {
                FragmentTransaction fm = ((CustomerActivity) mContext).getSupportFragmentManager().beginTransaction();
                DescriptionDialogFragment dialog = DescriptionDialogFragment.newInstance();
                dialog.setListener(new DescriptionDialogFragment.DescriptionDialogListener() {
                    @Override
                    public void onOrderClick(String description) {
                        boolean isUpperBound = viewModel.onOrderClick(description);
                        if (null != mListener) mListener.onOrderClick(!isUpperBound, viewModel.id);
                    }

                    @Override
                    public void onOrderClick() {
                        boolean isUpperBound = viewModel.onOrderClick("");
                        if (null != mListener) mListener.onOrderClick(!isUpperBound, viewModel.id);

                    }
                });
                dialog.show(fm, "description");
                return true;
            });

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        } else if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int pos) {
        return pos == 0;
    }

    private boolean isPositionFooter(int pos) {
        return pos == getItemCount() - 1;
    }

    private List<DishViewModel> getDishes() {
        return viewModel.getDishes();
    }

    @Override
    public int getItemCount() {
        return getDishes().size() + 2;
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

    public class BlankViewHolder extends RecyclerView.ViewHolder {
        public BlankViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OrderHandleListener {
        void onMaxOrderClick();

        void onOrderClick(boolean isMax, int dishId);
    }

    private OrderHandleListener mListener;

    public void setListener(OrderHandleListener mListener) {
        this.mListener = mListener;
    }

}
