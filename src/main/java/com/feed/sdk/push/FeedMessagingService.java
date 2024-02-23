package com.feed.sdk.push;

import android.util.Log;

import androidx.annotation.NonNull;

import com.feed.sdk.push.common.Logs;
import com.feed.sdk.push.common.Pref;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * This class extends FirebaseMessagingService and receives messages sent from Feedify console
 */
public class FeedMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FeedMessagingService";
    public static final String FCM_TOKEN = "Feed_FCM_TOKEN";

    /**
     * called when a message is received
     *
     * @param remoteMessage - data is received as RemoteMessage object
     *                      ***Note that don't call this method explicitly***
     */
    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);





        Logs.d(TAG, "onMessageReceived :: " + " data :: " + remoteMessage.getData()
                + " getMessageId :: " + remoteMessage.getMessageId() +
                " getMessageType :: " + remoteMessage.getMessageType()
                + " getFrom :: " + remoteMessage.getFrom() + " getTo :: " + remoteMessage.getTo()
                + " getNotification :: " + remoteMessage.getNotification() + " getTtl :: " + remoteMessage.getTtl());

        if (FeedSDK.isEnabled(getApplicationContext())) {
            NotificationProvider.onMessageReceived(FeedMessagingService.this, remoteMessage);
        }

    }

    /**
     * called when a new token is generated, due to app install/ reinstall or whenever
     * anytime the registered token is refreshed on Firebase server, then is sent back to the clients
     *Using cache
     * @param token - data is received as a String, saved this token on server by means of API,
     *              so that a FCM message can be sent from server to any targeted device
     *              ***Note that don't call this method explicitly***
     */
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Logs.d(TAG, "Token:" + token);
        Pref.get(this).put(FCM_TOKEN, token);
        FeedRegisterManager.invoke(this);
    }


}
