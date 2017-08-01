package com.nux.dhoan9.firstmvvm.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by hoang on 27/03/2017.
 */

public class RxUtils {
    public static <E> Observable.Transformer<E, E> withLoading(View loading) {
        return Observable -> Observable.observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> loading.setVisibility(View.VISIBLE))
                .doOnTerminate(() -> loading.setVisibility(View.GONE));
    }

    public static <E> Observable.Transformer<E, E> onProcessRequest() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    public static Observable<Integer> checkNetWork(Context context) {
        return Observable.create(subscriber -> {
            if (!Utils.isNetworkAvailable(context)) {
                subscriber.onNext(-1);
            } else{
                subscriber.onNext(0);
            }
            subscriber.onCompleted();
        });
//        else if (!Utils.isOnline()) {
//            subscriber.onNext(-2);
//        }
    }

}
