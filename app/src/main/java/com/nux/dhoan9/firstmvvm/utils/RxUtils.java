package com.nux.dhoan9.firstmvvm.utils;

import android.util.Log;
import android.view.View;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by hoang on 27/03/2017.
 */

public class RxUtils {
    public static <E> Observable.Transformer<E, E> withLoading(View loading) {
        return Observable -> Observable.observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> loading.setVisibility(View.VISIBLE) )
                .doOnTerminate(() -> loading.setVisibility(View.GONE) );
    }

    public static <E> Observable.Transformer<E, E> onProcessRequest(){
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }
}
