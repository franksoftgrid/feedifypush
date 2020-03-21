package com.feed.sdk.push.model;

import android.content.Context;
import android.provider.Settings;

import com.feed.sdk.push.R;

/**
 * Model for device

 */
public class ModelDeviceApp {

    public String device_name;
    public String package_name;
    public String device_uuid;
    public String app_name;
    public String platform;


    public static ModelDeviceApp getInstance(Context context) {
        ModelDeviceApp modelDeviceApp = new ModelDeviceApp();
        modelDeviceApp.device_uuid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        modelDeviceApp.device_name = android.os.Build.MODEL;
        modelDeviceApp.platform = "android";
        modelDeviceApp.app_name = context.getString(R.string.app_name);
        modelDeviceApp.package_name = context.getPackageName();
        return modelDeviceApp;
    }


}
