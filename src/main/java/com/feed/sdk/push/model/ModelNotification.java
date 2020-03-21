package com.feed.sdk.push.model;


import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Class representation of Notification received from push
 */
public class ModelNotification {

    public int id;
    public String title;
    public String body;
    public String icon;
    public String image;
    public String url;
    public String type;
    public ArrayList<ActionButton> actionButtons = new ArrayList<ActionButton>();

    public static ModelNotification getInstance(String jsonString) throws JSONException {
        JSONObject root = new JSONObject(jsonString);
        ModelNotification notification = new ModelNotification();
        notification.type = root.getString("type");
        notification.id = Integer.parseInt(root.getString("id"));
        notification.title = root.getString("title");
        notification.body = root.getString("body");

        notification.url = root.getString("url");
        if (notification.url != null && notification.url.trim().isEmpty())
            notification.url = null;
        notification.icon = root.getString("icon");
        if (notification.icon != null && notification.icon.trim().isEmpty())
            notification.icon = null;
        notification.image = root.getString("image");
        if (notification.image != null && notification.image.trim().isEmpty())
            notification.image = null;
        try {
            JSONObject buttons = root.getJSONObject("button");
            JSONObject button1 = buttons.getJSONObject("button1");
            ActionButton button = new ActionButton();
            button.setAction(button1.getString("action"));
            button.setIcon(button1.getString("icon"));
            button.setTitle(button1.getString("title"));
            notification.actionButtons.add(button);
            button1 = buttons.getJSONObject("button2");
            button = new ActionButton();
            button.setAction(button1.getString("action"));
            button.setIcon(button1.getString("icon"));
            button.setTitle(button1.getString("title"));
            notification.actionButtons.add(button);

        }catch(Exception ex){

        }



        return notification;
    }


    public static ModelNotification getInstance(RemoteMessage remoteMessage) throws JSONException {
        JSONObject root = new JSONObject(remoteMessage.getData().get("promotional"));
        ModelNotification notification = new ModelNotification();
        notification.type = root.getString("type");
        notification.id = root.getInt("id");
        notification.title = root.getString("title");
        notification.body = root.getString("body");

        notification.url = root.getString("url");
        if (notification.url != null && notification.url.trim().isEmpty())
            notification.url = null;
        notification.icon = root.getString("icon");
        if (notification.icon != null && notification.icon.trim().isEmpty())
            notification.icon = null;
        notification.image = root.getString("image");
        if (notification.image != null && notification.image.trim().isEmpty())
            notification.image = null;
        try {
            JSONObject buttons = root.getJSONObject("button");
            JSONObject button1 = buttons.getJSONObject("button1");
            ActionButton button = new ActionButton();
            button.setAction(button1.getString("action"));
            button.setIcon(button1.getString("icon"));
            button.setTitle(button1.getString("title"));
            notification.actionButtons.add(button);
            button1 = buttons.getJSONObject("button2");
            button = new ActionButton();
            button.setAction(button1.getString("action"));
            button.setIcon(button1.getString("icon"));
            button.setTitle(button1.getString("title"));
            notification.actionButtons.add(button);


        }catch(Exception ex){

        }
        return notification;
    }

  public static class ActionButton {
       // final String BASE_BUTTON_URL="https://feedify.net/images/push/icons/";
        String title;
        String action;
        String icon;
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }


    }


}
