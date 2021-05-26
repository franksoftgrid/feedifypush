package com.feed.sdk.push.model;


import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    public Map<String, String> customParams = new HashMap();
    public ArrayList<ActionButton> pushButtons = new ArrayList<ActionButton>();

//    public static ModelNotification getInstance(String jsonString) throws JSONException {
//        JSONObject root = new JSONObject(jsonString);
//        ModelNotification notification = new ModelNotification();
//        notification.type = root.getString("type");
//        notification.id = Integer.parseInt(root.getString("id"));
//        notification.title = root.getString("title");
//        notification.body = root.getString("body");
//
//        notification.url = root.getString("url");
//        if (notification.url != null && notification.url.trim().isEmpty())
//            notification.url = null;
//        notification.icon = root.getString("icon");
//        if (notification.icon != null && notification.icon.trim().isEmpty())
//            notification.icon = null;
//        notification.image = root.getString("image");
//        if (notification.image != null && notification.image.trim().isEmpty())
//            notification.image = null;
//        try {
//            JSONObject buttons = root.getJSONObject("button");
//            try {
//                JSONObject button1 = buttons.getJSONObject("button1");
//                ActionButton button = new ActionButton();
//                button.setAction(button1.getString("action"));
//                button.setIcon(button1.getString("icon"));
//                button.setTitle(button1.getString("title"));
//                notification.actionButtons.add(button);
//            } catch (Exception ex) {
//
//            }
//            try {
//                JSONObject button1 = buttons.getJSONObject("button2");
//                ActionButton button = new ActionButton();
//                button = new ActionButton();
//                button.setAction(button1.getString("action"));
//                button.setIcon(button1.getString("icon"));
//                button.setTitle(button1.getString("title"));
//                notification.actionButtons.add(button);
//            } catch (Exception ex) {
//
//            }
//
//        } catch (Exception ex) {
//
//        }
//
//        try {
//
//            JSONArray params = root.getJSONArray("custom_params");
//            notification.customParams = getCustomParams(params);
//
//        } catch (Exception ex) {
//
//        }
//
//        try {
//            JSONArray params = root.getJSONArray("custom_params_btn");
//            notification.pushButtons = getPushButtons(params);
//
//        } catch (Exception ex) {
//        }
//
//        return notification;
//    }

    private static ArrayList<ActionButton> getPushButtons(JSONArray params) throws JSONException {
        ArrayList<ActionButton> buttons = new ArrayList<>();
        for (int i = 0; i < params.length(); i++) {
            JSONObject param = params.getJSONObject(i);
            String key = param.getString("key");
            if (key != null && !key.equals("")) {
                String val = param.getString("value");
                String icon = param.getString("icon") == null ? "" : param.getString("icon");
                ActionButton btn = new ActionButton();
                btn.title = key;
                btn.action = val;
                btn.icon = icon.trim();
                buttons.add(btn);
            }
        }

        return buttons;
    }

    private static Map<String, String> getCustomParams(JSONArray params) throws JSONException {
        Map<String, String> customParams = new HashMap();
        for (int i = 0; i < params.length(); i++) {
            JSONObject param = params.getJSONObject(i);
            String key = param.getString("key");
            if (key != null && !key.equals("")) {
                String val = param.getString("value");
                customParams.put(key, val);
            }
        }

        return customParams;
    }


    public static ModelNotification getInstance(JSONObject root) throws JSONException {
//        Object obj = remoteMessage.getData();
//        JSONObject root = new JSONObject(remoteMessage);
        ModelNotification notification = new ModelNotification();
        String type = null;
//        if (root.has("type")){
//            type =  root.getString("type");
//        }
//        if (type == null) return null;
//        if (type.equalsIgnoreCase("ad")){
//            notification.type = root.getString("type");
//            return notification;
//        }else{
//        }
        notification.type = getDataFromJson(root,"type");
        notification.id = getIntDataFromJson(root, "id");
        notification.title = getDataFromJson(root,"title");
        notification.body = getDataFromJson(root,"body");

        notification.url = getDataFromJson(root,"url");
        if (notification.url != null && notification.url.trim().isEmpty())
            notification.url = null;
        notification.icon = getDataFromJson(root,"icon");
        if (notification.icon != null && notification.icon.trim().isEmpty())
            notification.icon = null;
        notification.image = getDataFromJson(root,"image");
        if (notification.image != null && notification.image.trim().isEmpty())
            notification.image = null;

        try {
            String s =  getDataFromJson(root,"button");
            JSONObject buttons = new JSONObject(s);
            try {
                JSONObject button1 = buttons.getJSONObject("button1");
                ActionButton button = new ActionButton();
                button.setAction(getDataFromJson(button1, "action"));
                button.setIcon(getDataFromJson(button1, "icon"));
                button.setTitle(getDataFromJson(button1, "title"));
                notification.actionButtons.add(button);
            } catch (Exception ex) {
                System.out.println("ex ");
            }
            try {
                JSONObject button1 = buttons.getJSONObject("button2");
                ActionButton button = new ActionButton();
                button = new ActionButton();
                button.setAction(getDataFromJson(button1, "action"));
                button.setIcon(getDataFromJson(button1, "icon"));
                button.setTitle(getDataFromJson(button1, "title"));
                notification.actionButtons.add(button);
            } catch (Exception ex) {
                System.out.println("ex ");
            }
        } catch (Exception ex) {
            System.out.println("ex ");
        }

        try {
            String s = getDataFromJson(root,"custom_params");
            JSONArray params = new JSONArray(s);
            notification.customParams = getCustomParams(params);
        } catch (Exception ex) {

        }

        try {
            String s = getDataFromJson(root,"custom_params_btn");
            JSONArray params = new JSONArray(s);
            notification.pushButtons = getPushButtons(params);

        } catch (Exception ex) {
        }

        return notification;
    }

    public static class ActionButton {
        // final String BASE_BUTTON_URL="https://feedify.net/images/push/icons/";
        String title;
        String action;
        String icon = "";

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

    private static String getDataFromJson(JSONObject object, String key){
        if (object.has(key)){
            try {
                return object.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private static int getIntDataFromJson(JSONObject object, String key){
        if (object.has(key)){
            try {
                return object.getInt(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

}
