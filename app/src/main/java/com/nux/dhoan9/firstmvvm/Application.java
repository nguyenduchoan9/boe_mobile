package com.nux.dhoan9.firstmvvm;

import android.content.Context;

import com.nux.dhoan9.firstmvvm.dependency.component.AppComponent;
import com.nux.dhoan9.firstmvvm.dependency.component.DaggerAppComponent;
import com.nux.dhoan9.firstmvvm.dependency.module.AppModule;

/**
 * Created by hoang on 27/03/2017.
 */

public class Application extends android.app.Application {
    private AppComponent daggerAppComponent;

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

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
