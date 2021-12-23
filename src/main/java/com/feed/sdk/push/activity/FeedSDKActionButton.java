package com.feed.sdk.push.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.feed.sdk.push.utils.FeedNotificationDataManager;
import com.feed.sdk.push.utils.FeedNotificationManager;

import static com.feed.sdk.push.NotificationProvider.NOTIFICATION_KEY;

public class FeedSDKActionButton extends AppCompatActivity {
    private static final String TAG = FeedSDKActionButton.class.getSimpleName();

    public static class ButtonReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int notificationId = intent.getIntExtra(NOTIFICATION_KEY, 0);
            Uri url = intent.getData();
            Log.d(TAG, "final url opening :: " + url);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, url);
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(browserIntent);
            intent = null;
            FeedNotificationDataManager.captureClick(notificationId, "ad");

            // if you want cancel notification
            FeedNotificationManager.cancelNotification(context, notificationId);
        }
    }
}
