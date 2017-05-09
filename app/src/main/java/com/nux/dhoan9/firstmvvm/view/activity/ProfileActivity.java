package com.nux.dhoan9.firstmvvm.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nux.dhoan9.firstmvvm.Application;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.ActivityProfileBinding;
import com.nux.dhoan9.firstmvvm.dependency.module.UserModule;
import com.nux.dhoan9.firstmvvm.utils.RxUtils;
import com.nux.dhoan9.firstmvvm.viewmodel.ProfileViewModel;

import javax.inject.Inject;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;

    @Inject
    ProfileViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Application) getApplication()).getComponent()
                .plus(new UserModule())
                .inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        initView();
//        bindingData();
    }

    private void initView() {
        viewModel.initializeData()
                .compose(RxUtils.withLoading(binding.pbLoading))
                .subscribe(userResponse -> bindingData());
    }

    private void bindingData() {
        binding.setViewmodel(viewModel);
        binding.executePendingBindings();
    }

    public static Intent newInstance(Context context) {
        Intent i = new Intent(context, ProfileActivity.class);
        return i;
    }
}
