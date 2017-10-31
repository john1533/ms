package com.mobcent.lowest.module.ad.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.mobcent.lowest.base.utils.MCResource;

public class AdNotificationHelper {
    public static Notification createNotification(Context context) {
        Notification notification = new Notification(17301633, "", System.currentTimeMillis());
        notification.flags = 32;
        notification.when = System.currentTimeMillis();
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), MCResource.getInstance(context).getLayoutId("mc_ad_widget_notification"));
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(), 134217728);
        notification.contentView = remoteViews;
        notification.contentIntent = contentIntent;
        return notification;
    }

    public static NotificationManager getManager(Context context) {
        return (NotificationManager) context.getSystemService("notification");
    }
}
