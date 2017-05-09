package com.nux.dhoan9.firstmvvm.dependency.module;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManagerImpl;
import com.nux.dhoan9.firstmvvm.utils.RetrofitUtils;
import com.nux.dhoan9.firstmvvm.utils.ThreadScheduler;
import com.nux.dhoan9.firstmvvm.utils.ThreadSchedulerImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hoang on 27/03/2017.
 */
@Module
public class AppModule {

    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public Context provideContext(){
        return this.context;
    }

    @Provides
    public PreferencesManager providePreferencesManager(Context context,Gson gson){
        return new PreferencesManagerImpl(context, gson);
    }

    @Provides
    public Retrofit provideRetrofit(PreferencesManager preferencesManager){
        return new RetrofitUtils(preferencesManager).create();
    }

    @Provides
    public Gson provideGson(){
        return new Gson();
    }

    @Provides
    @Singleton
    public Resources provideResources(Context context){
        return context.getResources();
    }

    @Provides
    public ThreadScheduler provideThreadScheduler(){
        return new ThreadSchedulerImpl(AndroidSchedulers.mainThread(), Schedulers.io());
    }
}
