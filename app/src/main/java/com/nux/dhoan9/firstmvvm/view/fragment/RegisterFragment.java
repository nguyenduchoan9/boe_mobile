package com.nux.dhoan9.firstmvvm.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nux.dhoan9.firstmvvm.BoeApplication;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.FragmentRegisterBinding;
import com.nux.dhoan9.firstmvvm.dependency.module.UserModule;
import com.nux.dhoan9.firstmvvm.model.User;
import com.nux.dhoan9.firstmvvm.utils.RxUtils;
import com.nux.dhoan9.firstmvvm.utils.test.EspressoIdlingResource;
import com.nux.dhoan9.firstmvvm.view.activity.QRCodeActivity;
import com.nux.dhoan9.firstmvvm.viewmodel.RegisterViewModel;

import javax.inject.Inject;

import retrofit2.Response;

public class RegisterFragment extends BaseFragment {
    private FragmentRegisterBinding binding;

    @Inject
    RegisterViewModel viewModel;

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BoeApplication) getActivity().getApplication()).getComponent()
                .plus(new UserModule())
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewmodel(viewModel);
        binding.btnRegister.setOnClickListener(v -> {
            EspressoIdlingResource.increment();
            viewModel.register()
                    .compose(RxUtils.withLoading(binding.pbLoading))
                    .subscribe(response -> {
                        navigateFlow(response);
                        EspressoIdlingResource.decrement();
                    });
//                    RxUtils.checkNetWork(getContext())
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(isAvailable -> {
//                                if (-1 == isAvailable) {
//                                    ToastUtils.toastLongMassage(getContext(), getString(R.string.text_not_available_network));
//                                } else if (-2 == isAvailable) {
//                                    ToastUtils.toastLongMassage(getContext(), getString(R.string.text_server_maintanance));
//                                } else {
//
//                                }
//                            });
                }
        );
        binding.executePendingBindings();
    }

    private void navigateFlow(Response<User> response) {
        if (response.isSuccessful())
            startActivity(QRCodeActivity.newInstance(getContext()));
    }

}
