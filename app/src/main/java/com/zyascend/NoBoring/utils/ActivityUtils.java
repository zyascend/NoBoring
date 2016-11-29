package com.zyascend.NoBoring.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import com.zyascend.NoBoring.base.BaseApplication;
import com.zyascend.NoBoring.dao.Fresh;

import java.util.List;

/**
 * Created by Administrator on 2016/7/14.
 */
public class ActivityUtils {

    private static final String TAG = "TAG_ActivityUtils";

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
}
