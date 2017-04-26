package com.kylezhudev.moveurcars;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Kyle on 1/21/2017.
 */

public class RebootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent rebootIntent = new Intent(context, NotificationService.class);
        context.startService(rebootIntent);
    }
}
