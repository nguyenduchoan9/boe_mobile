package com.nux.dhoan9.firstmvvm.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nux.dhoan9.firstmvvm.BoeApplication;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.ActivityProfileBinding;
import com.nux.dhoan9.firstmvvm.dependency.module.UserModule;
import com.nux.dhoan9.firstmvvm.model.User;
import com.nux.dhoan9.firstmvvm.utils.RetrofitUtils;
import com.nux.dhoan9.firstmvvm.utils.RxUtils;
import com.nux.dhoan9.firstmvvm.utils.ToastUtils;
import com.nux.dhoan9.firstmvvm.utils.Utils;
import com.nux.dhoan9.firstmvvm.view.custom.MyContextWrapper;
import com.nux.dhoan9.firstmvvm.viewmodel.ProfileViewModel;

import javax.inject.Inject;
import retrofit2.Response;
import rx.Subscriber;
import rx.functions.Action1;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;

    @Inject
    ProfileViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BoeApplication) getApplication()).getComponent()
                .plus(new UserModule())
                .inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar()
                .setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setTitle("Profile");

        initView();
//        bindingData();
    }

    private void initView() {
        viewModel.initializeData()
                .compose(RxUtils.withLoading(binding.pbLoading))
                .subscribe(new Subscriber<Response<User>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.toastLongMassage(ProfileActivity.this, RetrofitUtils.getMessageError(ProfileActivity.this, e));
                    }

                    @Override
                    public void onNext(Response<User> userResponse) {
//                        if (userResponse.isSuccessful()) {
                        bindingData();
//                        } else {
//
//                        }
                    }
                });
    }

    private void bindingData() {
        binding.setViewmodel(viewModel);
        binding.executePendingBindings();
    }

    public static Intent newInstance(Context context) {
        Intent i = new Intent(context, ProfileActivity.class);
        return i;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper
                .wrap(newBase,
                        Utils.getLanguage(newBase)));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
