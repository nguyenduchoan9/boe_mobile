package com.nux.dhoan9.firstmvvm.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nux.dhoan9.firstmvvm.Application;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.FragmentLoginBinding;
import com.nux.dhoan9.firstmvvm.dependency.module.UserModule;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.model.User;
import com.nux.dhoan9.firstmvvm.utils.ToastUtils;
import com.nux.dhoan9.firstmvvm.utils.test.EspressoIdlingResource;
import com.nux.dhoan9.firstmvvm.view.activity.ChefActivity;
import com.nux.dhoan9.firstmvvm.view.activity.CustomerActivity;
import com.nux.dhoan9.firstmvvm.view.activity.RegisterActivity;
import com.nux.dhoan9.firstmvvm.viewmodel.LoginViewModel;

import javax.inject.Inject;

import retrofit2.Response;

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
        ((Application) getActivity().getApplication()).getComponent()
                .plus(new UserModule())
                .inject(this);
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
            EspressoIdlingResource.increment();
            String email = binding.etEmail.getText().toString().trim();
            if ("customer".equals(email)) {
                preferencesManager.setRole(2);
                startActivity(CustomerActivity.newInstance(getContext()));
            } else if ("chef".equals(email)) {
                preferencesManager.setRole(1);
                startActivity(ChefActivity.newInstance(getContext()));
            }
//            loginViewModel.login()
//                    .compose(RxUtils.withLoading(binding.pbLoading))
//                    .subscribe(loginResponse -> {
//                        navigateFlow(loginResponse);
//                        EspressoIdlingResource.decrement();
//                    });
        });
        binding.executePendingBindings();
    }

    public void navigateFlow(Response<User> loginResponse) {
        if (loginResponse.isSuccessful()) {
            startActivity(ChefActivity.newInstance(getContext()));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loginViewModel.toast()
                .takeUntil(preDestroyView())
                .subscribe(v -> ToastUtils.toastLongMassage(getContext(), v));
    }
}
