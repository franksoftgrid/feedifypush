package com.feed.sdk.push.model;


import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representation of Notification received from push
 */
public class ModelNotification implements Parcelable {

    public int id;
    public String title;
    public String body;
    public String icon;
    public String image;
    public String url;
    public String type;
    public ArrayList<ActionButton> actionButtons = new ArrayList<>();
    public Map<String, String> customParams = new HashMap();
    public ArrayList<ActionButton> pushButtons = new ArrayList<>();

    private static final String NEW_LINE = "\n";

    protected ModelNotification(Parcel in) {
        id = in.readInt();
        title = in.readString();
        body = in.readString();
        icon = in.readString();
        image = in.readString();
        url = in.readString();
        type = in.readString();
        actionButtons = in.createTypedArrayList(ActionButton.CREATOR);
        pushButtons = in.createTypedArrayList(ActionButton.CREATOR);
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            String key = in.readString();
            String value = in.readString();
            customParams.put(key, value);
        }
    }

    public ModelNotification() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(body);
        dest.writeString(icon);
        dest.writeString(image);
        dest.writeString(url);
        dest.writeString(type);
        dest.writeTypedList(actionButtons);
        dest.writeTypedList(pushButtons);
        dest.writeInt(customParams.size());
        for (Map.Entry<String, String> entry : customParams.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ModelNotification> CREATOR = new Creator<ModelNotification>() {
        @Override
        public ModelNotification createFromParcel(Parcel in) {
            return new ModelNotification(in);
        }

        @Override
        public ModelNotification[] newArray(int size) {
            return new ModelNotification[size];
        }
    };

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("id :: " + id)
                .append(NEW_LINE)
                .append("title :: " + title).append(NEW_LINE)
                .append("icon :: " + icon).append(NEW_LINE)
                .append("image :: " + image).append(NEW_LINE)
                .append("url :: " + url).append(NEW_LINE)
                .append("type :: " + type).append(NEW_LINE);

        stringBuilder.append("-----------start actionButton logs------------").append(NEW_LINE);
        for (ActionButton actionBtn : actionButtons) {
            stringBuilder.append("action :: " + actionBtn.getAction()).append(NEW_LINE)
                    .append("title :: " + actionBtn.getTitle()).append(NEW_LINE)
                    .append("icon :: " + actionBtn.getIcon()).append(NEW_LINE);

        }
        stringBuilder.append("-----------end actionButton logs------------").append(NEW_LINE);

        stringBuilder.append("-----------start pushButton logs------------").append(NEW_LINE);
        for (ActionButton pushButton : pushButtons) {
            stringBuilder.append("action :: " + pushButton.getAction()).append(NEW_LINE)
                    .append("title :: " + pushButton.getTitle()).append(NEW_LINE)
                    .append("icon :: " + pushButton.getIcon()).append(NEW_LINE);

        }
        stringBuilder.append("-----------end pushButton logs------------").append(NEW_LINE);

        stringBuilder.append("-----------start customParams logs------------").append(NEW_LINE);

        stringBuilder.append(customParams.keySet().toString()).append(NEW_LINE);
        stringBuilder.append(customParams.values()).append(NEW_LINE);
        stringBuilder.append("-----------end customParams logs------------").append(NEW_LINE);

        return stringBuilder.toString();
    }

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
        notification.type = getDataFromJson(root, "type");
        notification.id = getIntDataFromJson(root, "id");
        notification.title = getDataFromJson(root, "title");
        notification.body = getDataFromJson(root, "body");

        notification.url = getDataFromJson(root, "url");
        if (notification.url != null && notification.url.trim().isEmpty())
            notification.url = null;
        notification.icon = getDataFromJson(root, "icon");
        if (notification.icon != null && notification.icon.trim().isEmpty())
            notification.icon = null;
        notification.image = getDataFromJson(root, "image");
        if (notification.image != null && notification.image.trim().isEmpty())
            notification.image = null;

        try {
            String s = getDataFromJson(root, "button");
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
            String s = getDataFromJson(root, "custom_params");
            JSONArray params = new JSONArray(s);
            notification.customParams = getCustomParams(params);
        } catch (Exception ex) {

        }

        try {
            String s = getDataFromJson(root, "custom_params_btn");
            JSONArray params = new JSONArray(s);
            notification.pushButtons = getPushButtons(params);

        } catch (Exception ex) {
        }

        return notification;
    }

    public static class ActionButton implements Parcelable {
        // final String BASE_BUTTON_URL="https://feedify.net/images/push/icons/";
        String title;
        String action;
        String icon = "";

        public ActionButton() {

        }

        protected ActionButton(Parcel in) {
            title = in.readString();
            action = in.readString();
            icon = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeString(action);
            dest.writeString(icon);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ActionButton> CREATOR = new Creator<ActionButton>() {
            @Override
            public ActionButton createFromParcel(Parcel in) {
                return new ActionButton(in);
            }

            @Override
            public ActionButton[] newArray(int size) {
                return new ActionButton[size];
            }
        };

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

    private static String getDataFromJson(JSONObject object, String key) {
        if (object.has(key)) {
            try {
                return object.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private static int getIntDataFromJson(JSONObject object, String key) {
        if (object.has(key)) {
            try {
                return object.getInt(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

}
