package com.jdlk7.chatbottfg;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

public class NotificationHandler {

    private static NotificationHandler nHandler;
    private static NotificationManager mNotificationManager;

    private NotificationHandler () {}

    /**
     * Singleton pattern implementation
     * @return
     */
    public static  NotificationHandler getInstance(Context context) {
        if(nHandler == null) {
            nHandler = new NotificationHandler();
            mNotificationManager = (NotificationManager) context.getApplicationContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return nHandler;
    }

    public NotificationCompat.Builder makeSimpleNotificationBuilder(Context context, String title,
                                                                    String text, String channel,
                                                                    boolean isFixed) {
        return new NotificationCompat.Builder(context, channel)
                .setSmallIcon(R.drawable.notification_icon) // notification icon
                .setContentTitle(title) // main title of the notification
                .setContentText(text) // notification text
                .setOngoing(isFixed);
    }

    /**
     * Shows a simple notification
     * @param context aplication context
     */
    public void createSimpleNotification(Context context, String title, String text, String channel,
                                         boolean isFixed) {
        Notification notification =
                makeSimpleNotificationBuilder(context, title, text, channel, isFixed)
                .build();

        // mId allows you to update the notification later on.
        mNotificationManager.notify(10, notification);
    }

    public void createSimpleNotification(Context context, String title, String text, String channel) {
        createSimpleNotification(context, title, text, channel, false);
    }

    public void notify(Notification notification) {
        mNotificationManager.notify(10, notification);
    }

}
