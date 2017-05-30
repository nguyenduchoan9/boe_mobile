package com.nux.dhoan9.firstmvvm.view.fragment;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nux.dhoan9.firstmvvm.Application;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.FragmentOrderBinding;
import com.nux.dhoan9.firstmvvm.dependency.module.ActivityModule;
import com.nux.dhoan9.firstmvvm.manager.CartManager;
import com.nux.dhoan9.firstmvvm.view.activity.CustomerActivity;
import com.nux.dhoan9.firstmvvm.view.adapter.OrderAdapter;
import com.nux.dhoan9.firstmvvm.view.custom.NavigationBottom;
import com.nux.dhoan9.firstmvvm.viewmodel.CartItemListViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.CartItemViewModel;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OrderFragment extends Fragment {
    FragmentOrderBinding binding;

    @Inject
    CartItemListViewModel cartItemListViewModel;
    @Inject
    OrderAdapter adapter;
    @Inject
    CartManager cartManager;

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
        initializeData();
        ((CustomerActivity) getActivity()).getNavigationBottom().setVisibility(View.VISIBLE);
    }

    private void initializeData() {
        binding.setViewModel(cartItemListViewModel);
        cartItemListViewModel.initialize();
        initTotalPayment(cartItemListViewModel.getCartItems());
    }

    private void initTotalPayment(List<CartItemViewModel> cartItems) {
        float total = 0F;
        for (int i = 0; i < cartItems.size(); i++) {
            CartItemViewModel cartItem = cartItems.get(i);
            total += cartItem.quantity * cartItem.price;
        }
        binding.tvTotalPayment.setText(String.valueOf(total));
    }

    private void initView() {
        rvCart = binding.rvCart;
        adapter.setListener(new OrderAdapter.CartListener() {
            @Override
            public void onIncrementQuantity(Observable<Float> subscribe) {
                float currentTotal = Float.valueOf(binding.tvTotalPayment.getText().toString());
                subscribe
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(value ->
                                binding.tvTotalPayment.setText(String.valueOf(currentTotal + value)));
            }

            @Override
            public void onDecrementQuantity(Observable<Float> subscribe) {
                float currentTotal = Float.valueOf(binding.tvTotalPayment.getText().toString());
                subscribe
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(value ->
                                binding.tvTotalPayment.setText(String.valueOf(currentTotal - value)));
            }
        });
        LinearLayoutManager manager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvCart.setAdapter(adapter);
        rvCart.setLayoutManager(manager);
        rvCart.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RelativeLayout view = ((CustomerActivity) getActivity()).getNavigationBottom();
                TextView tvTotalPayment = binding.tvTotalPayment;
                TextView tvContinues = binding.tvContinues;
                TextView tvClearAll = binding.tvClearAll;
                if (dy > 0) {
                    // Scrolling up
                    ObjectAnimator navigationAnim = ObjectAnimator
                            .ofFloat(view, View.TRANSLATION_Y, view.getHeight() * 1.1F);
                    ObjectAnimator tvTotalPaymentAnim = ObjectAnimator
                            .ofFloat(tvTotalPayment, View.TRANSLATION_X, -1 * tvTotalPayment.getWidth() * 1.1F);
                    ObjectAnimator tvContinuesAnim = ObjectAnimator
                            .ofFloat(tvContinues, View.TRANSLATION_X, tvContinues.getWidth() * 1.1F);
                    ObjectAnimator tvClearAllAnim = ObjectAnimator
                            .ofFloat(tvClearAll, View.TRANSLATION_X, tvClearAll.getWidth() * 1.1F);
                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(navigationAnim, tvTotalPaymentAnim, tvContinuesAnim, tvClearAllAnim);
                    set.setDuration(300).setInterpolator(new DecelerateInterpolator());
                    set.start();
                    view.setVisibility(View.GONE);
                } else {
                    // Scrolling down\
                    view.setVisibility(View.VISIBLE);
                    ObjectAnimator navigationAnim = ObjectAnimator
                            .ofFloat(view, View.TRANSLATION_Y, 0);
                    ObjectAnimator tvTotalPaymentAnim = ObjectAnimator
                            .ofFloat(tvTotalPayment, View.TRANSLATION_X, 0);
                    ObjectAnimator tvContinuesAnim = ObjectAnimator
                            .ofFloat(tvContinues, View.TRANSLATION_X, 0);
                    ObjectAnimator tvClearAllAnim = ObjectAnimator
                            .ofFloat(tvClearAll, View.TRANSLATION_X, 0);
                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(navigationAnim, tvTotalPaymentAnim, tvContinuesAnim, tvClearAllAnim);
                    set.setDuration(300).setInterpolator(new DecelerateInterpolator());
                    set.start();
                }
            }
        });
    }
}
