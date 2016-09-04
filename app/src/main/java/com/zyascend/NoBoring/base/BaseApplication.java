package com.zyascend.NoBoring.base;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.zyascend.NoBoring.utils.SPUtils;

/**
 *
 * Created by Administrator on 2016/8/19.
 */
public class BaseApplication extends Application {

    private boolean isNight;
    public static Context context;
    private RefWatcher refWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        isNight = SPUtils.getBoolean(SPUtils.SP_KEY_NIGHT,false,this);

        refWatcher = LeakCanary.install(this);

    }

    public static RefWatcher getRefWatcher(Context context) {
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    public boolean isNight(){
        return isNight;
    }

    public void setIsNight(boolean b) {
        isNight = b;
    }
}
