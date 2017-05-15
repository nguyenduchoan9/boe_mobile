package com.nux.dhoan9.firstmvvm.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nux.dhoan9.firstmvvm.Application;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.ActivityCartBinding;
import com.nux.dhoan9.firstmvvm.dependency.module.ActivityModule;
import com.nux.dhoan9.firstmvvm.view.adapter.OrderAdapter;
import com.nux.dhoan9.firstmvvm.viewmodel.CartItemListViewModel;

import javax.inject.Inject;

public class CartActivity extends AppCompatActivity {
    ActivityCartBinding binding;

    @Inject
    CartItemListViewModel cartItemListViewModel;
    @Inject
    OrderAdapter adapter;

    RecyclerView rvCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        ((Application) getApplication()).getComponent()
                .plus(new ActivityModule(this))
                .inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeView();
    }

    private void initializeView() {
        enableBackButton();
        binding.setViewModel(cartItemListViewModel);
        cartItemListViewModel.initialize();

        rvCart = binding.rvCart;
        LinearLayoutManager manager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvCart.setAdapter(adapter);
        rvCart.setLayoutManager(manager);
    }

    private void enableBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, CartActivity.class);
        return intent;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
