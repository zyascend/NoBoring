package com.zyascend.NoBoring.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.zyascend.NoBoring.base.BaseApplication;
import com.zyascend.NoBoring.dao.Fresh;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/14.
 */
public class ActivityUtils {

    private static final String TAG = "TAG_ActivityUtils";
    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";


    public static void addFragmentToActivity(android.support.v4.app.FragmentManager manager, Fragment fragment, int frameId){

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(frameId,fragment);
        transaction.commit();
    }


    public static void jumpTo(Context context, Class<?> activity){
        Intent intent  = new Intent(context, activity);
        context.startActivity(intent);
    }

    public static void showSnackBar(View view,String msg){
        Snackbar.make(view,msg,Snackbar.LENGTH_SHORT).show();
    }

    public static void openUrl(Activity activity,String url){

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        activity.startActivity(intent);

    }

    /**
     * 检查是否有APP可以接受这个Intent
     * @param intent
     * @return
     */
    public static boolean isIntentSafe(Intent intent) {
        PackageManager packageManager = BaseApplication.context.getPackageManager();
        List activities = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return activities.size() > 0;
    }

    public static void share(Context context,String title,String url){
        String sb = "『 " +
                title +
                " 』 点击查看: " +
                url +
                "   ――来自【不许无聊】APP的分享――   ";

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, sb);
        shareIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(shareIntent,"分享到："));

    }

    public static void shareJokes(Context context, String text){

        StringBuilder sb = new StringBuilder();
        sb.append("『 ");
        sb.append(text);
        sb.append(" 』");
        sb.append("   ――来自【不许无聊】APP的分享――  ");

    }


    public static boolean isGif(String picUrl) {
        if (!TextUtils.isEmpty(picUrl) && picUrl.contains("gif")){
            return true;
        }
        return false;
    }

    public static byte[] getBytes(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            Log.d(TAG, "getBytes: error---------->"+e.toString());
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }



    public static String formatDate(Date date) {

        long delta = new Date().getTime() - date.getTime();
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 48L * ONE_HOUR) {
            return "昨天";
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
        }
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }

    public static boolean isExternalStorageAvailable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }
}
