package com.nux.dhoan9.firstmvvm.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nux.dhoan9.firstmvvm.BoeApplication;
import com.nux.dhoan9.firstmvvm.dependency.module.UserModule;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.utils.Utils;
import com.nux.dhoan9.firstmvvm.utils.test.EspressoIdlingResource;
import com.nux.dhoan9.firstmvvm.view.custom.MyContextWrapper;
import com.nux.dhoan9.firstmvvm.view.fragment.LoginFragment;
import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity {
    @Inject
    PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BoeApplication) getApplication()).getComponent()
                .inject(this);
        Utils.handleSelectLanguage(this, preferencesManager.getLanguage());
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, LoginFragment.newInstance())
                .commit();
    }

    public static Intent newInstance(Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    @VisibleForTesting
    public IdlingResource getIdlingResource() {
        return EspressoIdlingResource.getEdlingResource();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper
                .wrap(newBase,
                        Utils.getLanguage(newBase)));
    }
}
