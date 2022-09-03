package com.example.takecare_11.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver
{
    private String title, message;

    @Override
    public void onReceive(Context context, Intent intent) {

        title = intent.getStringExtra("alert_title");
        message = intent.getStringExtra("alert_message");

        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb =  notificationHelper.GetChannel1Notification(title, message);
        notificationHelper.getManager().notify(1, nb.build());

    }
}
