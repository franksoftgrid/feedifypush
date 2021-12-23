package com.feed.sdk.push.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

/**
 * NotificationManager to cancel notification
 */
public class FeedNotificationManager {
    /**
     * call to cancel the notification, Cancel a previously shown notification.
     * If it's transient, the view will be hidden. If it's persistent,
     * it will be removed from the status bar.
     *
     * @param context        - activity,fragment context
     * @param notificationId - notification id used to dismiss/ cancel the notification
     */
    public static void cancelNotification(final Context context, @Nullable Integer notificationId) {
        if (notificationId != null) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notificationId);
            Intent closeIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(closeIntent);
        }
    }

}
