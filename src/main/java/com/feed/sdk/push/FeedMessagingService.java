package com.feed.sdk.push;

import androidx.annotation.NonNull;

import com.feed.sdk.push.common.Logs;
import com.feed.sdk.push.common.Pref;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class FeedMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FeedMessagingService";
    public static final String FCM_TOKEN = "Feed_FCM_TOKEN";

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (FeedSDK.isEnabled())
            DataProvider.loadData(this);

    }


    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Logs.i("Token:", token);
        Pref.get(this).put(FCM_TOKEN, token);
        FeedRegisterManager.invoke(this);
    }
}
