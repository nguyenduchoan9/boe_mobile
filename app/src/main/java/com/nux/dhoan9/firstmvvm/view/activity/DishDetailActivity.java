package com.nux.dhoan9.firstmvvm.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.dynamic.LifecycleDelegate;
import com.nux.dhoan9.firstmvvm.Application;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.ActivityDishDetailBinding;
import com.nux.dhoan9.firstmvvm.dependency.module.ActivityModule;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.model.Dish;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.utils.RxUtils;
import com.nux.dhoan9.firstmvvm.view.custom.DragDismissDelegate;
import com.nux.dhoan9.firstmvvm.viewmodel.DishDetailViewModel;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DishDetailActivity extends BaseActivity {

    ActivityDishDetailBinding binding;

    List<LifecycleDelegate> lifecycleDelegates = new ArrayList<>();

    { // Initializer block
        lifecycleDelegates.add(new DragDismissDelegate(this));
    }

    @Inject
    DishDetailViewModel viewModel;
    @Inject
    PreferencesManager preferencesManager;

    private Button btnOrder;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dish_detail);
        super.onCreate(savedInstanceState);
        for (LifecycleDelegate lifecycleDelegate : lifecycleDelegates) {
            lifecycleDelegate.onCreate(savedInstanceState);
        }
        initDependencies();
        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar()
                .setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setTitle("");
        initView();
    }

    private void initView() {
        findView();
        btnOrder.setOnClickListener(v -> {
            viewModel.onOrderClick();
            btnOrder.setVisibility(View.GONE);
        });
        btnCancel.setOnClickListener(v -> {
            viewModel.onCancelClick();
            btnOrder.setVisibility(View.VISIBLE);
        });
    }

    private void findView() {
        btnCancel = binding.btnCancel;
        btnOrder = binding.btnOrder;
    }

    @Override
    protected void setProcessing() {
        tvProcessingTitle = binding.processingContainer.tvProcessingTitle;
        rlProcessing = binding.processingContainer.rlProcessing;
    }

    private void initDependencies() {
        ((Application) getApplication()).getComponent()
                .plus(new ActivityModule(this))
                .inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.setViewModel(viewModel);
        viewModel.getDishDetail(getDishId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> showProcessing("Loading"))
                .doOnCompleted(() -> hideProcessing())
                .subscribe(response -> {
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (LifecycleDelegate lifecycleDelegate : lifecycleDelegates) {
            lifecycleDelegate.onResume();
        }
    }

    @Override
    protected void onPause() {
        for (LifecycleDelegate lifecycleDelegate : lifecycleDelegates) {
            lifecycleDelegate.onPause();
        }
        super.onPause();
    }

    public int getDishId() {
        return getIntent().getIntExtra(Constant.KEY_DISH_DETAIL, -1);
    }

    @Override
    protected void setPreference(PreferencesManager preference) {
        super.setPreference(this.preferencesManager);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
