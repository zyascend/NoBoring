package com.zyascend.NoBoring.utils.view;

import com.zyascend.NoBoring.base.BaseApplication;

public class ViewUtils {

    public static int dip2px(float dpValue) {
        final float scale = BaseApplication.context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(float pxValue) {
        final float scale = BaseApplication.context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getScreenWidth() {
        return BaseApplication.context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return BaseApplication.context.getResources().getDisplayMetrics().heightPixels;
    }
}
