package com.zyascend.NoBoring.utils;

import android.content.Context;
import android.util.Log;
import android.widget.CompoundButton;

import com.zyascend.NoBoring.activity.MainActivity;

/**
 *
 * Created by Administrator on 2016/8/9.
 */
public class SPUtils {
    public  static final String SP_KEY_NOPIC = "nopic";
    public  static final String SP_KEY_NIGHT = "night";
    private static final String FILE_NAME = "file_name";
    private static final String TAG = "TAG_SPUtils";

    public static boolean getBoolean(String key, boolean defaultValue, Context context) {
        boolean b = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE)
                .getBoolean(key,defaultValue);
        Log.d(TAG, "getBoolean: "+ b );
        return b;
    }

    public static void putBoolean(Context context, String keyNopic, boolean value) {
        context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
                .edit().putBoolean(keyNopic, value).apply();
        Log.d(TAG, "putBoolean: key = "+keyNopic+"value = "+value);
    }
}
