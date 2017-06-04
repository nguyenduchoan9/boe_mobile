package com.nux.dhoan9.firstmvvm.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.DishCartItemBinding;
import com.nux.dhoan9.firstmvvm.dependency.scope.ActivityScope;
import com.nux.dhoan9.firstmvvm.dependency.scope.ForActivity;
import com.nux.dhoan9.firstmvvm.viewmodel.CartItemListViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.CartItemViewModel;
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

    private List<CartItemViewModel> getCartItems() {
        return viewModel.getCartItems();
    }

    @Override
    public void onBindViewHolder(CartItemViewHolder holder, int position) {
        CartItemViewModel cartItemViewModel = getCartItems().get(position);
        holder.binding.ivMinus.setOnClickListener(v -> {
            cartItemViewModel.onMinusClick()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(oops -> {
                        if(null != listener){
                            listener.onDecrementQuantity(oops);
                        }
                    });
        });

        holder.binding.ivPlus.setOnClickListener(v -> {
            cartItemViewModel.onPlusClick()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(oops -> {
                        if(null != listener){
                            listener.onIncrementQuantity(oops);
                        }
                    });
        });

        holder.binding.setViewModel(cartItemViewModel);
        holder.binding.executePendingBindings();
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
    }

    public interface CartListener {
        void onIncrementQuantity(CartItemViewModel.Oops oops);

        void onDecrementQuantity(CartItemViewModel.Oops oops);
    }

    private CartListener listener;

    public void setListener(CartListener listener) {
        this.listener = listener;
    }
}
