package com.nux.dhoan9.firstmvvm.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.nux.dhoan9.firstmvvm.BoeApplication;
import com.nux.dhoan9.firstmvvm.BuildConfig;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.model.QRCodeTableInfo;
import com.nux.dhoan9.firstmvvm.model.User;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.utils.ToastUtils;
import com.nux.dhoan9.firstmvvm.utils.Utils;
import com.nux.dhoan9.firstmvvm.view.custom.MyContextWrapper;
import com.nux.dhoan9.firstmvvm.view.fragment.EndpointDialogFragment;
import javax.inject.Inject;

public class SlashActivity extends AppCompatActivity {
    private final int PLAY_SERVICES_RESOLUTION_REQUEST = 1;
    @Inject
    PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slash);
        ((BoeApplication) getApplication()).getComponent()
                .inject(this);
        if (checkGooglePlayService()) {
            new Handler().postDelayed(() -> {
                if (BuildConfig.IS_PROD) {
                    navigateUser();
                } else
                    showEndpointDialog();
            }, 4000);
        } else {
            finish();
        }
    }

    private void showEndpointDialog() {
        EndpointDialogFragment dialog = EndpointDialogFragment.newInstance();
        dialog.setListener(() -> navigateUser());

        dialog.show(getSupportFragmentManager(),
                EndpointDialogFragment.class.getSimpleName());
    }

    //    https://stackoverflow.com/questions/31016722/googleplayservicesutil-vs-googleapiavailability
    private boolean checkGooglePlayService() {
        GoogleApiAvailability googleApi = GoogleApiAvailability.getInstance();
        int resultCode = googleApi.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS != resultCode) {
            if (googleApi.isUserResolvableError(resultCode)) {
                googleApi.getErrorDialog(this, resultCode,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
//                ToastUtils.toastLongMassage(this, "Google Play Services error");
                ToastUtils.toastLongMassage(this, "This device dose not support Google Play Service");
            }
            return false;
        }
        return true;
    }

    private void navigateUser() {
        User user = preferencesManager.getUser();
        if (null != user) {
            if (Constant.ROLE_DINER.equals(user.getRole())) {
                QRCodeTableInfo tableInfo = preferencesManager.getTableInfo();
//                Utils.setLanguage(this, preferencesManager.getLanguage());
                if (null != tableInfo) {
                    startActivity(CustomerActivity.newInstance(this));
                } else {
                    startActivity(QRCodeActivity.newInstance(this));
                }
            }
        } else {
            startActivity(LoginActivity.newInstance(this));
        }
    }

    public static Intent newInstance(Context context, String body) {
        Intent i = new Intent(context, SlashActivity.class);
        return i;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper
                .wrap(newBase,
                        Utils.getLanguage(newBase)));
    }
}
