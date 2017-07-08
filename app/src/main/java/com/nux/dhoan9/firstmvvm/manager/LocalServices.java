package com.nux.dhoan9.firstmvvm.manager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.nux.dhoan9.firstmvvm.BoeApplication;

/**
 * Created by hoang on 26/06/2017.
 */

public class LocalServices extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        ((BoeApplication)getApplication()).clearPreviousData();
        stopSelf();
    }
}
