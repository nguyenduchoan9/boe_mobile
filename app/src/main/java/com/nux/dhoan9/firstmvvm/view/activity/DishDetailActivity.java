package com.nux.dhoan9.firstmvvm.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.dynamic.LifecycleDelegate;
import com.nux.dhoan9.firstmvvm.Application;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.ActivityDishDetailBinding;
import com.nux.dhoan9.firstmvvm.dependency.module.ActivityModule;
import com.nux.dhoan9.firstmvvm.model.Dish;
import com.nux.dhoan9.firstmvvm.utils.Constant;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dish_detail);
        for (LifecycleDelegate lifecycleDelegate : lifecycleDelegates) {
            lifecycleDelegate.onCreate(savedInstanceState);
        }
        initDependencies();
        getDishId();

        binding.setViewModel(viewModel);
    }

    @Override
    protected void setProcessing() {

    }

    private void initDependencies() {
        ((Application) getApplication()).getComponent()
                .plus(new ActivityModule(this))
                .inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.getDishDetail(getDishId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Dish>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Dish dish) {
                    }
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
}
