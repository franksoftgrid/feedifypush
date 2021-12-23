package com.feed.sdk.push.model;

import android.content.Context;

import com.feed.sdk.push.R;
import com.feed.sdk.push.common.Assets;
import com.feed.sdk.push.exception.GoogleServiceJsonException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * reads google-services.json file from app's root directory
 */
public class ModelFirebaseApp {

    private static final String TAG = "ModelFirebaseApp";

    public String api_key;
    public String mobilesdk_app_id;
    public String project_number;
    public String firebase_url;
    public String storage_bucket;

    private ModelFirebaseApp() {
    }

    public static ModelFirebaseApp getInstance(Context context) throws GoogleServiceJsonException {
        try {
            String googleServicesJson = Assets.textFileToString(context, "google-services.json");
            JSONObject root = new JSONObject(googleServicesJson);
            JSONObject project_info = root.getJSONObject("project_info");
            JSONArray client = root.getJSONArray("client");

            for (int i = 0; i < client.length(); i++) {
                JSONObject client_info = client.getJSONObject(i).getJSONObject("client_info");
                JSONArray api_key = client.getJSONObject(i).getJSONArray("api_key");
                String package_name = client_info.getJSONObject("android_client_info").getString("package_name");
                if (package_name.equals(context.getPackageName())) {
                    ModelFirebaseApp model = new ModelFirebaseApp();
                    model.project_number = project_info.getString("project_number");
                    model.firebase_url = project_info.getString("firebase_url");
                    model.storage_bucket = project_info.getString("storage_bucket");
                    model.mobilesdk_app_id = client_info.getString("mobilesdk_app_id");
                    model.api_key = api_key.getJSONObject(0).getString("current_key");
                    return model;
                }
            }

            throw new GoogleServiceJsonException(context.getString(R.string._google_services_not_register));

        } catch (IOException e) {
            e.printStackTrace();
            throw new GoogleServiceJsonException(context.getString(R.string._google_services_not_found));
        } catch (JSONException e) {
            e.printStackTrace();
            throw new GoogleServiceJsonException(context.getString(R.string._google_services_invalid));
        }
    }


}
