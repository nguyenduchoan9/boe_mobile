package com.nux.dhoan9.firstmvvm.dependency.module;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.nux.dhoan9.firstmvvm.manager.CartManager;
import com.nux.dhoan9.firstmvvm.manager.CartManagerImpl;
import com.nux.dhoan9.firstmvvm.manager.EndpointManager;
import com.nux.dhoan9.firstmvvm.manager.EndpointManagerImpl;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManager;
import com.nux.dhoan9.firstmvvm.manager.PreferencesManagerImpl;
import com.nux.dhoan9.firstmvvm.utils.RetrofitUtils;
import com.nux.dhoan9.firstmvvm.utils.ThreadScheduler;
import com.nux.dhoan9.firstmvvm.utils.ThreadSchedulerImpl;
import com.nux.dhoan9.firstmvvm.view.adapter.DishesByCategoryAdapter;
import com.nux.dhoan9.firstmvvm.viewmodel.DishListViewModel;

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
    public Context provideContext() {
        return this.context;
    }

    @Provides
    public PreferencesManager providePreferencesManager(Context context, Gson gson) {
        return new PreferencesManagerImpl(context, gson);
    }

    @Provides
    @Singleton
    public CartManager provideCartManager() {
        return new CartManagerImpl();
    }

    @Provides
    public Retrofit provideRetrofit(PreferencesManager preferencesManager,
                                    EndpointManager endpointManager) {
        return new RetrofitUtils(preferencesManager, endpointManager).create();
    }

    @Provides
    public Gson provideGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    public Resources provideResources(Context context) {
        return context.getResources();
    }

    @Provides
    public ThreadScheduler provideThreadScheduler() {
        return new ThreadSchedulerImpl(AndroidSchedulers.mainThread(), Schedulers.io());
    }

    @Provides
    public EndpointManager provideEndpointManager(Context context) {
        return new EndpointManagerImpl(context);
    }

}
