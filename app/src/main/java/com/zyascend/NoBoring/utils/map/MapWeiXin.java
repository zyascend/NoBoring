package com.zyascend.NoBoring.utils.map;

import android.util.Log;

import com.zyascend.NoBoring.model.WeiXinResult;

import java.util.List;

import rx.functions.Func1;

/**
 * Created by Administrator on 2016/7/17.
 */
public class MapWeiXin implements Func1<WeiXinResult ,List<WeiXinResult.WeiXin>> {

    private static final String TAG = "TAG_MapWeiXin";
    private static MapWeiXin INSTANCE = new MapWeiXin();

    private MapWeiXin(){}

    public static MapWeiXin getInstance(){
        Log.d(TAG, "getInstance: ");
        return INSTANCE;
    }

    @Override
    public List<WeiXinResult.WeiXin> call(WeiXinResult weiXinResult) {
        Log.d(TAG, "call: ");
        List<WeiXinResult.WeiXin> weiXinList = weiXinResult.getNewslist();
        Log.d(TAG, "call: size = " + weiXinList.size());
        return weiXinList;
    }
}
