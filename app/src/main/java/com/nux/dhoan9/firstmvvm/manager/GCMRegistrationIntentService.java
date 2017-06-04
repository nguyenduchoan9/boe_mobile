package com.nux.dhoan9.firstmvvm.manager;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.nux.dhoan9.firstmvvm.R;

import java.io.IOException;

/**
 * Created by hoang on 24/05/2017.
 */

public class GCMRegistrationIntentService extends IntentService {
    public static final String TAG = "RegIntentService";
    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";

    public GCMRegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Intent i = null;
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                    null);
            i = new Intent(REGISTRATION_SUCCESS);
            i.putExtra("token", token);
            Log.v(TAG, "register successfully");
            sendRegistrationToServer(token);
            subscribeTopics(token);
        } catch (IOException e) {
            Log.v(TAG, "register fail");
            i = new Intent(REGISTRATION_ERROR);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }

    private void subscribeTopics(String token) {
    }

    private void sendRegistrationToServer(String token) {

    }
}
