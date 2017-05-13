package com.nux.dhoan9.firstmvvm.viewmodel;

import android.content.res.Resources;
import android.databinding.BaseObservable;

import com.nux.dhoan9.firstmvvm.utils.ThreadScheduler;

import rx.Observable;

/**
 * Created by hoang on 28/03/2017.
 */

public abstract class BaseViewModel extends BaseObservable {
    protected ThreadScheduler threadScheduler;
    protected Resources resources;

    public BaseViewModel(ThreadScheduler threadScheduler, Resources resources) {
        this.threadScheduler = threadScheduler;
        this.resources = resources;
    }

    public <T> Observable.Transformer<T, T> withScheduler() {
        return observable -> observable.observeOn(threadScheduler.observeOn())
                .subscribeOn(threadScheduler.subscribeOn());
    }

    protected String getString(int id){
        return resources.getString(id);
    }

}
