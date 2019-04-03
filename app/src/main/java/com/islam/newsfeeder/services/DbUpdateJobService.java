package com.islam.newsfeeder.services;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.islam.newsfeeder.POJO.network.ArticleResponse;
import com.islam.newsfeeder.R;
import com.islam.newsfeeder.data.NewsFeederDatabase;
import com.islam.newsfeeder.data.articles.ArticleRepository;
import com.islam.newsfeeder.data.articles.ArticleService;
import com.islam.newsfeeder.util.CallBacks;

import static com.islam.newsfeeder.util.Constants.ID_FOREGROUND_SERVICE;
import static com.islam.newsfeeder.util.Constants.NOTIFICATION_CHANNEL_ID;
import static com.islam.newsfeeder.util.Constants.NOTIFICATION_CHANNEL_NAME;

public class DbUpdateJobService extends IntentService {

    public DbUpdateJobService() {
        super("DbUpdateJobService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForegroundService();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
    }

    private void startForegroundService() {
        Log.d(ArticleRepository.class.getName(), "Start foreground service.");

        // Create notification default intent.
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        String channelId;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createNotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME);
        } else {
            channelId = "";
        }

        // Create notification builder.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        builder.setContentTitle(getString(R.string.syncing_database));
        builder.setContentText(getString(R.string.download_in_progress));
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setProgress(0, 0, true);
        // Make the notification max priority.
        builder.setPriority(Notification.PRIORITY_MAX);
        // Make head-up notification.
        builder.setFullScreenIntent(pendingIntent, true);

        // Build the notification.
        Notification notification = builder.build();

        // Start foreground service.
        startForeground(ID_FOREGROUND_SERVICE, notification);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelId, String channelName) {
        NotificationChannel chan = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setDescription(getString(R.string.syncing));
        NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        service.createNotificationChannel(chan);
        return channelId;
    }


    private void stopForegroundService() {
        stopForeground(true);
        stopSelf();
    }
}
