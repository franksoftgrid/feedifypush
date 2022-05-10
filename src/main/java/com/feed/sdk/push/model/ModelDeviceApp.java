package com.feed.sdk.push.model;

import android.content.Context;
import android.provider.Settings;

import com.feed.sdk.push.FeedSDK;
import com.feed.sdk.push.R;
import com.feed.sdk.push.common.Logs;
import com.feed.sdk.push.utils.TextUtils;

/**
 * Model for device
 */
public class ModelDeviceApp {

    public String device_name = "";
    public String package_name = "";
    private String device_uuid = "dkfjdf-8458893434-dknfksdfk-49384";
    public String app_name = "";
    public String platform = "";
    private static volatile ModelDeviceApp INSTANCE = null;

    private ModelDeviceApp() {
    }

    /**
     * returns ModelDeviceApp's object with values
     *
     * @return ModelDeviceApp
     */
    public static ModelDeviceApp getInstance(Context context) {
        synchronized (ModelDeviceApp.class) {
            if (INSTANCE == null) {
                INSTANCE = new ModelDeviceApp();
                Logs.i(" Model device app initialized starts");
                INSTANCE.device_uuid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                INSTANCE.device_name = android.os.Build.MODEL;
                INSTANCE.platform = "android";
                INSTANCE.app_name = context.getString(R.string.app_name);
                INSTANCE.package_name = context.getPackageName();
                Logs.i(" Model device app initialized ends");
            }
            return INSTANCE;
        }
    }

    public String getDevice_uuid() {
        return device_uuid + TextUtils.UNDERSCORE + getAppName();
    }

    private String getAppName() {
        return FeedSDK.mAppName.trim().replaceAll(" ", TextUtils.UNDERSCORE);
    }
}
