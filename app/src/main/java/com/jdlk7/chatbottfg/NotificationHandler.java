package com.jdlk7.chatbottfg;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.Random;

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

    /**
     * Shows a simple notification
     * @param context aplication context
     */
    public void createSimpleNotification(Context context, String title, String text, String channel, boolean isFixed) {

//        // Creates an explicit intent for an Activity
//        Intent resultIntent = new Intent(context, TestActivity.class);
//
//        // Creating a artificial activity stack for the notification activity
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.addParentStack(TestActivity.class);
//        stackBuilder.addNextIntent(resultIntent);
//
//        // Pending intent to the notification manager
//        PendingIntent resultPending = stackBuilder
//                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Building the notification
        Notification notification = new NotificationCompat.Builder(context, channel)
                .setSmallIcon(R.drawable.notification_icon) // notification icon
                .setContentTitle(title) // main title of the notification
                .setContentText(text) // notification text
                .setOngoing(isFixed)
//                .setContentIntent(resultPending)
                .build(); // notification intent

        // mId allows you to update the notification later on.
        mNotificationManager.notify(10, notification);
    }

    public void createSimpleNotification(Context context, String title, String text, String channel) {
        createSimpleNotification(context, title, text, channel, false);
    }

//    public void createExpandableNotification (Context context, String title, String text, String contentTitle, String content, String channel) {
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            // Building the expandable content
//            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//
//            String [] mContent = content.split("\\.");
//
//            inboxStyle.setBigContentTitle(contentTitle);
//            for (String line : mContent) {
//                inboxStyle.addLine(line);
//            }
//
//            // Building the notification
//            Notification notification = new NotificationCompat.Builder(context)
//                    .setSmallIcon(R.drawable.notification_icon) // notification icon
//                    .setContentTitle(title) // title of notification
//                    .setContentText(text) // text inside the notification
//                    .setStyle(inboxStyle) // adds the expandable content to the notification
//                    .build();
//
//            mNotificationManager.notify(11, notification);
//
//        } else {
//            Toast.makeText(context, "Can't show", Toast.LENGTH_LONG).show();
//        }
//    }


//    /**
//     * Show a determinate and undeterminate progress notification
//     * @param context, activity context
//     */
//    public void createProgressNotification (final Context context) {
//
//        // used to update the progress notification
//        final int progresID = new Random().nextInt(1000);
//
//        // building the notification
//        final NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context)
//                .setSmallIcon(R.drawable.refresh)
//                .setContentTitle("Progress notification")
//                .setContentText("Now waiting")
//                .setTicker("Progress notification created")
//                .setUsesChronometer(true)
//                .setProgress(100, 0, true);
//
//
//
//        AsyncTask<Integer, Integer, Integer> downloadTask = new AsyncTask<Integer, Integer, Integer>() {
//            @Override
//            protected void onPreExecute () {
//                super.onPreExecute();
//                mNotificationManager.notify(progresID, nBuilder.build());
//            }
//
//            @Override
//            protected Integer doInBackground (Integer... params) {
//                try {
//                    // Sleeps 2 seconds to show the undeterminated progress
//                    Thread.sleep(5000);
//
//                    // update the progress
//                    for (int i = 0; i < 101; i+=5) {
//                        nBuilder
//                                .setContentTitle("Progress running...")
//                                .setContentText("Now running...")
//                                .setProgress(100, i, false)
//                                .setSmallIcon(R.drawable.download)
//                                .setContentInfo(i + " %");
//
//                        // use the same id for update instead created another one
//                        mNotificationManager.notify(progresID, nBuilder.build());
//                        Thread.sleep(500);
//                    }
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                return null;
//            }
//
//
//            @Override
//            protected void onPostExecute (Integer integer) {
//                super.onPostExecute(integer);
//
//                nBuilder.setContentText("Progress finished :D")
//                        .setContentTitle("Progress finished !!")
//                        .setTicker("Progress finished !!!")
//                        .setSmallIcon(R.drawable.accept)
//                        .setUsesChronometer(false);
//
//                mNotificationManager.notify(progresID, nBuilder.build());
//            }
//        };
//
//        // Executes the progress task
//        downloadTask.execute();
//    }


//    public void createButtonNotification (Context context) {
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            // Prepare intent which is triggered if the  notification button is pressed
//            Intent intent = new Intent(context, TestActivity.class);
//            PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
//
//            // Building the notification
//            NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context)
//                    .setSmallIcon(R.drawable.ic_launcher) // notification icon
//                    .setContentTitle("Button notification") // notification title
//                    .setContentText("Expand to show the buttons...") // content text
//                    .setTicker("Showing button notification") // status bar message
//                    .addAction(R.drawable.accept, "Accept", pIntent) // accept notification button
//                    .addAction(R.drawable.cancel, "Cancel", pIntent); // cancel notification button
//
//            mNotificationManager.notify(1001, nBuilder.build());
//
//        } else {
//            Toast.makeText(context, "You need a higher version", Toast.LENGTH_LONG).show();
//        }
//    }
}
