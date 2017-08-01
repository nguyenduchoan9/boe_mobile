package com.nux.dhoan9.firstmvvm.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import rx.subjects.PublishSubject;

public abstract class BaseActivity extends AppCompatActivity {
    protected PublishSubject<BaseActivity> destroyView = PublishSubject.create();
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
        if (tvProcessingTitle != null && rlProcessing != null) {
            if (null != title) {
                tvProcessingTitle.setText(title);
            } else {
                tvProcessingTitle.setText(R.string.text_processing);
            }
            rlProcessing.setVisibility(View.VISIBLE);
        }
    }

    public void hideProcessing() {
        if (null != rlProcessing)
            rlProcessing.setVisibility(View.GONE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("ZZZZZZZ", "onStop");
    }

    @Override
    protected void onDestroy() {
        destroyView.onNext(this);
        super.onDestroy();
    }
}
