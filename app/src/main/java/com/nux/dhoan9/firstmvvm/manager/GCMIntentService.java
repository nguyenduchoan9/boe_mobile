package com.nux.dhoan9.firstmvvm.manager;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import com.google.android.gms.gcm.GcmListenerService;
import com.nux.dhoan9.firstmvvm.view.activity.SlashActivity;

/**
 * Created by hoang on 24/05/2017.
 */

public class GCMIntentService extends GcmListenerService {
    public static final String MESSAGE_TO_DINER = "MESSAGE_TO_DINER";
    public static final String MESSAGE_TO_CHEF = "MESSAGE_TO_CHEF";
    public static final String MESSAGE_TO_WAITER = "MESSAGE_TO_WAITER";

    @Override
    public void onMessageReceived(String string, Bundle data) {
        String to = data.getString("to");
        String body = data.getString("body");

        if ("diner".equals(to)) {
            sendMessageToDiner(body);
        } else if ("chef".equals(to)) {
            sendMessageToChef(body);
        } else if ("waiter".equals(to)) {
            sendMessageToWaiter(body);
        }
    }

    private void sendMessageToDiner(String message) {
        Intent i = new Intent(MESSAGE_TO_DINER);
        i.putExtra("body", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }

    private void sendMessageToChef(String message) {
        Intent i = new Intent(MESSAGE_TO_CHEF);
        i.putExtra("body", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }

    private void sendMessageToWaiter(String message) {
        Intent i = new Intent(MESSAGE_TO_WAITER);
        i.putExtra("body", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }
}
