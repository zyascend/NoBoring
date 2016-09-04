package com.zyascend.NoBoring.utils.map;

import android.util.Log;

import com.zyascend.NoBoring.model.DongTaiJokeResult;

import java.util.List;

import rx.functions.Func1;

/**
 * Created by Administrator on 2016/7/15.
 */
public class MapDongTaiJoke implements Func1<DongTaiJokeResult,List<DongTaiJokeResult.ShowapiResBodyBean.DongTaiJoke>> {
    private static final String TAG = "TAG_MapDonTaiJoke";
    private static MapDongTaiJoke INSTANCE = new MapDongTaiJoke();

    private MapDongTaiJoke(){}

    public static MapDongTaiJoke getInstance(){
        Log.d(TAG, "getInstance: ");
        return INSTANCE;
    }

    @Override
    public List<DongTaiJokeResult.ShowapiResBodyBean.DongTaiJoke> call(DongTaiJokeResult dongTaiJokeResult) {
        List<DongTaiJokeResult.ShowapiResBodyBean.DongTaiJoke> jokeList = dongTaiJokeResult.getShowapi_res_body().getContentlist();
        Log.d(TAG, "call: ");
        Log.d(TAG, "call: jokeListSize = "+jokeList.size());
        return jokeList;
    }
}
