package com.feed.sdk.push;

import android.annotation.SuppressLint;
import android.content.Context;


import com.feed.sdk.push.common.Logs;
import com.feed.sdk.push.common.Pref;
import com.feed.sdk.push.net.FeedNet;
import com.feed.sdk.push.net.Request;
import com.feed.sdk.push.net.Response;
import com.feed.sdk.push.net.ResponseListener;

public class DataProvider {

    public static void loadData(final Context context) {
        String token = Pref.get(context).getString(FeedMessagingService.FCM_TOKEN, null);
        if (token != null) {
            String dataUrl = String.format("%s=%s", Const.DELIVER_ENDPOINT, token);
            final Request req = new Request(dataUrl, Request.REQUEST_GET, null, new ResponseListener() {
                /**
                 * This response is on Background thread , you need to take care if you want to access UI thred
                 * @param resp
                 */
                @Override
                public void onResponse(Response resp) {

                    if(!resp.isError()){

                        NotificationProvider.onMessageReceived(context, resp.getData());
                    }
                    Logs.d(" resposne is " + resp.getData());
                }
            });

            FeedNet.getInstance(context).executeRequest(req);
        }
    }


}
