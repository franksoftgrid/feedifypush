package com.feed.sdk.push;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Looper;

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
        Pref.showToast(FeedMessagingService.this,"Push received");
        if (FeedSDK.isEnabled()){
            NotificationProvider.onMessageReceived(FeedMessagingService.this, remoteMessage);
        }

    }



    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Logs.d("Token:"+ token);
        Pref.get(this).put(FCM_TOKEN, token);
        FeedRegisterManager.invoke(this);
    }


}
