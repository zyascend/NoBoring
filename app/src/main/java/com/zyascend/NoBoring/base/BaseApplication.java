package com.zyascend.NoBoring.base;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.lzy.okgo.OkGo;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.zyascend.NoBoring.utils.SPUtils;

/**
 *
 * Created by Administrator on 2016/8/19.
 */
public class BaseApplication extends Application {


    private static final String APP_KEY = "VVIjOnCkOuS4s2ETvmCavUus";
    private static final String APP_ID = "ioJGwMTChBTt0qRdhWFhhEye-gzGzoHsz";


    private boolean isNight;
    public static Context context;
    private RefWatcher refWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        isNight = SPUtils.getBoolean(SPUtils.SP_KEY_NIGHT,false,this);
        refWatcher = LeakCanary.install(this);
        // 初始化参数依次为 this, AppId, AppKey

        AVOSCloud.initialize(this,APP_ID,APP_KEY);
        // 放在 SDK 初始化语句 AVOSCloud.initialize() 后面，只需要调用一次即可
        //在应用发布之前，请关闭调试日志，以免暴露敏感数据。
        AVOSCloud.setDebugLogEnabled(true);

        OkGo.getInstance().init(this);


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
