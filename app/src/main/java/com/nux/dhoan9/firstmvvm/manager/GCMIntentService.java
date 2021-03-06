package com.nux.dhoan9.firstmvvm.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by hoang on 24/05/2017.
 */

public class GCMIntentService extends GcmListenerService {
    public static final String MESSAGE_TO_DINER = "MESSAGE_TO_DINER";
    public static final String MESSAGE_TO_DINER_REFUND = "MESSAGE_TO_DINER_REFUND";
    public static final String MESSAGE_TO_DINER_CASH_PEDING = "MESSAGE_TO_DINER_CASH_PEDING";
    public static final String MESSAGE_TO_DINER_ALLOWANCE = "MESSAGE_TO_DINER_ALLOWANCE";
    public static final String MESSAGE_TO_CHEF = "MESSAGE_TO_CHEF";
    public static final String MESSAGE_TO_WAITER = "MESSAGE_TO_WAITER";

    @Override
    public void onMessageReceived(String string, Bundle data) {
        String to = data.getString("to");
        String body = data.getString("body");
        String term = data.getString("term");

        if ("diner".equals(to)) {
            if ("notification".equals(term)) {
                sendMessageToDiner(body);
            } else if ("afterRefund".equals(term)) {
                sendMessageRefundToDiner(body);
            } else if ("cashPending".equals(term)) {
                sendMessageCashPendingToDiner(body);
            }else if("allowance".equals(term)){
                sendMessageAllowanceToDiner(body);
            }
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

    private void sendMessageRefundToDiner(String message) {
        Intent i = new Intent(MESSAGE_TO_DINER_REFUND);
        i.putExtra("body", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }

    private void sendMessageCashPendingToDiner(String message) {
        Intent i = new Intent(MESSAGE_TO_DINER_CASH_PEDING);
        i.putExtra("body", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }

    private void sendMessageAllowanceToDiner(String message) {
        Intent i = new Intent(MESSAGE_TO_DINER_ALLOWANCE);
        i.putExtra("body", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }
}
