package com.nux.dhoan9.firstmvvm.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.dynamic.LifecycleDelegate;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.ActivityDishDetailBinding;
import com.nux.dhoan9.firstmvvm.view.custom.DragDismissDelegate;

import java.util.ArrayList;
import java.util.List;

public class DishDetailActivity extends AppCompatActivity {
    public static String DISH_ID = "DISH_ID";
    ActivityDishDetailBinding binding;

    List<LifecycleDelegate> lifecycleDelegates = new ArrayList<>();

    { // Initializer block
        lifecycleDelegates.add(new DragDismissDelegate(this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dish_detail);
        for (LifecycleDelegate lifecycleDelegate : lifecycleDelegates) {
            lifecycleDelegate.onCreate(savedInstanceState);
        }
        int image = R.drawable.chocolate_ball;
        binding.setImage(image);
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
}
