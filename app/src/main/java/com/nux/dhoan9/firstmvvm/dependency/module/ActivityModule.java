package com.nux.dhoan9.firstmvvm.dependency.module;

import android.app.Activity;
import android.content.Context;

import com.nux.dhoan9.firstmvvm.dependency.scope.ActivityScope;
import com.nux.dhoan9.firstmvvm.dependency.scope.ForActivity;
import com.nux.dhoan9.firstmvvm.utils.support.ListBinder;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hoang on 12/04/2017.
 */
@Module
public class ActivityModule {
    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }


    @Provides
    @ForActivity
    @ActivityScope
    public Context provideContext(){
        return activity;
    }


}
