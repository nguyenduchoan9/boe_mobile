package com.nux.dhoan9.firstmvvm.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.nux.dhoan9.firstmvvm.Application;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.FragmentOrderBinding;
import com.nux.dhoan9.firstmvvm.dependency.module.ActivityModule;
import com.nux.dhoan9.firstmvvm.manager.CartManager;
import com.nux.dhoan9.firstmvvm.view.activity.CustomerActivity;
import com.nux.dhoan9.firstmvvm.view.adapter.OrderAdapter;
import com.nux.dhoan9.firstmvvm.viewmodel.CartItemListViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.CartItemViewModel;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OrderFragment extends BaseFragment {
    FragmentOrderBinding binding;

    @Inject
    CartItemListViewModel cartItemListViewModel;
    @Inject
    OrderAdapter adapter;
    @Inject
    CartManager cartManager;
    float total = 0F;
    RecyclerView rvCart;

    public OrderFragment() {
    }

    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Application) getActivity().getApplication()).getComponent()
                .plus(new ActivityModule(getActivity()))
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    @Override
    public void onStart() {
        super.onStart();
        setOrderToolBar();

        ((CustomerActivity) getActivity()).getNavigationBottom().setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeData();
    }

    private void initializeData() {
        binding.setViewModel(cartItemListViewModel);
        cartItemListViewModel
                .initialize()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(v -> showProcessing("Loading..."))
                .doOnCompleted(() -> hideProcessing())
                .subscribe(result -> {
                    initTotalPayment(cartItemListViewModel.getCartItems());
                });

    }

    private void initTotalPayment(List<CartItemViewModel> cartItems) {
        total = 0;
        for (int i = 0; i < cartItems.size(); i++) {
            CartItemViewModel cartItem = cartItems.get(i);
            total += cartItem.quantity * cartItem.price;
        }
        setTotal(String.valueOf(total));
    }

    private void initView() {

        rvCart = binding.rvCart;
        adapter.setListener(new OrderAdapter.CartListener() {
            @Override
            public void onIncrementQuantity(CartItemViewModel.Oops oops) {
                cartManager.plus(oops.dishId, 1);
                total += oops.price;
                String textTotal = String.valueOf(total);
                setTotal(textTotal);
            }

            @Override
            public void onDecrementQuantity(CartItemViewModel.Oops oops) {
                cartManager.minus(oops.dishId, 1);
                total += oops.price;
                String textTotal = String.valueOf(total);
                setTotal(textTotal);
            }
        });
        LinearLayoutManager manager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        rvCart.setAdapter(adapter);
        rvCart.setLayoutManager(manager);
        rvCart.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rvCart.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RelativeLayout view = ((CustomerActivity) getActivity()).getNavigationBottom();
                if (dy > 0) {
                    // Scrolling up
                    view.setVisibility(View.GONE);
                } else {
                    // Scrolling down\
                    view.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public float getTotalPayment() {
        return total;
    }
    public int getItemtotal(){
        return cartManager.getItamTotal();
    }
}
