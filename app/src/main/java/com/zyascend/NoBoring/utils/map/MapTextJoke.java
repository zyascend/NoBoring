package com.zyascend.NoBoring.utils.map;

import android.util.Log;

import com.zyascend.NoBoring.model.TextJokeResult;

import java.util.List;

import rx.functions.Func1;

/**
 * Created by Administrator on 2016/7/16.
 */
public class MapTextJoke implements Func1<TextJokeResult,List<TextJokeResult.ShowapiResBodyBean.TextJoke>> {

    private static final String TAG = "TAG_MapTextJoke";
    private static MapTextJoke INSTANCE = new MapTextJoke();

    private MapTextJoke(){}

    public static MapTextJoke getInstance(){
        Log.d(TAG, "getInstance: ");
        return INSTANCE;
    }

    @Override
    public List<TextJokeResult.ShowapiResBodyBean.TextJoke> call(TextJokeResult textJokeResult) {
        List<TextJokeResult.ShowapiResBodyBean.TextJoke> textJokeList
                = textJokeResult.getShowapi_res_body().getContentlist();
        Log.d(TAG, "call: textJokeSize = "+ textJokeList.size());
        return textJokeList;
    }
}
