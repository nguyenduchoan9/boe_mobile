package com.nux.dhoan9.firstmvvm.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.nux.dhoan9.firstmvvm.R;
import butterknife.ButterKnife;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
    }
}
