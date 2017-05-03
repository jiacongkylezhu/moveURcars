package com.kylezhudev.moveurcars;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RebootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent rebootIntent = new Intent(context, NotificationService.class);
        context.startService(rebootIntent);
    }
}
