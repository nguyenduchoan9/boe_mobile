package com.nux.dhoan9.firstmvvm.view.fragment;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.FragmentQrcodeBinding;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.view.activity.QRCodeScanActivity;

import static android.app.Activity.RESULT_OK;


public class QRCodeFragment extends Fragment {
    private final int REQUEST_CODE = 400;
    FragmentQrcodeBinding binding;

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
        binding.ivScanner.setOnClickListener(v -> {
            startActivityForResult(QRCodeScanActivity.newInstace(getContext()), REQUEST_CODE);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (null != data) {
                String barcodeFormat = data.getStringExtra(Constant.SCANNER_RESULT_QRCODE);
                String content = data.getStringExtra(Constant.SCANNER_RESULT_CONTENT);
                binding.tvContent.setText(content);
                binding.tvFormat.setText(barcodeFormat);
            }
        }
    }
}
