package com.feed.sdk.push.utils;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.feed.sdk.push.NotificationProvider;
import com.feed.sdk.push.model.ModelNotification;

public class FeedNotificationDataManager {
    public static ModelNotification getNotificationData(@Nullable Bundle extras) {
        ModelNotification modelNotification = new ModelNotification();
        if (extras != null) {
            modelNotification = extras.getParcelable(NotificationProvider.NOTIFICATION_DATA);
        }
        return modelNotification;
    }
}
