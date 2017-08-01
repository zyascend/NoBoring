package com.zyascend.NoBoring.utils;

import android.content.Context;
import android.util.Log;
import android.widget.CompoundButton;

import com.zyascend.NoBoring.activity.MainActivity;
import com.zyascend.NoBoring.base.BaseApplication;
import com.zyascend.NoBoring.bean.LoginResponse;
import com.zyascend.NoBoring.bean.RegisterResponse;

/**
 *
 * Created by Administrator on 2016/8/9.
 */
public class SPUtils {
    public  static final String SP_KEY_NOPIC = "nopic";
    public  static final String SP_KEY_NIGHT = "night";
    private static final String FILE_NAME = "file_name";
    private static final String TAG = "TAG_SPUtils";
    public static final String ENTRANCE_PIC = "pic";
    public static final String SESSION_TOKEN = "session_token";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String AVATAR_URL = "avatar";

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

    public static void putString(String content,String key){
        Context context = BaseApplication.context;
        if (context == null)return;
        context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE)
                .edit().putString(key,content).apply();
        Log.d(TAG, "putString: "+content);

    }

    public static String getString(String key,String def){
        Context context = BaseApplication.context;
        if (context == null)return null;
        String content = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE)
                .getString(key,def);
        Log.d(TAG, "getString: "+content);
        return content;
    }


    public static void putUserInfo(LoginResponse response) {
        SPUtils.putString(response.getSessionToken(),SESSION_TOKEN);
        SPUtils.putString(response.getObjectId(),USER_ID);
        SPUtils.putString(response.getUsername(),USER_NAME);
        SPUtils.putString(response.getAvatarUrl(),AVATAR_URL);
    }

    public static void putUserInfo(RegisterResponse response,String name) {
        SPUtils.putString(response.getSessionToken(),SESSION_TOKEN);
        SPUtils.putString(response.getObjectId(),USER_ID);
        SPUtils.putString(name,USER_NAME);

    }
}
