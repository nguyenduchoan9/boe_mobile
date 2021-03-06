package com.nux.dhoan9.firstmvvm.view.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nux.dhoan9.firstmvvm.BoeApplication;
import com.nux.dhoan9.firstmvvm.BuildConfig;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.FragmentLoginBinding;
import com.nux.dhoan9.firstmvvm.dependency.module.UserModule;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.model.User;
import com.nux.dhoan9.firstmvvm.utils.RetrofitUtils;
import com.nux.dhoan9.firstmvvm.utils.RxUtils;
import com.nux.dhoan9.firstmvvm.utils.ToastUtils;
import com.nux.dhoan9.firstmvvm.utils.Utils;
import com.nux.dhoan9.firstmvvm.utils.test.EspressoIdlingResource;
import com.nux.dhoan9.firstmvvm.view.activity.CustomerActivity;
import com.nux.dhoan9.firstmvvm.view.activity.QRCodeActivity;
import com.nux.dhoan9.firstmvvm.view.activity.RegisterActivity;
import com.nux.dhoan9.firstmvvm.view.custom.MyContextWrapper;
import com.nux.dhoan9.firstmvvm.viewmodel.LoginViewModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginFragment extends BaseFragment {

    private FragmentLoginBinding binding;

    @Inject
    LoginViewModel loginViewModel;
    @Inject
    PreferencesManager preferencesManager;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BoeApplication) getActivity().getApplication()).getComponent()
                .plus(new UserModule())
                .inject(this);
        Utils.handleSelectLanguage(getActivity(), preferencesManager.getLanguage());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnRegister.setOnClickListener(v -> startActivity(RegisterActivity.newInstance(getContext())));
        binding.setViewmodel(loginViewModel);
        binding.btnLogin.setOnClickListener(v -> {
            RxUtils.checkNetWork(getContext())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(isAvailable -> {
                        if (-1 == isAvailable) {
                            ToastUtils.toastLongMassage(getContext(), getString(R.string.text_not_available_network));
                        } else if (-2 == isAvailable) {
                            ToastUtils.toastLongMassage(getContext(), getString(R.string.text_server_maintanance));
                        } else {
                            EspressoIdlingResource.increment();
                            loginViewModel.login()
                                    .compose(RxUtils.withLoading(binding.processingContainer.rlProcessing))
                                    .subscribe(new Subscriber<Response<User>>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            ToastUtils.toastLongMassage(getContext(), RetrofitUtils.getMessageError(getContext(), e));
                                        }

                                        @Override
                                        public void onNext(Response<User> userResponse) {
                                            navigateFlow(userResponse);
                                            EspressoIdlingResource.decrement();
                                        }
                                    });
                        }
                    });

        });
        binding.executePendingBindings();
    }

    public void navigateFlow(Response<User> loginResponse) {
        if (BuildConfig.IS_FAKE) {
            startActivity(CustomerActivity.newInstance(getContext()));
        } else {
            if (loginResponse.isSuccessful()) {
                startActivity(QRCodeActivity.newInstance(getContext()));
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loginViewModel.toast()
                .takeUntil(preDestroyView())
                .subscribe(v -> ToastUtils.toastLongMassage(getContext(), v));
    }

    @Override
    protected void setProcessing(RelativeLayout rlProcessing, TextView tvProcessingTitle) {
        super.setProcessing(binding.processingContainer.rlProcessing,
                binding.processingContainer.tvProcessingTitle);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(MyContextWrapper
                .wrap(context,
                        Utils.getLanguage(context)));
    }
}
