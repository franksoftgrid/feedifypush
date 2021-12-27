package com.feed.sdk.push.utils;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.feed.sdk.push.Const;
import com.feed.sdk.push.NotificationProvider;
import com.feed.sdk.push.common.Logs;
import com.feed.sdk.push.model.ModelNotification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * reads notification data from bundle
 */
public class FeedNotificationDataManager {
    private static final String TAG = "FeedNotificationDataManager";

    /**
     * reads notification data passed inside Bundle and also captures click count
     *
     * @param extras - bundle object received in the activity
     * @return ModelNotification
     */
    public static ModelNotification getNotificationData(@Nullable Bundle extras) {
        ModelNotification modelNotification = new ModelNotification();
        if (extras != null) {
            modelNotification = extras.getParcelable(NotificationProvider.NOTIFICATION_DATA);
        }
        captureClick(modelNotification.id, modelNotification.type);
        return modelNotification;
    }

    /**
     * records click of user on notification, no need to call it explicitly,
     * already called by SDK itself, if counts failed to call, then can be called from outside the SDK too
     *
     * @param id   - id of the notification received
     * @param type - notification type - "ad" or "push"
     */
    public static void captureClick(int id, String type) {
        if (id != 0) {
            new CaptureClick(id, type).execute();
        }
    }

    private static class CaptureClick extends AsyncTask<Void, Void, Void> {
        private final int id;
        private final String type;

        public CaptureClick(final int id, final String type) {
            this.id = id;
            this.type = type;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String query = getClickQueryParameters(id, type);
                String url = Const.CLICK_API;
                URLConnection connection = new URL(url + "?" + query).openConnection();
                connection.setRequestProperty("Accept-Charset", "UTF-8");

                InputStream response = connection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(response));

                String line = "";
                String message = "";

                while ((line = reader.readLine()) != null) {
                    message += line;
                }
                response.close();
                reader.close();
                Logs.d(TAG, message);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private String getClickQueryParameters(int id, String type) {
            try {
                return "type=" + URLEncoder.encode(type, "UTF-8") + "&id=" + URLEncoder.encode(String.valueOf(id), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return "";
        }
    }
}
