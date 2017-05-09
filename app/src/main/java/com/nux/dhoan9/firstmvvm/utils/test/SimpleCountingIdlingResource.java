package com.nux.dhoan9.firstmvvm.utils.test;

import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hoang on 03/04/2017.
 */

public class SimpleCountingIdlingResource implements IdlingResource {

    private final String mResourceName;

    private final AtomicInteger counter = new AtomicInteger(0);

    private volatile ResourceCallback resourceCallback;

    public SimpleCountingIdlingResource(String mResourceName) {
        this.mResourceName = mResourceName;
    }

    @Override
    public String getName() {
        return mResourceName;
    }

    @Override
    public boolean isIdleNow() {
        return counter.get() == 0;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;
    }

    public void increment() {
        counter.incrementAndGet();
    }

    public void decrement() {
        int counterVal = counter.decrementAndGet();
        if (0 == counterVal) {
            if (null != resourceCallback) {
                resourceCallback.onTransitionToIdle();
            }
        }

        if (counterVal < 0) {
            throw new IllegalArgumentException("Counter has been corrupted.");
        }
    }
}
