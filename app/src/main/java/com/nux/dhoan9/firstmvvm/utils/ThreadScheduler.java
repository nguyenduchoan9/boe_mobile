package com.nux.dhoan9.firstmvvm.utils;

import rx.Scheduler;

/**
 * Created by hoang on 28/03/2017.
 */

public interface ThreadScheduler {
    Scheduler subscribeOn();
    Scheduler observeOn();
}
