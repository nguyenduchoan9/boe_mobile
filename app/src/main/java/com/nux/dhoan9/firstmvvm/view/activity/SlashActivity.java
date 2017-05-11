package com.nux.dhoan9.firstmvvm.view.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nux.dhoan9.firstmvvm.Application;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;

import javax.inject.Inject;

public class SlashActivity extends AppCompatActivity {

    @Inject
    PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slash);
        ((Application) getApplication()).getComponent()
                .inject(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateUser();
            }
        }, 4000);


    }

    private void navigateUser() {
//        if (preferencesManager.isLoggedin()) {
//            startActivity(ChefActivity.newInstance(this));
//        } else {
//            startActivity(LoginActivity.newInstance(this));
//        }
        int role = preferencesManager.getRole();
        if (1 == role) {
            startActivity(ChefActivity.newInstance(this));
        } else if (2 == role) {
            startActivity(CustomerActivity.newInstance(this));
        }
    }
}
