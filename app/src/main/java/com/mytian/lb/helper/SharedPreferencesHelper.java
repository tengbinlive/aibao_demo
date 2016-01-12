package com.mytian.lb.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.mytian.lb.Constant;


public final class SharedPreferencesHelper {

    private SharedPreferencesHelper() {
    }

    public static boolean getBoolean(Context c, String key, boolean defValue) {
        SharedPreferences sp = c.getSharedPreferences(Constant.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static void setBoolean(Context c, String key, boolean content) {
        SharedPreferences sp = c.getSharedPreferences(Constant.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, content);
        editor.apply();
    }

    public static int getPreferInt(Context c, String key, int defValue) {
        SharedPreferences sp = c.getSharedPreferences(Constant.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    public static void setPreferInt(Context c, String key, int content) {
        SharedPreferences sp = c.getSharedPreferences(Constant.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, content);
        editor.apply();
    }

    public static float getFloat(Context c, String key, float defValue) {
        SharedPreferences sp = c.getSharedPreferences(Constant.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        return sp.getFloat(key, defValue);
    }

    public static void setFloat(Context c, String key, float content) {
        SharedPreferences sp = c.getSharedPreferences(Constant.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, content);
        editor.apply();
    }

    public static long getLong(Context c, String key, long defValue) {
        SharedPreferences sp = c.getSharedPreferences(Constant.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        return sp.getLong(key, defValue);
    }

    public static void setLong(Context c, String key, long content) {
        SharedPreferences sp = c.getSharedPreferences(Constant.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, content);
        editor.apply();
    }

    public static String getString(Context c, String key, String defValue) {
        SharedPreferences sp = c.getSharedPreferences(Constant.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static void setString(Context c, String key, String content) {
        SharedPreferences sp = c.getSharedPreferences(Constant.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, content);
        editor.apply();
    }

    public static void remove(Context c, String key) {
        SharedPreferences sp = c.getSharedPreferences(Constant.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }
}
