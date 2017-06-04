package com.nux.dhoan9.firstmvvm.manager;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.view.activity.SlashActivity;
/**
 * Created by hoang on 24/05/2017.
 */

public class GCMIntentService extends GcmListenerService {

    @Override
    public void onMessageReceived(String string, Bundle data) {
        PendingIntent pIntent = PendingIntent.getActivity(
                this,
                (int) System.currentTimeMillis(),
                SlashActivity.newInstance(this, "body"),
                PendingIntent.FLAG_ONE_SHOT
        );
        String message = data.getString("message");
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_add)
                .setContentTitle("Test")
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setContentText(message);
        notificationManager.notify(1, mBuilder.build());
    }
}
