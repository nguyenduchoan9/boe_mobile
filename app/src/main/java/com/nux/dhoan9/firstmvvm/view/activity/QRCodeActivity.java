package com.nux.dhoan9.firstmvvm.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import com.nux.dhoan9.firstmvvm.BoeApplication;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.utils.Utils;
import com.nux.dhoan9.firstmvvm.view.custom.MyContextWrapper;
import com.nux.dhoan9.firstmvvm.view.fragment.QRCodeFragment;
import javax.inject.Inject;

public class QRCodeActivity extends AppCompatActivity {
    public static String QR_CODE_EXPIRE_KEY = "QR_CODE_EXPIRE_KEY";
    @Inject
    PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BoeApplication) getApplication()).getComponent()
                .inject(this);
        Utils.handleSelectLanguage(this, preferencesManager.getLanguage());
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, QRCodeFragment.newInstance(getQrCodeExpire()))
                .commit();
    }

    private boolean getQrCodeExpire() {
        return getIntent().getBooleanExtra(QR_CODE_EXPIRE_KEY, false);
    }

    public static Intent newInstance(Context context) {
        Intent i = new Intent(context, QRCodeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper
                .wrap(newBase,
                        Utils.getLanguage(newBase)));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
