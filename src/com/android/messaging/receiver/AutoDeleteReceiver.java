package com.android.messaging.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.messaging.datamodel.action.AutoDeleteOldConversationsAction;

/**
 * Broadcast receiver that triggers auto-delete of old deleted conversations
 */
public class AutoDeleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AutoDeleteOldConversationsAction.scheduleAutoDelete();
    }
}