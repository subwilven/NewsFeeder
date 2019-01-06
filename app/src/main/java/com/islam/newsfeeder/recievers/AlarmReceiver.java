package com.islam.newsfeeder.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

import com.islam.newsfeeder.services.DbUpdateJobService;

// Triggered by the Alarm periodically (starts the service to run task)
public class AlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, DbUpdateJobService.class);
        ContextCompat.startForegroundService(context, i);
    }
}