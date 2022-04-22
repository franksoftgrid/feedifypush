package com.feed.sdk.push;

import android.app.Application;
import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.feed.sdk.push.common.Logs;
import com.feed.sdk.push.common.Pref;
import com.feed.sdk.push.exception.GoogleServiceJsonException;
import com.feed.sdk.push.model.ModelDeviceApp;
import com.feed.sdk.push.model.ModelFirebaseApp;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

/**
 * entry point for SDK, this class contains all the configuration related methods that needs
 * to be used at runtime
 */
public class FeedSDK extends Application {

    private static final String TAG = "FeedSDK";
    private static Context mContext;
    protected static Class activityClass;
    protected static @DrawableRes
    int notificationIcon = R.drawable.ic_notification;
    public static String mAppName = "";

    /**
     * sets the activity that needs to be started on click of the notification, and when set, passes all
     * the notification related data like payload, custom params, to the passed activity, inside Bundle
     *
     * @param activityClass - activity if set then all the notification related data will
     *                      be passed to the activity when notification is clicked
     */
    public static void setStartActivity(Class activityClass) {
        FeedSDK.activityClass = activityClass;
    }

    /**
     * icon to be used on the notification
     *
     * @param icon - drawable icon to be used as icon to be shown inside notifications
     */
    public static void setNotificationIcon(@DrawableRes int icon) {
        notificationIcon = icon;
    }

    /**
     * enables/ disables notifications
     *
     * @param enable - boolean value used to enable/ disable notifications
     */
    public static void setEnabled(boolean enable) {

        Pref.get(mContext).put(Const.PREF_ENABLE_KEY, enable);
    }

    /**
     * check if the notifications are enabled
     */
    public static boolean isEnabled() {
        return Pref.get(mContext).getBoolean(Const.PREF_ENABLE_KEY, true);
    }

    /**
     * used to initialise the SDK and Firebase
     *
     * @param ctx     - context, fragment or Activity
     * @param appName - app name
     */
    public static void init(Context ctx, String appName) {
        mContext = ctx;
        mAppName = appName;
        // Logs.setEnabled(BuildConfig.DEBUG);
        initializeApp(ctx, appName);
        Logs.i("ModelDeviceApp info...");
        try {
            ModelDeviceApp modelDeviceApp = ModelDeviceApp.getInstance(ctx);
            Logs.i("device_name", modelDeviceApp.device_name);
            Logs.i("device_uuid", modelDeviceApp.getDevice_uuid());
            Logs.i("package_name", modelDeviceApp.package_name);
            Logs.i("app_name", modelDeviceApp.app_name);
            Logs.i("platform", modelDeviceApp.platform);
        } catch (Exception ex) {
            Logs.e(ex.getMessage());
        }


        FeedRegisterManager.invoke(ctx);
    }

    private static void initializeApp(@NonNull Context context, String appName) {
        try {
            ModelFirebaseApp modelFirebaseApp = ModelFirebaseApp.getInstance(context);

            Logs.i("ModelFirebaseApp model initiated successfully...");
            Logs.i("api_key", modelFirebaseApp.api_key);
            Logs.i("firebase_url", modelFirebaseApp.firebase_url);
            Logs.i("mobilesdk_app_id", modelFirebaseApp.mobilesdk_app_id);
            Logs.i("project_number", modelFirebaseApp.project_number);
            Logs.i("storage_bucket", modelFirebaseApp.storage_bucket);


            FirebaseApp.initializeApp(context, new FirebaseOptions.Builder().
                    setApiKey(modelFirebaseApp.api_key).
                    setApplicationId(modelFirebaseApp.mobilesdk_app_id).
                    setDatabaseUrl(modelFirebaseApp.firebase_url).
                    setGcmSenderId(modelFirebaseApp.project_number).
                    setStorageBucket(modelFirebaseApp.storage_bucket).build(), appName);

        } catch (GoogleServiceJsonException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * provides Firebase device token unique for every device
     */
    public static String getToken(Context context) {
        return Pref.get(context).getString(FeedMessagingService.FCM_TOKEN, null);
    }


}
