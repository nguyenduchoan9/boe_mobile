package com.nux.dhoan9.firstmvvm.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;

public abstract class BaseActivity extends AppCompatActivity {
    TextView tvProcessingTitle;
    RelativeLayout rlProcessing;
    private PreferencesManager preferencesManager;

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
    protected void onDestroy() {
        if (null != preferencesManager)
            preferencesManager.setTableInfo(null);
        super.onDestroy();
    }

    protected void setPreference(PreferencesManager preference) {
        this.preferencesManager = preference;
    }
}
