package com.nux.dhoan9.firstmvvm.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.manager.GCMRegistrationIntentService;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.utils.ToastUtils;

public abstract class BaseActivity extends AppCompatActivity {
    private final static String GCM_TOKEN = "GCM_TOKEN";
    private BroadcastReceiver mBroadcastReceiver;
    public TextView tvProcessingTitle;
    public RelativeLayout rlProcessing;
    protected PreferencesManager mPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBroadcastReceiver();
        setProcessing();
        setPreference();
    }

    protected void setBroadcastReceiver() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().endsWith(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                    String token = intent.getStringExtra("token");
                    Log.v(GCM_TOKEN, token);
                } else if (intent.getAction().endsWith(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    ToastUtils.toastLongMassage(getApplicationContext(),
                            "GCM registration error");
                } else {
                    ToastUtils.toastShortMassage(getApplicationContext(), "Nothing");
                }
            }
        };

        Intent intent = new Intent(getApplicationContext(), GCMRegistrationIntentService.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("ZZZZZZZ", "onPause");
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mBroadcastReceiver);
    }

    protected abstract void setProcessing();

    public void showProcessing(String title) {
        if (null != title) {
            tvProcessingTitle.setText(title);
        } else {
            tvProcessingTitle.setText(R.string.text_processing);
        }
        rlProcessing.setVisibility(View.VISIBLE);
    }

    public void hideProcessing() {
        rlProcessing.setVisibility(View.GONE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("ZZZZZZZ", "onStop");
    }

    @Override
    protected void onDestroy() {
        Log.i("ZZZZZZZ", "onDestroy");
        if (null != mPreferencesManager) {
            Log.i("ZZZZZZZ", "onDestroy- pre");
            mPreferencesManager.setTableInfo(null);
        }
        super.onDestroy();
    }

    protected abstract void setPreference();
}
