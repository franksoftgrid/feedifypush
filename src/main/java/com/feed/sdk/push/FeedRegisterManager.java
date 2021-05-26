package com.feed.sdk.push;

import android.content.Context;


import androidx.annotation.NonNull;

import com.feed.sdk.push.common.Assets;
import com.feed.sdk.push.common.Logs;
import com.feed.sdk.push.common.Pref;
import com.feed.sdk.push.model.ModelDeviceApp;
import com.feed.sdk.push.net.FeedNet;
import com.feed.sdk.push.net.Request;
import com.feed.sdk.push.net.Response;
import com.feed.sdk.push.net.ResponseListener;

import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;

public class FeedRegisterManager {

    public static final String SETTINGS_FILE_NAME = "push-settings.json";
    public static final String KEY_PROJECT_INFO = "project_info";
    public static final String KEY_USER = "feedify_user";
    public static final String KEY_DKEY = "feedify_dkey";
    public static final String KEY_DOMAIN = "feedify_domain";

    private Context context;

    public FeedRegisterManager(Context context) {
        this.context = context;
    }

    public static void invoke(Context context) {
        String token = Pref.get(context).getString(FeedMessagingService.FCM_TOKEN, null);
        if (token != null) {
            FeedRegisterManager fm = new FeedRegisterManager(context);
            fm.register(context,  ModelDeviceApp.getInstance(context), token);
        }
    }

    public void register(final Context context, @NonNull final ModelDeviceApp modelDeviceApp, @NonNull final String token) {
        String pushSettingsJson = null;
        String feedify_user = "";
        String feedify_dkey = "";
        String feedify_domain = "";

        try {
            pushSettingsJson = Assets.textFileToString(context, SETTINGS_FILE_NAME);

            JSONObject root = new JSONObject(pushSettingsJson);
            JSONObject project_info = root.getJSONObject(KEY_PROJECT_INFO);

            feedify_user = project_info.getString(KEY_USER);
            feedify_dkey = project_info.getString(KEY_DKEY);
            feedify_domain = project_info.getString(KEY_DOMAIN);

        } catch (Exception e) {
            e.printStackTrace();
            Logs.e("Push-settings.json file in missing from assets folder.");
        }

        if (!feedify_dkey.trim().isEmpty() && !feedify_dkey.trim().isEmpty() && !feedify_domain.isEmpty()) {
            Logs.i("Registering token...", true);

            final String finalFeedify_user = feedify_user;
            final String finalFeedify_dkey = feedify_dkey;
            final String finalFeedify_domain = feedify_domain;

            // From here
            Map<String, String> MyData = new HashMap<String, String>();
            MyData.put("agent", "Feedify Android Application 1.0");
            MyData.put("endpoint", "https://fcm.googleapis.com/fcm/send/" + token);
            MyData.put("registration_id", token);
            MyData.put("user_id", finalFeedify_user);
            MyData.put("domkey", finalFeedify_dkey);
            MyData.put("referrer", finalFeedify_domain);
            MyData.put("browser", "Android Application");
            MyData.put("uuid", modelDeviceApp.device_uuid);

            Request req= new Request(Const.PUSH_REGISTER, Request.REQUEST_POST, MyData, new ResponseListener() {
                @Override
                public void onResponse(Response resp) {
                    if(!resp.isError()){
                        Logs.d("Token sent!!! "+ token);
                    }else{
                        Logs.e("That didn't work!");
                    }
                }
            });
          /*  MyData.put("token", token);
            Request req1= new Request(Const.TOKEN_SAVE, Request.REQUEST_POST, MyData, new ResponseListener() {
                @Override
                public void onResponse(Response resp) {
                    if(!resp.isError()){
                        Logs.d("Token sent!!!", token);
                    }else{
                        Logs.d("That didn't work!");
                    }
                }
            });*/
            FeedNet.getInstance(context).executeRequest(req);
           // FeedNet.getInstance(context).executeRequest(req1);

        } else {
            Logs.d( "Missing (feedify_user, feedify_dkey, feedify_domain) strings please make sure to update your feedify credentials in push-settings.json.");
        }
    }
}
