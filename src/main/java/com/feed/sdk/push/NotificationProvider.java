package com.feed.sdk.push;

import static com.feed.sdk.push.utils.AdUtil.isAd;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.feed.sdk.push.activity.FeedSDKActionButton;
import com.feed.sdk.push.common.Logs;
import com.feed.sdk.push.model.ModelNotification;
import com.feed.sdk.push.net.FeedNet;
import com.feed.sdk.push.net.Request;
import com.feed.sdk.push.net.Response;
import com.feed.sdk.push.net.ResponseListener;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NotificationProvider {

    private ModelNotification model;
    private static final String TAG = NotificationProvider.class.getName();
    private static final String ADS_TAG = "AdsLog";

    public static final String NOTIFICATION_KEY = "notificationId";
    public static final String NOTIFICATION_TYPE = "notificationType";
    public static final String NOTIFICATION_DATA = "notificationData";
    public static final String AD = "ad";

    /**
     * private constructor to stop initialising from outside the SDK
     */
    private NotificationProvider() {
    }

    /**
     * private constructor for internal use
     *
     * @param model
     */
    NotificationProvider(ModelNotification model, Context context) {
        this.model = model;
//        buttonReceiver = new ButtonReceiver();
//        registerBroadCastReceiver(context);
    }

    Bitmap icon = null;
    Bitmap image = null;
    Notification notification;

    private void showNotification(final Context context) {
        callNotificationReceivedApi(model, context);
        String feed_channel = "FEEDIFY_CHANNEL";
        try {
            createNotificationChannel(context, feed_channel, feed_channel, "Feed notification channel.", NotificationManager.IMPORTANCE_DEFAULT);
        } catch (Exception ex) {
            Log.d(TAG, "========================================================************============================");
            Log.d(TAG, "Notification shown success: ");
            Log.d(TAG, "========================================================************============================");
        }
        Log.d(TAG, "========================================================************============================");
        Log.d(TAG, "Inside showNotification");
        Log.d(TAG, "========================================================************============================");
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, feed_channel)
                .setSmallIcon(FeedSDK.notificationIcon)
                .setContentTitle(model.title)
                .setContentText(model.body)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(model.body))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        Logs.d(TAG, model.toString());
//        Log.d(TAG, "========================================================************============================");
//        Log.d(TAG, "Model Details: ");
//        Log.d(TAG, "Model id: " + model.id);
//        Log.d(TAG, "Model icon: " + model.icon);
//        Log.d(TAG, "Model image: " + model.image);
//        Log.d(TAG, "Model tile: " + model.title);
//        Log.d(TAG, "Model body: " + model.body);
//        Log.d(TAG, "========================================================************============================");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, model.id, getIntent(context), getPendingIntent());
        builder.setContentIntent(pendingIntent);
        int i = getRandomNumber();
        // Add push buttons

        //Create an Intent for the BroadcastReceiver
//        Intent buttonIntent = new Intent(context, ButtonReceiver.class);
//        buttonIntent.putExtra("notificationId", model.id);


        for (ModelNotification.ActionButton button : model.actionButtons) {
            if (!button.getTitle().equals("")) {
                PendingIntent pi = PendingIntent.getActivity(context, model.id, getPushButtonIntent2(context, button), getPendingIntent());
                NotificationCompat.Action action = new NotificationCompat.Action.Builder(getActionIcon(button.getIcon()), button.getTitle(), pi).build();
                builder.setAutoCancel(true);
                builder.addAction(action);
                i += 1;
            }
        }
        int pushButtonCount = model.pushButtons.size();


        // If push buttons are less than two and there are actions buttons add those
        if (model.pushButtons.size() < 2) {
            for (ModelNotification.ActionButton button : model.actionButtons) {
                if (!button.getTitle().equals("")) {
                    PendingIntent pi = PendingIntent.getBroadcast(context, i, getButtonIntent(context, button), getPendingIntent());
                    NotificationCompat.Action action = new NotificationCompat.Action.Builder(getActionIcon(button.getIcon()), button.getTitle(), pi).build();
                    builder.setAutoCancel(true);
                    builder.addAction(action);
                    i += 1;
                    pushButtonCount += 1;
                    if (pushButtonCount == 2) {
                        break;
                    }
                }
            }
        }


        if (model.icon != null) {
            new FetchIconTask(context, model.icon, builder).execute("");
        }


    }

    private int getPendingIntent() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            return PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT;
        } else {
            return PendingIntent.FLAG_UPDATE_CURRENT;
        }
    }

    private int getRandomNumber() {
        Random rand = new Random();
        return rand.nextInt(1000);
    }

//    private void registerBroadCastReceiver(final Context context) {
//        IntentFilter filter = new IntentFilter();
//        context.registerReceiver(buttonReceiver, filter);
//    }


    private class FetchIconTask extends AsyncTask<String, Void, Bitmap> {

        Context ctx;
        String urlIcon;
        NotificationCompat.Builder builder;

        public FetchIconTask(Context context, String icon, NotificationCompat.Builder builder) {
            super();
            this.ctx = context;
            urlIcon = icon;
            this.builder = builder;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Log.d(TAG, "========================================================************============================");
            Log.d(TAG, "Inside Icon fetch");
            Log.d(TAG, "========================================================************============================");
            InputStream in;

            try {
                URL url = new URL(model.icon);
                icon = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return icon;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            super.onPostExecute(result);
            Log.d(TAG, "========================================================************============================");
            Log.d(TAG, "Icon fetch completed");
            Log.d(TAG, "========================================================************============================");
            new FetchImageTask(ctx, model.image, builder).execute("");

        }
    }

    private class FetchImageTask extends AsyncTask<String, Void, Bitmap> {

        Context ctx;
        String urlImage;
        NotificationCompat.Builder builder;

        public FetchImageTask(Context context, String icon, NotificationCompat.Builder builder) {
            super();
            this.ctx = context;
            urlImage = icon;
            this.builder = builder;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Log.d(TAG, "========================================================************============================");
            Log.d(TAG, "Inside image fetch");
            Log.d(TAG, "========================================================************============================");
            InputStream in;

            try {
                if (model.image != null && !model.image.isEmpty()) {
                    URL url = new URL(model.image);
                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                }
                return image;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            super.onPostExecute(result);
            builder.setLargeIcon(icon);
            if (image != null)
                builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image));
            notification = builder.build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            NotificationManagerCompat.from(ctx).notify(model.id, notification);
            Log.d(TAG, "========================================================************============================");
            Log.d(TAG, "Notification shown success: ");
            Log.d(TAG, "========================================================************============================");

        }
    }

    private Intent getIntent(Context context) {
        if (FeedSDK.activityClass != null && model.customParams != null && !model.customParams.isEmpty()) {
            Intent intent = new Intent(context, FeedSDK.activityClass);
            for (String key : model.customParams.keySet()) {
                intent.putExtra(key, model.customParams.get(key));
            }
            if (!isAd(model)) {
                intent.putExtra(NOTIFICATION_DATA, model);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            return intent;
        } else if (model.url != null) {
            Intent intent;
            if (FeedSDK.activityClass != null && !isAd(model)) {
                intent = new Intent(context, FeedSDK.activityClass);
                intent.putExtra(NOTIFICATION_DATA, model);
            } else {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(model.url));
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            return intent;

        } else {
            PackageManager pm = context.getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage(context.getPackageName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            return intent;
        }
    }

    private Intent getButtonIntent(Context ctx, ModelNotification.ActionButton b) {
        Intent intent = new Intent(ctx, FeedSDKActionButton.ButtonReceiver.class);
        intent.putExtra(NOTIFICATION_KEY, model.id);
        intent.putExtra(NOTIFICATION_TYPE, model.type);
        intent.setData(Uri.parse(b.getAction()));
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    private Intent getPushButtonIntent(Context ctx, ModelNotification.ActionButton b) {
        Intent intent = null;
        if (FeedSDK.activityClass != null) {
            intent = new Intent(ctx, FeedSDK.activityClass);
            for (String key : model.customParams.keySet()) {
                intent.putExtra(key, model.customParams.get(key));
            }
            intent.putExtra(b.getTitle(), b.getAction());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(b.getAction()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        return intent;
    }


    private Intent getPushButtonIntent2(Context ctx, ModelNotification.ActionButton b) {
        Intent intent = null;
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(b.getAction()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }
    public static void onMessageReceived(final Context context, RemoteMessage remoteMessage) {
        try {
            Log.d(TAG, "========================================================************============================");
            Log.d(TAG, "Inside onMessage received " );
            Log.d(TAG, "========================================================************============================");

            Map<String ,String>   jsonDataStr = remoteMessage.getData();


            ModelNotification model = ModelNotification.getInstance(new JSONObject(jsonDataStr));




            if (isAd(model) && model.title.isEmpty()) {
                Logs.d(ADS_TAG, "inside ad, fetching API...");
                JSONObject payloadObj = new JSONObject();
                payloadObj.put("notification", new JSONObject(jsonDataStr));
                payloadObj.put("to", "https://fcm.googleapis.com/fcm/send/" + FeedSDK.getToken(context));

               Log.e(ADS_TAG , payloadObj.toString());
                Log.e(ADS_TAG , Const.FETCH_AD);

                Request req = new Request(Const.FETCH_AD, Request.REQUEST_POST, payloadObj, new ResponseListener() {
                    @Override
                    public void onResponse(Response resp) {
                        Logs.d(ADS_TAG, "ads response received :: " + resp.getData());
                        if (!resp.isError()) {
                            Logs.d(ADS_TAG, "Token sent!!! ");
                            String data = resp.getData();
                            try {
                                JSONObject responseObject = new JSONObject(data);
                                ModelNotification internalModel = ModelNotification.getInstance(responseObject);
                                NotificationProvider np = new NotificationProvider(internalModel, context);
                                np.showNotification(context);
                            } catch (JSONException e) {
                                Logs.e(ADS_TAG, "json parsing exception occurred :: ");
                                e.printStackTrace();
                                Log.d(ADS_TAG, "========================================================************============================");
                                Log.d(ADS_TAG, "Exception: " + e.getMessage());
                                Log.d(ADS_TAG, "========================================================************============================");
                            }
                        } else {
                            Logs.e(ADS_TAG, "That didn't work!");
                        }
                    }
                });
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=UTF-8");
                req.setHeaders(headers);
                Logs.d(ADS_TAG, " ad API request params :: " + payloadObj.toString());
                FeedNet.getInstance(context).executeRequest(req);
            } else if (remoteMessage.getData().isEmpty()) {
                new CallNotificationReceivedApi(0, "", CallNotificationReceivedApi.ApiCall.PAYLOAD_DATA, context).execute();
            } else {
                NotificationProvider np = new NotificationProvider(model, context);
                np.showNotification(context);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "========================================================************============================");
            Log.d(TAG, "Exception: " + e.getMessage());
            Log.d(TAG, "========================================================************============================");

        }
    }

    private static void callNotificationReceivedApi(final ModelNotification model, final Context context) {
        new CallNotificationReceivedApi(model.id, model.type, CallNotificationReceivedApi.ApiCall.DELIVERY_COUNT, context).execute();
    }

    private static String getDeliveryCountUrlParams(int id, String type) {
        try {
            return "type=" + URLEncoder.encode(type, "UTF-8") + "&id=" + URLEncoder.encode(String.valueOf(id), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void createNotificationChannel(Context context, String channel_id, String channel_name, String channel_description, int importance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance1 = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channel_id, channel_name, importance1);
            channel.setDescription(channel_description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * icon images
     *
     * @param name image name from push data
     * @return image resource
     */
    private int getActionIcon(String name) {
        if (name == null) {
            return R.drawable.add;
        }
        switch (name) {
            case "television-1":
                return R.drawable.television_1;
            case "id-card-5":
                return R.drawable.id_card_5;
            case "info":
                return R.drawable.info;
            case "add":
                return R.drawable.add;
            case "flag-2":
                return R.drawable.flag_2;
            case "broken-link":
                return R.drawable.broken_link;
            case "folder-11":
                return R.drawable.folder_11;
            case "fast-forward":
                return R.drawable.fast_forward;
            case "star-1":
                return R.drawable.star_1;
            case "video-camera-1":
                return R.drawable.video_camera_1;
            case "controls-5":
                return R.drawable.controls_5;
            case "smartphone-6":
                return R.drawable.smartphone_6;
            case "cloud-computing-4":
                return R.drawable.cloud_computing_4;
            case "internet":
                return R.drawable.internet;
            case "wifi":
                return R.drawable.wifi;
            case "warning":
                return R.drawable.warning;
            case "cloud-computing-5":
                return R.drawable.cloud_computing_5;
            case "calendar-1":
                return R.drawable.calendar_1;
            case "smartphone-7":
                return R.drawable.smartphone_7;
            case "controls-4":
                return R.drawable.controls_4;
            case "document":
                return R.drawable.document;
            case "locked-6":
                return R.drawable.locked_6;
            case "plus":
                return R.drawable.plus;
            case "windows-1":
                return R.drawable.windows_1;
            case "bluetooth":
                return R.drawable.bluetooth;
            case "street":
                return R.drawable.street;
            case "focus":
                return R.drawable.focus;
            case "video-player-1":
                return R.drawable.video_player_1;
            case "folder-10":
                return R.drawable.folder_10;
            case "fax":
                return R.drawable.fax;
            case "flag-3":
                return R.drawable.flag_3;
            case "tabs":
                return R.drawable.tabs;
            case "sign-1":
                return R.drawable.sign_1;
            case "id-card-4":
                return R.drawable.id_card_4;
            case "record":
                return R.drawable.record;
            case "search-1":
                return R.drawable.search_1;
            case "newspaper":
                return R.drawable.newspaper;
            case "stop":
                return R.drawable.stop;
            case "speaker-8":
                return R.drawable.speaker_8;
            case "flag-1":
                return R.drawable.flag_1;
            case "muted":
                return R.drawable.muted;
            case "folder-12":
                return R.drawable.folder_12;
            case "notepad":
                return R.drawable.notepad;
            case "windows-3":
                return R.drawable.windows_3;
            case "battery":
                return R.drawable.battery;
            case "locked-4":
                return R.drawable.locked_4;
            case "smartphone-5":
                return R.drawable.smartphone_5;
            case "controls-6":
                return R.drawable.controls_6;
            case "calendar-3":
                return R.drawable.calendar_3;
            case "flag":
                return R.drawable.flag;
            case "music-player":
                return R.drawable.music_player;
            case "calendar-2":
                return R.drawable.calendar_2;
            case "controls-7":
                return R.drawable.controls_7;
            case "smartphone-4":
                return R.drawable.smartphone_4;
            case "menu-4":
                return R.drawable.menu_4;
            case "locked-5":
                return R.drawable.locked_5;
            case "funnel":
                return R.drawable.funnel;
            case "windows-2":
                return R.drawable.windows_2;
            case "dislike-1":
                return R.drawable.dislike_1;
            case "video-player-2":
                return R.drawable.video_player_2;
            case "layers":
                return R.drawable.layers;
            case "folder-13":
                return R.drawable.folder_13;
            case "login":
                return R.drawable.login;
            case "app":
                return R.drawable.app;
            case "id-card-3":
                return R.drawable.id_card_3;
            case "server":
                return R.drawable.server;
            case "folder-17":
                return R.drawable.folder_17;
            case "flag-4":
                return R.drawable.flag_4;
            case "note":
                return R.drawable.note;
            case "pin":
                return R.drawable.pin;
            case "time":
                return R.drawable.time;
            case "download":
                return R.drawable.download;
            case "controls-3":
                return R.drawable.controls_3;
            case "locked-1":
                return R.drawable.locked_1;
            case "incoming":
                return R.drawable.incoming;
            case "cloud-computing-2":
                return R.drawable.cloud_computing_2;
            case "calendar-6":
                return R.drawable.calendar_6;
            case "map-2":
                return R.drawable.map_2;
            case "stop-1":
                return R.drawable.stop_1;
            case "cloud-computing-3":
                return R.drawable.cloud_computing_3;
            case "calendar-7":
                return R.drawable.calendar_7;
            case "menu-1":
                return R.drawable.menu_1;
            case "controls-2":
                return R.drawable.controls_2;
            case "smartphone-1":
                return R.drawable.smartphone_1;
            case "compose":
                return R.drawable.compose;
            case "magnet":
                return R.drawable.magnet;
            case "exit-2":
                return R.drawable.exit_2;
            case "edit-1":
                return R.drawable.edit_1;
            case "diploma":
                return R.drawable.diploma;
            case "archive":
                return R.drawable.archive;
            case "folder-16":
                return R.drawable.folder_16;
            case "music-player-1":
                return R.drawable.music_player_1;
            case "blueprint":
                return R.drawable.blueprint;
            case "settings":
                return R.drawable.settings;
            case "stopwatch":
                return R.drawable.stopwatch;
            case "id-card-2":
                return R.drawable.id_card_2;
            case "alarm-1":
                return R.drawable.alarm_1;
            case "music-player-3":
                return R.drawable.music_player_3;
            case "folder-14":
                return R.drawable.folder_14;
            case "dislike":
                return R.drawable.dislike;
            case "key":
                return R.drawable.key;
            case "smartphone-3":
                return R.drawable.smartphone_3;
            case "menu-3":
                return R.drawable.menu_3;
            case "percent-1":
                return R.drawable.percent_1;
            case "wifi-1":
                return R.drawable.wifi_1;
            case "locked-2":
                return R.drawable.locked_2;
            case "link":
                return R.drawable.link;
            case "instagram":
                return R.drawable.instagram;
            case "map-1":
                return R.drawable.map_1;
            case "calendar-5":
                return R.drawable.calendar_5;
            case "cloud-computing-1":
                return R.drawable.cloud_computing_1;
            case "calendar-4":
                return R.drawable.calendar_4;
            case "divide":
                return R.drawable.divide;
            case "menu-2":
                return R.drawable.menu_2;
            case "locked-3":
                return R.drawable.locked_3;
            case "smartphone-2":
                return R.drawable.smartphone_2;
            case "controls-1":
                return R.drawable.controls_1;
            case "windows-4":
                return R.drawable.windows_4;
            case "exit-1":
                return R.drawable.exit_1;
            case "id-card":
                return R.drawable.id_card;
            case "folder-15":
                return R.drawable.folder_15;
            case "music-player-2":
                return R.drawable.music_player_2;
            case "lock":
                return R.drawable.lock;
            case "alarm-clock":
                return R.drawable.alarm_clock;
            case "google-plus":
                return R.drawable.google_plus;
            case "id-card-1":
                return R.drawable.id_card_1;
            case "vk":
                return R.drawable.vk;
            case "wireless-internet":
                return R.drawable.wireless_internet;
            case "database-3":
                return R.drawable.database_3;
            case "share-1":
                return R.drawable.share_1;
            case "stopwatch-3":
                return R.drawable.stopwatch_3;
            case "radar":
                return R.drawable.radar;
            case "settings-3":
                return R.drawable.settings_3;
            case "switch-7":
                return R.drawable.switch_7;
            case "placeholder-1":
                return R.drawable.placeholder_1;
            case "zoom-in":
                return R.drawable.zoom_in;
            case "file":
                return R.drawable.file;
            case "switch-6":
                return R.drawable.switch_6;
            case "settings-2":
                return R.drawable.settings_2;
            case "battery-1":
                return R.drawable.battery_1;
            case "stopwatch-2":
                return R.drawable.stopwatch_2;
            case "database":
                return R.drawable.database;
            case "power":
                return R.drawable.power;
            case "microphone":
                return R.drawable.microphone;
            case "database-2":
                return R.drawable.database_2;
            case "map":
                return R.drawable.map;
            case "checked-1":
                return R.drawable.checked_1;
            case "share-2":
                return R.drawable.share_2;
            case "briefcase":
                return R.drawable.briefcase;
            case "battery-3":
                return R.drawable.battery_3;
            case "hold":
                return R.drawable.hold;
            case "notebook-5":
                return R.drawable.notebook_5;
            case "switch-4":
                return R.drawable.switch_4;
            case "placeholder-2":
                return R.drawable.placeholder_2;
            case "folder-9":
                return R.drawable.folder_9;
            case "multiply":
                return R.drawable.multiply;
            case "shuffle-1":
                return R.drawable.shuffle_1;
            case "play-button-1":
                return R.drawable.play_button_1;
            case "folder-8":
                return R.drawable.folder_8;
            case "placeholder-3":
                return R.drawable.placeholder_3;
            case "switch-5":
                return R.drawable.switch_5;
            case "television":
                return R.drawable.television;
            case "notebook-4":
                return R.drawable.notebook_4;
            case "settings-1":
                return R.drawable.settings_1;
            case "smartphone":
                return R.drawable.smartphone;
            case "eject":
                return R.drawable.eject;
            case "equal":
                return R.drawable.equal;
            case "stopwatch-1":
                return R.drawable.stopwatch_1;
            case "battery-2":
                return R.drawable.battery_2;
            case "database-1":
                return R.drawable.database_1;
            case "cloud-computing":
                return R.drawable.cloud_computing;
            case "hourglass-1":
                return R.drawable.hourglass_1;
            case "battery-6":
                return R.drawable.battery_6;
            case "home":
                return R.drawable.home;
            case "switch-1":
                return R.drawable.switch_1;
            case "settings-5":
                return R.drawable.settings_5;
            case "alarm-clock-1":
                return R.drawable.alarm_clock_1;
            case "umbrella":
                return R.drawable.umbrella;
            case "user":
                return R.drawable.user;
            case "deviantart":
                return R.drawable.deviantart;
            case "list-1":
                return R.drawable.list_1;
            case "file-1":
                return R.drawable.file_1;
            case "lastfm":
                return R.drawable.lastfm;
            case "bookmark-1":
                return R.drawable.bookmark_1;
            case "notebook-1":
                return R.drawable.notebook_1;
            case "settings-4":
                return R.drawable.settings_4;
            case "repeat":
                return R.drawable.repeat;
            case "stopwatch-4":
                return R.drawable.stopwatch_4;
            case "battery-7":
                return R.drawable.battery_7;
            case "checked":
                return R.drawable.checked;
            case "hourglass":
                return R.drawable.hourglass;
            case "price-tag":
                return R.drawable.price_tag;
            case "target":
                return R.drawable.target;
            case "restart":
                return R.drawable.restart;
            case "hourglass-2":
                return R.drawable.hourglass_2;
            case "battery-5":
                return R.drawable.battery_5;
            case "switch-2":
                return R.drawable.switch_2;
            case "repeat-1":
                return R.drawable.repeat_1;
            case "settings-6":
                return R.drawable.settings_6;
            case "notebook-3":
                return R.drawable.notebook_3;
            case "house":
                return R.drawable.house;
            case "microphone-1":
                return R.drawable.microphone_1;
            case "division":
                return R.drawable.division;
            case "tumblr":
                return R.drawable.tumblr;
            case "skip":
                return R.drawable.skip;
            case "like":
                return R.drawable.like;
            case "file-2":
                return R.drawable.file_2;
            case "megaphone":
                return R.drawable.megaphone;
            case "sign":
                return R.drawable.sign;
            case "list":
                return R.drawable.list;
            case "notebook-2":
                return R.drawable.notebook_2;
            case "settings-7":
                return R.drawable.settings_7;
            case "switch-3":
                return R.drawable.switch_3;
            case "push-pin":
                return R.drawable.push_pin;
            case "battery-4":
                return R.drawable.battery_4;
            case "hourglass-3":
                return R.drawable.hourglass_3;
            case "resume":
                return R.drawable.resume;
            case "search":
                return R.drawable.search;
            case "notebook":
                return R.drawable.notebook;
            case "volume-control-1":
                return R.drawable.volume_control_1;
            case "battery-9":
                return R.drawable.battery_9;
            case "placeholder":
                return R.drawable.placeholder;
            case "volume-control":
                return R.drawable.volume_control;
            case "smartphone-11":
                return R.drawable.smartphone_11;
            case "unlocked-1":
                return R.drawable.unlocked_1;
            case "behance":
                return R.drawable.behance;
            case "film":
                return R.drawable.film;
            case "print":
                return R.drawable.print;
            case "folder-3":
                return R.drawable.folder_3;
            case "more-1":
                return R.drawable.more_1;
            case "flickr":
                return R.drawable.flickr;
            case "server-1":
                return R.drawable.server_1;
            case "user-2":
                return R.drawable.user_2;
            case "notepad-1":
                return R.drawable.notepad_1;
            case "perspective":
                return R.drawable.perspective;
            case "pause-1":
                return R.drawable.pause_1;
            case "add-3":
                return R.drawable.add_3;
            case "bluetooth-1":
                return R.drawable.bluetooth_1;
            case "add-2":
                return R.drawable.add_2;
            case "users":
                return R.drawable.users;
            case "shutdown":
                return R.drawable.shutdown;
            case "alarm":
                return R.drawable.alarm;
            case "folder":
                return R.drawable.folder;
            case "user-3":
                return R.drawable.user_3;
            case "binoculars":
                return R.drawable.binoculars;
            case "minus":
                return R.drawable.minus;
            case "folder-2":
                return R.drawable.folder_2;
            case "substract":
                return R.drawable.substract;
            case "garbage":
                return R.drawable.garbage;
            case "home-2":
                return R.drawable.home_2;
            case "fingerprint":
                return R.drawable.fingerprint;
            case "multiply-1":
                return R.drawable.multiply_1;
            case "smartphone-10":
                return R.drawable.smartphone_10;
            case "eyeglasses":
                return R.drawable.eyeglasses;
            case "locked":
                return R.drawable.locked;
            case "battery-8":
                return R.drawable.battery_8;
            case "zoom-out":
                return R.drawable.zoom_out;
            case "menu":
                return R.drawable.menu;
            case "calculator":
                return R.drawable.calculator;
            case "settings-9":
                return R.drawable.settings_9;
            case "unlocked-2":
                return R.drawable.unlocked_2;
            case "more-2":
                return R.drawable.more_2;
            case "photo-camera-1":
                return R.drawable.photo_camera_1;
            case "server-2":
                return R.drawable.server_2;
            case "user-1":
                return R.drawable.user_1;
            case "placeholders":
                return R.drawable.placeholders;
            case "notepad-2":
                return R.drawable.notepad_2;
            case "help":
                return R.drawable.help;
            case "add-1":
                return R.drawable.add_1;
            case "bookmark":
                return R.drawable.bookmark;
            case "lock-1":
                return R.drawable.lock_1;
            case "tabs-1":
                return R.drawable.tabs_1;
            case "server-3":
                return R.drawable.server_3;
            case "folder-1":
                return R.drawable.folder_1;
            case "home-1":
                return R.drawable.home_1;
            case "settings-8":
                return R.drawable.settings_8;
            case "error":
                return R.drawable.error;
            case "shuffle":
                return R.drawable.shuffle;
            case "pause":
                return R.drawable.pause;
            case "upload":
                return R.drawable.upload;
            case "rewind":
                return R.drawable.rewind;
            case "equal-2":
                return R.drawable.equal_2;
            case "street-1":
                return R.drawable.street_1;
            case "cloud":
                return R.drawable.cloud;
            case "minus-1":
                return R.drawable.minus_1;
            case "soundcloud":
                return R.drawable.soundcloud;
            case "route":
                return R.drawable.route;
            case "folder-5":
                return R.drawable.folder_5;
            case "user-4":
                return R.drawable.user_4;
            case "fast-forward-1":
                return R.drawable.fast_forward_1;
            case "twitter":
                return R.drawable.twitter;
            case "divide-1":
                return R.drawable.divide_1;
            case "user-5":
                return R.drawable.user_5;
            case "calendar":
                return R.drawable.calendar;
            case "folder-4":
                return R.drawable.folder_4;
            case "view":
                return R.drawable.view;
            case "linkedin":
                return R.drawable.linkedin;
            case "video-camera":
                return R.drawable.video_camera;
            case "like-2":
                return R.drawable.like_2;
            case "swarm":
                return R.drawable.swarm;
            case "equal-1":
                return R.drawable.equal_1;
            case "gift":
                return R.drawable.gift;
            case "share":
                return R.drawable.share;
            case "badoo":
                return R.drawable.badoo;
            case "magic-wand":
                return R.drawable.magic_wand;
            case "folder-6":
                return R.drawable.folder_6;
            case "dribbble":
                return R.drawable.dribbble;
            case "user-7":
                return R.drawable.user_7;
            case "video-player":
                return R.drawable.video_player;
            case "success":
                return R.drawable.success;
            case "user-6":
                return R.drawable.user_6;
            case "folder-7":
                return R.drawable.folder_7;
            case "forbidden":
                return R.drawable.forbidden;
            case "like-1":
                return R.drawable.like_1;
            case "clock":
                return R.drawable.clock;
            case "diamond":
                return R.drawable.diamond;
            case "command":
                return R.drawable.command;
            case "exit":
                return R.drawable.exit;
            case "archive-2":
                return R.drawable.archive_2;
            case "layers-1":
                return R.drawable.layers_1;
            case "speaker-2":
                return R.drawable.speaker_2;
            case "folder-18":
                return R.drawable.folder_18;
            case "controls":
                return R.drawable.controls;
            case "idea":
                return R.drawable.idea;
            case "compact-disc":
                return R.drawable.compact_disc;
            case "compass":
                return R.drawable.compass;
            case "windows":
                return R.drawable.windows;
            case "reading":
                return R.drawable.reading;
            case "paper-plane":
                return R.drawable.paper_plane;
            case "unlink":
                return R.drawable.unlink;
            case "edit":
                return R.drawable.edit;
            case "navigation-1":
                return R.drawable.navigation_1;
            case "folder-19":
                return R.drawable.folder_19;
            case "worldwide":
                return R.drawable.worldwide;
            case "speaker-3":
                return R.drawable.speaker_3;
            case "archive-3":
                return R.drawable.archive_3;
            case "next":
                return R.drawable.next;
            case "star":
                return R.drawable.star;
            case "photo-camera":
                return R.drawable.photo_camera;
            case "archive-1":
                return R.drawable.archive_1;
            case "speaker-1":
                return R.drawable.speaker_1;
            case "albums":
                return R.drawable.albums;
            case "trash":
                return R.drawable.trash;
            case "paper-plane-1":
                return R.drawable.paper_plane_1;
            case "network":
                return R.drawable.network;
            case "rewind-1":
                return R.drawable.rewind_1;
            case "eject-1":
                return R.drawable.eject_1;
            case "more":
                return R.drawable.more;
            case "attachment":
                return R.drawable.attachment;
            case "infinity":
                return R.drawable.infinity;
            case "mute":
                return R.drawable.mute;
            case "pinterest":
                return R.drawable.pinterest;
            case "facebook":
                return R.drawable.facebook;
            case "speaker":
                return R.drawable.speaker;
            case "users-1":
                return R.drawable.users_1;
            case "speaker-4":
                return R.drawable.speaker_4;
            case "play-button":
                return R.drawable.play_button;
            case "unlocked":
                return R.drawable.unlocked;
            case "garbage-1":
                return R.drawable.garbage_1;
            case "picture":
                return R.drawable.picture;
            case "notification":
                return R.drawable.notification;
            case "view-2":
                return R.drawable.view_2;
            case "photos":
                return R.drawable.photos;
            case "compact-disc-2":
                return R.drawable.compact_disc_2;
            case "smartphone-9":
                return R.drawable.smartphone_9;
            case "picture-2":
                return R.drawable.picture_2;
            case "send":
                return R.drawable.send;
            case "agenda":
                return R.drawable.agenda;
            case "smartphone-8":
                return R.drawable.smartphone_8;
            case "clock-1":
                return R.drawable.clock_1;
            case "magnet-2":
                return R.drawable.magnet_2;
            case "calculator-1":
                return R.drawable.calculator_1;
            case "percent":
                return R.drawable.percent;
            case "map-location":
                return R.drawable.map_location;
            case "previous":
                return R.drawable.previous;
            case "speaker-5":
                return R.drawable.speaker_5;
            case "spotlight":
                return R.drawable.spotlight;
            case "back":
                return R.drawable.back;
            case "worldwide-1":
                return R.drawable.worldwide_1;
            case "navigation":
                return R.drawable.navigation;
            case "substract-1":
                return R.drawable.substract_1;
            case "lamp":
                return R.drawable.lamp;
            case "save":
                return R.drawable.save;
            case "speaker-7":
                return R.drawable.speaker_7;
            case "garbage-2":
                return R.drawable.garbage_2;
            case "hide":
                return R.drawable.hide;
            case "view-1":
                return R.drawable.view_1;
            case "controls-9":
                return R.drawable.controls_9;
            case "compact-disc-1":
                return R.drawable.compact_disc_1;
            case "picture-1":
                return R.drawable.picture_1;
            case "controls-8":
                return R.drawable.controls_8;
            case "megaphone-1":
                return R.drawable.megaphone_1;
            case "magnet-1":
                return R.drawable.magnet_1;
            case "close":
                return R.drawable.close;
            case "speaker-6":
                return R.drawable.speaker_6;
        }
        return R.drawable.add;
    }

    private static class CallNotificationReceivedApi extends AsyncTask<Void, Void, Void> {
        private final int id;
        private final String type;
        private final ApiCall apiCall;
        private final Context context;

        enum ApiCall {
            DELIVERY_COUNT,
            PAYLOAD_DATA
        }

        public CallNotificationReceivedApi(int id, String type, ApiCall apiCall, Context context)
        {
            this.id = id;
            this.type = type;
            this.apiCall = apiCall;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String query = "";
                String url = "";
                if (apiCall == ApiCall.DELIVERY_COUNT) {
                    query = getDeliveryCountUrlParams(id, type);
                    url = Const.DELIVERY_COUNT_API;
                } else {
                    query = getPayloadDataUrlParams(context);
                    url = Const.PAYLOAD_DATA_API;
                }

                URLConnection connection = new URL(url + "?" + query).openConnection();
                connection.setRequestProperty("Accept-Charset", "UTF-8");

                InputStream response = connection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(response));

                String line = "";
                String message = "";

                while ((line = reader.readLine()) != null) {
                    message += line;
                }


                if (apiCall == ApiCall.PAYLOAD_DATA) {
                    ModelNotification model = ModelNotification.getInstance(new JSONObject(message));
                    if (model.id != 0) {
                        NotificationProvider np = new NotificationProvider(model, context);
                        np.showNotification(context);
                    }
                }
                Logs.d(TAG, message);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        private String getPayloadDataUrlParams(Context context) {
            try {
                return "endpoint=" + URLEncoder.encode(FeedSDK.getToken(context), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return "";
        }
    }
}
