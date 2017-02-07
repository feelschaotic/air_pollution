package com.ramo.air.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.repacked.apache.commons.lang3.StringUtils;

/**
 * Created by ramo on 2016/11/22.
 */
public class MessageReceiver extends BroadcastReceiver {
    public boolean city_change = false;
    public static final String MESSAGE_RECEIVED_ACTION = "com.ramo.air.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
            String messge = intent.getStringExtra(KEY_MESSAGE);
            String extras = intent.getStringExtra(KEY_EXTRAS);
            StringBuilder showMsg = new StringBuilder();
            showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
            if (!StringUtils.isEmpty(extras)) {
                showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
            }
        }
    }
}