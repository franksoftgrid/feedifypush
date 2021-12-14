package com.feed.sdk.push.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

public class FeedNotificationManager {

    public static void cancelNotification(final Context context, @Nullable Integer notificationId) {
        if (notificationId != null) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notificationId);
            Intent closeIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(closeIntent);
        }
    }

}
