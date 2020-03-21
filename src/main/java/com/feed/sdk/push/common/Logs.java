package com.feed.sdk.push.common;

import android.util.Log;

import java.util.List;


public class Logs {


    private static final String TAG = "Feedify-Push";
    public static boolean enabled;
    public static List<Class> debuggingClasses;

    public static void setEnabled(boolean enable) {
        enabled = enable;
    }

    public static void setDebuggingOn(List<Class> classes) {
        debuggingClasses = classes;
    }

    public static void debug(Class clazz, String string) {
        if (debuggingClasses != null && debuggingClasses.contains(clazz))
            Log.i(TAG, string);
    }

    public static void debug(Class clazz, String key, String string) {
        if (debuggingClasses != null && debuggingClasses.contains(clazz))
            Log.i(key, string);
    }

    @Deprecated
    public static void print(String string) {
        if (enabled)
            Log.d(TAG, string);
    }

    @Deprecated
    public static void print(String key, String string) {
        if (enabled)
            Log.d(key, string);
    }

    public static void d(String string) {
        if (enabled)
            Log.d(TAG, string);
    }

    public static void d(String key, String string) {
        if (enabled)
            Log.d(key, string);
    }

    //
    public static void e(String string) {
        if (enabled)
            Log.e(TAG, string);
    }

    public static void e(String key, String string) {
        if (enabled)
            Log.e(key, string);
    }


    //
    public static void w(String string) {
        if (enabled)
            Log.w(TAG, string);
    }

    public static void w(String key, String string) {
        if (enabled)
            Log.w(key, string);
    }

    //
    public static void i(String string) {
        if (enabled)
            Log.i(TAG, string);
    }

    public static void i(String string, boolean header) {
        if (enabled) {
            Log.i(TAG, string);
            Logs.i("---------------------------------------------------------");
        }
    }


    public static void i(String key, String string) {
        if (enabled)
            Log.i(key, string);
    }


    //
    public static void v(String string) {
        if (enabled)
            Log.v(TAG, string);
    }

    public static void v(String key, String string) {
        if (enabled)
            Log.v(key, string);
    }
}
