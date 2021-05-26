package com.feed.sdk.push;

import android.content.Context;

public class DataProvider {

    public static void loadData(final Context context) {

      /*  String token = Pref.get(context).getString(FeedMessagingService.FCM_TOKEN, null);
        if (token != null) {
            String dataUrl = String.format("%s=%s", Const.DELIVER_ENDPOINT, token);
            final Request req = new Request(dataUrl, Request.REQUEST_GET, null, new ResponseListener() {
                @Override
                public void onResponse(Response resp) {
                    if(!resp.isError()){
                        Logs.d(" response is " + resp.getData());
                        NotificationProvider.onMessageReceived(context, resp.getData());
                    }else{
                        Logs.d(" Error while registering ");
                    }

                }
            });

            FeedNet.getInstance(context).executeRequest(req);
        }*/



    }


}
