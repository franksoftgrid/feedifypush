package com.feed.sdk.push;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.feed.sdk.push.common.Logs;
import com.feed.sdk.push.common.Pref;
import com.feed.sdk.push.model.ModelDeviceApp;
import com.feed.sdk.push.net.FeedNet;
import com.feed.sdk.push.net.Request;
import com.feed.sdk.push.net.Response;
import com.feed.sdk.push.net.ResponseListener;

import java.util.HashMap;
import java.util.Map;

/**
 * used by SDK to register a device on Feedify server
 */
public class FeedRegisterManager {
    // keys used for fetching values from build.gradle file
    public static final String KEY_USER = "feedify_user";
    public static final String KEY_DKEY = "feedify_dkey";
    public static final String KEY_DOMAIN = "feedify_domain";

    private Context context;

    private FeedRegisterManager(Context context) {
        this.context = context;
    }

    /**
     * called by SDK only, to register a new device or when a new token is generated from Firebase,
     * this will update the token to feedify server
     *
     * @param context - context used to access token saved in the preferences
     */
    static void invoke(Context context) {
        String token = Pref.get(context).getString(FeedMessagingService.FCM_TOKEN, null);
        if (token != null) {
            FeedRegisterManager fm = new FeedRegisterManager(context);
            fm.register(context, ModelDeviceApp.getInstance(context), token);
        }
    }

    /**
     * called internally by SDK to fetch the feedify_domain, feedify_dkey and feedify_user values
     * and register to feedify server
     */
    private void register(final Context context, @NonNull final ModelDeviceApp modelDeviceApp, @NonNull final String token) {
        String pushSettingsJson = null;
        int feedify_user = 0;
        String feedify_dkey = "";
        String feedify_domain = "";

        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = appInfo.metaData;

            if (bundle != null) {
                if (bundle.containsKey(KEY_USER)) {
                    feedify_user = bundle.getInt(KEY_USER);
                }
                if (bundle.containsKey(KEY_DKEY)) {
                    feedify_dkey = bundle.getString(KEY_DKEY);
                }
                if (bundle.containsKey(KEY_DOMAIN)) {
                    feedify_domain = bundle.getString(KEY_DOMAIN);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Logs.e("Push-settings.json file in missing from assets folder.");
        }

        if (!feedify_dkey.trim().isEmpty() && !feedify_dkey.trim().isEmpty() && !feedify_domain.isEmpty()) {
            Logs.i("Registering token...");

            final int finalFeedify_user = feedify_user;
            final String finalFeedify_dkey = feedify_dkey;
            final String finalFeedify_domain = feedify_domain;

            // From here
            Map<String, String> MyData = new HashMap<String, String>();
            MyData.put("agent", "Feedify Android Application 1.0");
            MyData.put("endpoint", "https://fcm.googleapis.com/fcm/send/" + token);
            MyData.put("registration_id", token);
            MyData.put("user_id", String.valueOf(finalFeedify_user));
            MyData.put("domkey", finalFeedify_dkey);
            MyData.put("referrer", finalFeedify_domain);
            MyData.put("browser", "Android Application");
            MyData.put("uuid", modelDeviceApp.device_uuid);

            Request req = new Request(Const.PUSH_REGISTER, Request.REQUEST_POST, MyData, new ResponseListener() {
                @Override
                public void onResponse(Response resp) {
                    if (!resp.isError()) {
                        Logs.d("Token sent!!! " + token);
                    } else {
                        Logs.e("That didn't work!");
                    }
                }
            });
            FeedNet.getInstance(context).executeRequest(req);
        } else {
            Logs.d("Missing (feedify_user, feedify_dkey, feedify_domain) strings please make sure to update your feedify credentials in push-settings.json.");
        }
    }
}
