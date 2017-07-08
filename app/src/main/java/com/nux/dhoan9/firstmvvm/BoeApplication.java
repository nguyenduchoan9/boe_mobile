package com.nux.dhoan9.firstmvvm;

import android.content.Context;

import com.nux.dhoan9.firstmvvm.dependency.component.AppComponent;
import com.nux.dhoan9.firstmvvm.dependency.component.DaggerAppComponent;
import com.nux.dhoan9.firstmvvm.dependency.module.AppModule;
import com.nux.dhoan9.firstmvvm.manager.CartManager;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import javax.inject.Inject;

/**
 * Created by hoang on 27/03/2017.
 */

public class BoeApplication extends android.app.Application {
    private AppComponent daggerAppComponent;
    @Inject
    PreferencesManager preferencesManager;
    @Inject
    CartManager cartManager;

    @Override
    public void onCreate() {
        super.onCreate();
        buildComponent();
        daggerAppComponent.inject(this);
    }

    private void buildComponent() {
        daggerAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getComponent() {
        return daggerAppComponent;
    }

    public void clearPreviousData() {
        preferencesManager.setTableInfo(null);
        cartManager.clear();
    }
}
