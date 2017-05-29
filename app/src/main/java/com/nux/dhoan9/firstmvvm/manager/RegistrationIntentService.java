package com.nux.dhoan9.firstmvvm.manager;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.nux.dhoan9.firstmvvm.R;

import java.io.IOException;
/**
 * Created by hoang on 24/05/2017.
 */

public class RegistrationIntentService extends IntentService{
    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            sendRegistrationToServer(token);
            subscribeTopics(token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void subscribeTopics(String token) {
    }

    private void sendRegistrationToServer(String token) {
    }
}
