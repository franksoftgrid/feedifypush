package com.feed.sdk.push.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

@SuppressLint("NewApi")
public class Pref {
    private SharedPreferences privatePref;
    private SharedPreferences editablePref;
    private String KEY_PREFERENCE_ID = "astro_lab";


    public String default_secure_key = "ad8e75cf";


    public static Pref get(Context context) {
        return new Pref(context);
    }

    public static Pref get(Context context, String prefs) {
        return new Pref(context, prefs);
    }


    public Pref(Context context) {
        privatePref = context.getSharedPreferences(KEY_PREFERENCE_ID, Context.MODE_PRIVATE);
        editablePref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Pref(Context context, String prefKey) {
        privatePref = context.getSharedPreferences(prefKey, Context.MODE_PRIVATE);
        editablePref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void put(String name, String data) {
        privatePref.edit().putString(name, data).commit();
    }

    public String getString(String name, String data) {
        return privatePref.getString(name, data);
    }


    public void put(String name, boolean data) {

        privatePref.edit().putBoolean(name, data).commit();
    }

    public void put(String name, int data) {

        privatePref.edit().putInt(name, data).commit();
    }

    public void put(String name, float data) {

        privatePref.edit().putFloat(name, data).commit();

    }

    public void put(String name, long data) {

        privatePref.edit().putLong(name, data).commit();
    }


    public boolean getBoolean(String name, boolean data) {

        return privatePref.getBoolean(name, data);
    }

    public int getInt(String name, int data) {

        return privatePref.getInt(name, data);
    }

    public float getFloat(String name, float data) {

        return privatePref.getFloat(name, data);

    }

    public long getLong(String name, long data) {
        return privatePref.getLong(name, data);
    }

    public String getEditableString(String name, String data) {

        return editablePref.getString(name, data);
    }

    public boolean getEditableBoolean(String name, boolean data) {

        return editablePref.getBoolean(name, data);
    }

    public void putEditable(String name, String data) {
        editablePref.edit().putString(name, data).commit();
    }

    public void putEditable(String name, boolean data) {
        editablePref.edit().putBoolean(name, data).commit();
    }

    public void clearAll() {
        privatePref.edit().clear().commit();
    }

    public void clear(String key) {
        privatePref.edit().remove(key).commit();
    }

    public static void showToast(final Context ctx, final String msg) {
        /*
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();
            }
        });*/
    }
}
