package com.feed.sdk.push.utils;

import static com.feed.sdk.push.NotificationProvider.AD;

import com.feed.sdk.push.model.ModelNotification;

public class AdUtil {
    public static boolean isAd(final ModelNotification model) {
        return model.type.equalsIgnoreCase(AD);
    }
}
