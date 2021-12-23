package com.feed.sdk.push.model;

import android.content.Context;
import android.provider.Settings;

import com.feed.sdk.push.R;
import com.feed.sdk.push.common.Logs;

/**
 * Model for device
 */
public class ModelDeviceApp {

    public String device_name = "";
    public String package_name = "";
    public String device_uuid = "dkfjdf-8458893434-dknfksdfk-49384";
    public String app_name = "";
    public String platform = "";

    private ModelDeviceApp() {
    }

    /**
     * returns ModelDeviceApp's object with values
     *
     * @return ModelDeviceApp
     */
    public static ModelDeviceApp getInstance(Context context) {
        ModelDeviceApp modelDeviceApp = new ModelDeviceApp();
        Logs.i(" Model device app initialized starts");
        modelDeviceApp.device_uuid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        modelDeviceApp.device_name = android.os.Build.MODEL;
        modelDeviceApp.platform = "android";
        modelDeviceApp.app_name = context.getString(R.string.app_name);
        modelDeviceApp.package_name = context.getPackageName();
        Logs.i(" Model device app initialized ends");

        return modelDeviceApp;
    }
}
