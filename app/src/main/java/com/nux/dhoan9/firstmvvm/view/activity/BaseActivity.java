package com.nux.dhoan9.firstmvvm.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class BaseActivity extends AppCompatActivity {
    ProgressBar srProcessing;
    TextView tvProcessingTitle;
    RelativeLayout rlProcessing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setProcessing();
    }

    protected abstract void setProcessing();

    protected void showProcessing(String title) {
        if (null != title) {
            tvProcessingTitle.setText(title);
        }
        rlProcessing.setVisibility(View.VISIBLE);
    }

    protected void hideProcessing() {
        rlProcessing.setVisibility(View.GONE);
        tvProcessingTitle.setText("Processing...");
    }
}
