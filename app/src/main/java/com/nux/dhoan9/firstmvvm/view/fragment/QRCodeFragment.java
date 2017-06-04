package com.nux.dhoan9.firstmvvm.view.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nux.dhoan9.firstmvvm.Application;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.FragmentQrcodeBinding;
import com.nux.dhoan9.firstmvvm.dependency.module.UserModule;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.model.QRCodeTableInfo;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.view.activity.CustomerActivity;
import com.nux.dhoan9.firstmvvm.view.activity.QRCodeScanActivity;

import javax.inject.Inject;
import static android.app.Activity.RESULT_OK;

public class QRCodeFragment extends Fragment {
    private final int REQUEST_CODE = 400;
    FragmentQrcodeBinding binding;

    @Inject
    PreferencesManager preferencesManager;

    public QRCodeFragment() {
        // Required empty public constructor
    }

    public static QRCodeFragment newInstance() {
        QRCodeFragment fragment = new QRCodeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((Application) getActivity().getApplication()).getComponent()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_qrcode, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvScanner.setOnClickListener(v -> {
            startActivityForResult(QRCodeScanActivity.newInstace(getContext()), REQUEST_CODE);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (null != data) {
                String barcodeFormat = data.getStringExtra(Constant.SCANNER_RESULT_QRCODE);
                String content = data.getStringExtra(Constant.SCANNER_RESULT_CONTENT);
                if (null != barcodeFormat && null != content) {
                    QRCodeTableInfo tableInfo = new QRCodeTableInfo(barcodeFormat, content);
                    preferencesManager.setTableInfo(tableInfo);
                    startActivity(CustomerActivity.newInstance(getContext()));
                }
            }
        }
    }
}
