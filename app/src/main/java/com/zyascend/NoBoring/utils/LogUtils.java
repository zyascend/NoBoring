package com.zyascend.NoBoring.utils;

import android.util.Log;

/**
 * 功能：
 * 作者：zyascend on 2017/7/30 12:30
 * 邮箱：zyascend@qq.com
 */

public class LogUtils {

    private static final String TAG = "[----LogUtils---]";
    public static void d(String msg) {
        Log.d(TAG,msg);
    }

    public static void e(String message) {
        Log.e(TAG, message);
    }

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }
}
