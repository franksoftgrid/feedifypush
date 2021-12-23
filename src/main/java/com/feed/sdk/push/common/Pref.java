package com.feed.sdk.push.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

/**
 * manages shared preferences
 */
@SuppressLint("NewApi")
public class Pref {
    private SharedPreferences privatePref;
    private SharedPreferences editablePref;
    private static final String KEY_PREFERENCE_ID = "astro_lab";


    public String default_secure_key = "ad8e75cf";

    /**
     * default constructor disabled
     */
    private Pref() {
    }

    /**
     * initialises the class
     *
     * @param context - context used to access preferences
     */
    public static Pref get(Context context) {
        return new Pref(context);
    }

    /**
     * initialises the class
     *
     * @param context - context used to access preferences
     * @param prefs   -  Desired preferences file. If a preferences file by this name does
     *                not exist, it will be created when you retrieve an editor (SharedPreferences.edit())
     *                and then commit changes (Editor.commit()).
     */
    public static Pref get(Context context, String prefs) {
        return new Pref(context, prefs);
    }


    private Pref(Context context) {
        privatePref = context.getSharedPreferences(KEY_PREFERENCE_ID, Context.MODE_PRIVATE);
        editablePref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private Pref(Context context, String prefKey) {
        privatePref = context.getSharedPreferences(prefKey, Context.MODE_PRIVATE);
        editablePref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Set a String value in the preferences editor, to be written back once commit() or apply() are called.
     *
     * @param name - The name of the preference to modify.
     * @param data - The new value for the preference. Passing null for this argument is equivalent to calling remove(String) with this key.
     */
    public void put(String name, String data) {
        privatePref.edit().putString(name, data).commit();
    }

    /**
     * Retrieve a String value from the preferences.
     *
     * @param name - The name of the preference to retrieve.
     * @param data -  Value to return if this preference does not exist.
     */
    public String getString(String name, String data) {
        return privatePref.getString(name, data);
    }

    /**
     * Set a boolean value in the preferences editor, to be written back once commit() or apply() are called.
     *
     * @param name - The name of the preference to modify.
     * @param data - The new value for the preference.
     */
    public void put(String name, boolean data) {
        privatePref.edit().putBoolean(name, data).commit();
    }

    /**
     * Set an int value in the preferences editor, to be written back once commit() or apply() are called.
     *
     * @param name - The name of the preference to modify.
     * @param data - The new value for the preference.
     */

    public void put(String name, int data) {
        privatePref.edit().putInt(name, data).commit();
    }

    /**
     * Set a float value in the preferences editor, to be written back once commit() or apply() are called.
     *
     * @param name - The name of the preference to modify.
     * @param data - The new value for the preference.
     */
    public void put(String name, float data) {
        privatePref.edit().putFloat(name, data).commit();

    }

    /**
     * Set a long value in the preferences editor, to be written back once commit() or apply() are called.
     *
     * @param name -  The name of the preference to modify.
     * @param data - The new value for the preference.
     */
    public void put(String name, long data) {
        privatePref.edit().putLong(name, data).commit();
    }

    /**
     * Retrieve a boolean value from the preferences.
     *
     * @param name -  The name of the preference to retrieve.
     * @param data - Value to return if this preference does not exist.
     */
    public boolean getBoolean(String name, boolean data) {
        return privatePref.getBoolean(name, data);
    }

    /**
     * Retrieve an int value from the preferences.
     *
     * @param name - The name of the preference to retrieve.
     * @param data - Value to return if this preference does not exist.
     */
    public int getInt(String name, int data) {
        return privatePref.getInt(name, data);
    }

    /**
     * Retrieve a float value from the preferences.
     *
     * @param name - The name of the preference to retrieve.
     * @param data - Value to return if this preference does not exist.
     */
    public float getFloat(String name, float data) {
        return privatePref.getFloat(name, data);
    }

    /**
     * Retrieve a long value from the preferences.
     *
     * @param name - The name of the preference to retrieve.
     * @param data - Value to return if this preference does not exist.
     */
    public long getLong(String name, long data) {
        return privatePref.getLong(name, data);
    }

    /**
     * Retrieve a String value from the preferences.
     *
     * @param name - The name of the preference to retrieve.
     * @param data - Value to return if this preference does not exist.
     */
    public String getEditableString(String name, String data) {
        return editablePref.getString(name, data);
    }

    /**
     * Retrieve a boolean value from the preferences.
     *
     * @param name - The name of the preference to retrieve.
     * @param data - Value to return if this preference does not exist.
     */
    public boolean getEditableBoolean(String name, boolean data) {
        return editablePref.getBoolean(name, data);
    }

    /**
     * Retrieve a string value from the preferences.
     *
     * @param name - The name of the preference to retrieve.
     * @param data - Value to return if this preference does not exist.
     */

    public void putEditable(String name, String data) {
        editablePref.edit().putString(name, data).commit();
    }

    /**
     * Retrieve a boolean value from the preferences.
     *
     * @param name - The name of the preference to retrieve.
     * @param data - Value to return if this preference does not exist.
     */
    public void putEditable(String name, boolean data) {
        editablePref.edit().putBoolean(name, data).commit();
    }

    /**
     * Mark in the editor to remove all values from the preferences. Once commit is called,
     * the only remaining preferences will be any that you have defined in this editor.
     * Note that when committing back to the preferences, the clear is done first, regardless
     * of whether you called clear before or after put methods on this editor.
     */
    public void clearAll() {
        privatePref.edit().clear().commit();
    }

    /**
     * Mark in the editor that a preference value should be removed, which will be done in the actual
     * preferences once commit() is called.
     * Note that when committing back to the preferences, all removals are done first, regardless
     * of whether you called remove before or after put methods on this editor.
     *
     * @param key - The name of the preference to remove.
     */
    public void clear(String key) {
        privatePref.edit().remove(key).commit();
    }
}
