package com.nux.dhoan9.firstmvvm.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nux.dhoan9.firstmvvm.BoeApplication;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.FragmentQrcodeBinding;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.model.QRCodeTableInfo;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.view.activity.CustomerActivity;
import com.nux.dhoan9.firstmvvm.view.activity.QRCodeScanActivity;

import javax.inject.Inject;
import static android.app.Activity.RESULT_OK;

public class QRCodeFragment extends Fragment {
    private final int REQUEST_CODE = 400;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 404;
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
        ((BoeApplication) getActivity().getApplication()).getComponent()
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
            checkAndHandlePermission();

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

    private void checkAndHandlePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
//                    Manifest.permission.CAMERA)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//            } else {
//
//                // No explanation needed, we can request the permission.
//
//
//
//                // MY_PERMISSIONS_REQUEST_CAMERA is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
            startActivityForResult(QRCodeScanActivity.newInstace(getContext()), REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (MY_PERMISSIONS_REQUEST_CAMERA == requestCode) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                startActivityForResult(QRCodeScanActivity.newInstace(getContext()), REQUEST_CODE);
            } else {
                return;
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
            return;
        }
    }
}
