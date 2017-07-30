package com.nux.dhoan9.firstmvvm.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.DishCartItemBinding;
import com.nux.dhoan9.firstmvvm.dependency.scope.ActivityScope;
import com.nux.dhoan9.firstmvvm.dependency.scope.ForActivity;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.view.activity.DishDetailActivity;
import com.nux.dhoan9.firstmvvm.viewmodel.CartItemListViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.CartItemViewModel;
import java.math.BigDecimal;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hoang on 12/05/2017.
 */
@ActivityScope
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.CartItemViewHolder> {
    private final LayoutInflater layoutInflater;
    private final CartItemListViewModel viewModel;

    @Inject
    public OrderAdapter(@ForActivity Context context, CartItemListViewModel viewModel) {
        this.layoutInflater = LayoutInflater.from(context);
        this.viewModel = viewModel;
    }

    @Override
    public CartItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DishCartItemBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.dish_cart_item, parent, false);
        return new CartItemViewHolder(binding);
    }

    public void clearDate() {

    }

    private List<CartItemViewModel> getCartItems() {
        return viewModel.getCartItems();
    }

    private CartItemViewModel getItems(int pos) {
        return viewModel.getCartItems().get(pos);
    }

    @Override
    public void onBindViewHolder(CartItemViewHolder holder, int position) {
        holder.setupListeners();
    }

    @Override
    public int getItemCount() {
        return getCartItems().size();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {
        DishCartItemBinding binding;

        public CartItemViewHolder(DishCartItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setupListeners() {
            CartItemViewModel cartItemViewModel = getCartItems().get(getAdapterPosition());
            binding.ivMinus.setOnClickListener(v -> {
                cartItemViewModel.onMinusClick()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(oops -> {
                            if (null != listener) {
                                listener.onDecrementQuantity(oops);
                            }
                        });
            });

            binding.ivPlus.setOnClickListener(v -> {
                cartItemViewModel.onPlusClick()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(oops -> {
                            if (null != listener) {
                                listener.onIncrementQuantity(oops);
                            }
                        });

            });

            binding.ivMinus.setOnLongClickListener(v -> {
                if (null != listener) {
                    listener.onRemove(viewModel.remove(getAdapterPosition()));
                    return true;
                }
                return false;
            });

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), DishDetailActivity.class);
                intent.putExtra(Constant.KEY_DISH_DETAIL, cartItemViewModel.id);
                intent.putExtra(Constant.KEY_ORDER_ADAPTER, true);
                itemView.getContext().startActivity(intent);
            });

            itemView.setOnLongClickListener(v -> {
                if (null != listener) {
                    listener.onRemoveAll();
                    return true;
                }
                return false;
            });

            binding.setViewModel(cartItemViewModel);
            binding.executePendingBindings();
        }
    }

    public interface CartListener {
        void onIncrementQuantity(CartItemViewModel.Oops oops);

        void onDecrementQuantity(CartItemViewModel.Oops oops);

        void onRemove(float minus);

        void onRemoveAll();
    }

    private CartListener listener;

    public void setListener(CartListener listener) {
        this.listener = listener;
    }
}
