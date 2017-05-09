package com.nux.dhoan9.firstmvvm.utils.test;

/**
 * Created by hoang on 03/04/2017.
 */

import android.support.test.espresso.IdlingResource;

public class EspressoIdlingResource {
    private static final String RESOURCE = "GLOBAL";
    private static SimpleCountingIdlingResource mSimpleCountingIdlingResource =
            new SimpleCountingIdlingResource(RESOURCE);

    public static void increment() {
        mSimpleCountingIdlingResource.increment();
    }

    public static void decrement() {
        mSimpleCountingIdlingResource.decrement();
    }

    public static IdlingResource getEdlingResource(){
        return mSimpleCountingIdlingResource;
    }
}
