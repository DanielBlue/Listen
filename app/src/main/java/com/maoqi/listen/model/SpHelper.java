package com.maoqi.listen.model;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Administrator on 2017/5/2.
 */

public class SpHelper {
    private final static String name = "config";

    private static SharedPreferences init(Context context) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp;
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = init(context).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences.Editor editor = init(context).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences.Editor editor = init(context).edit();
        editor.putString(key, value);
        editor.commit();
    }

    // 获取
    public static boolean getBoolean(Context context, String key,
                                     boolean defValue) {
        boolean value = init(context).getBoolean(key, defValue);
        return value;
    }

    public static int getInt(Context context, String key, int defValue) {
        int value = init(context).getInt(key, defValue);
        return value;
    }

    public static String getString(Context context, String key, String defValue) {
        String value = init(context).getString(key, defValue);
        return value;
    }
}
