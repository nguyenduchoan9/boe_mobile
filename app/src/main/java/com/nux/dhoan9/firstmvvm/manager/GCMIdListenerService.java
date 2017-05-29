package com.nux.dhoan9.firstmvvm.manager;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;
/**
 * Created by hoang on 24/05/2017.
 */

public class GCMIdListenerService extends InstanceIDListenerService{

    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
