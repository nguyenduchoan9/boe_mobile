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

    public TextView tvProcessingTitle;
    public RelativeLayout rlProcessing;
    protected PreferencesManager mPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setProcessing();
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
}
