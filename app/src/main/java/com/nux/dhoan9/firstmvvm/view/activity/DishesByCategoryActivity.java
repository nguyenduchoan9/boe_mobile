package com.nux.dhoan9.firstmvvm.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.nux.dhoan9.firstmvvm.Application;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.data.repo.DishRepo;
import com.nux.dhoan9.firstmvvm.databinding.ActivityDishesByCategoryBinding;
import com.nux.dhoan9.firstmvvm.dependency.module.DishByCategoryModule;
import com.nux.dhoan9.firstmvvm.dependency.scope.ForSecondActivity;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.view.adapter.DishesByCategoryAdapter;
import com.nux.dhoan9.firstmvvm.view.custom.ItemDecorationAlbumColumns;
import com.nux.dhoan9.firstmvvm.viewmodel.DishListViewModel;

import javax.inject.Inject;

public class DishesByCategoryActivity extends BaseActivity {
    ActivityDishesByCategoryBinding binding;

    RecyclerView rvDish;

    @Inject
    DishesByCategoryAdapter adapter;
    @Inject
    DishListViewModel viewModel;
    @Inject
    DishRepo dishRepo;
    @Inject
    PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dishes_by_category);
        ((Application) getApplication()).getComponent()
                .plus(new DishByCategoryModule(this))
                .inject(this);
    }

    @Override
    protected void setProcessing() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }

    private void initView() {
        enableBackButton();
        rvDish = binding.rvDish;
        GridLayoutManager manager =
                new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        rvDish.setAdapter(adapter);
        rvDish.setLayoutManager(manager);

        binding.setViewModel(viewModel);
        viewModel.initializeCategory(getIdCategory());
    }

    private void enableBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, DishesByCategoryActivity.class);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public int getIdCategory() {
        return getIntent().getIntExtra(Constant.KEY_ID_CATEGORY, 1);
    }

    @Override
    protected void setPreference(PreferencesManager preference) {
        super.setPreference(this.preferencesManager);
    }
}
