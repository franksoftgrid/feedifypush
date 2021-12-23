package com.feed.sdk.push.common;

import android.util.Log;

/**
 * Prints logs
 */
public class Logs {


    private static final String TAG = "Feedify-Push";
    private static boolean enabled;

    /**
     * enables/ disables logs printing on logcat console
     */
    public static void setEnabled(boolean enable) {
        enabled = enable;
    }

    /**
     * prints logs in debug mode with default tag - "Feedify-Push"
     *
     * @param string - string to be printed
     */
    public static void d(String string) {
        if (enabled)
            Log.d(TAG, string);
    }

    /**
     * prints logs in debug mode with tag
     *
     * @param key    - the tag to be used for printing
     * @param string - string to be printed
     */
    public static void d(String key, String string) {
        if (enabled)
            Log.d(key, string);
    }

    /**
     * prints logs in error mode with default tag - "Feedify-Push"
     *
     * @param string - string to be printed
     */
    public static void e(String string) {
        if (enabled)
            Log.e(TAG, string);
    }

    /**
     * prints logs in error mode with tag
     *
     * @param key    - the tag to be used for printing
     * @param string - string to be printed
     */
    public static void e(String key, String string) {
        if (enabled)
            Log.e(key, string);
    }


    /**
     * prints logs in warning mode with tag - "Feedify-Push"
     *
     * @param string - string to be printed
     */
    public static void w(String string) {
        if (enabled)
            Log.w(TAG, string);
    }

    /**
     * prints logs in warning mode with tag
     *
     * @param key    - the tag to be used for printing
     * @param string - string to be printed
     */
    public static void w(String key, String string) {
        if (enabled)
            Log.w(key, string);
    }

    /**
     * prints logs in info mode with tag - "Feedify-Push"
     *
     * @param string - string to be printed
     */
    public static void i(String string) {
        if (enabled)
            Log.i(TAG, string);
    }

    /**
     * prints logs in info mode with tag
     *
     * @param key    - the tag to be used for printing
     * @param string - string to be printed
     */
    public static void i(String key, String string) {
        if (enabled)
            Log.i(key, string);
    }


    /**
     * prints logs in verbose mode with tag - "Feedify-Push"
     *
     * @param string - string to be printed
     */
    public static void v(String string) {
        if (enabled)
            Log.v(TAG, string);
    }

    /**
     * prints logs in debug mode with tag
     *
     * @param key    - the tag to be used for printing
     * @param string - string to be printed
     */
    public static void v(String key, String string) {
        if (enabled)
            Log.v(key, string);
    }
}
