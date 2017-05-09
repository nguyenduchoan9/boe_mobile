package com.nux.dhoan9.firstmvvm.utils;

import rx.Scheduler;

/**
 * Created by hoang on 28/03/2017.
 */

public class ThreadSchedulerImpl implements ThreadScheduler {
    private Scheduler observeOn;

    public ThreadSchedulerImpl(Scheduler observeOn, Scheduler subscribeOn) {
        this.observeOn = observeOn;
        this.subscribeOn = subscribeOn;
    }

    private Scheduler subscribeOn;

    @Override
    public Scheduler subscribeOn() {
        return subscribeOn;
    }

    @Override
    public Scheduler observeOn() {
        return observeOn;
    }
}
