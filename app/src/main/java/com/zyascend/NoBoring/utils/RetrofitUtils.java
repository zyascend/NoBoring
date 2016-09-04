package com.zyascend.NoBoring.utils;

import android.support.v7.app.AppCompatActivity;

import com.zyascend.NoBoring.API;
import com.zyascend.NoBoring.Constants;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * Created by Administrator on 2016/7/14.
 */
public class RetrofitUtils {

    private static final String TAG = "TAG_RetrofitUtils";
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    private static API.DongTaiJokeApi dongTaiJokeApi;
    private static API.TextJokeApi textJokeApi;
    private static API.WeiXinNewsApi weixinApi;


    private static API.GirlApi girlApi;
    private static API.FreshApi freshApi;
    private static API.FreshContentApi contentApi;

    private static Retrofit getRetrofit(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();
        return retrofit;
    }

    public static API.GirlApi getGirlApi(){
        if (girlApi == null){
            girlApi = getRetrofit(Constants.BASE_GANK_URL).create(API.GirlApi.class);
        }
        return girlApi;
    }

    public static API.DongTaiJokeApi getDongTaiJokeApi(){
        if (dongTaiJokeApi == null){
            dongTaiJokeApi = getRetrofit(Constants.BASE_SHOW_URL).create(API.DongTaiJokeApi.class);
        }
        return dongTaiJokeApi;
    }

    public static API.WeiXinNewsApi getWeixinApi() {
        if (weixinApi == null){
            weixinApi = getRetrofit(Constants.TIAN_URL).create(API.WeiXinNewsApi.class);
        }
        return weixinApi;
    }

    public static API.TextJokeApi getTextJokeApi(){
        if (textJokeApi == null){
            textJokeApi = getRetrofit(Constants.BASE_SHOW_URL).create(API.TextJokeApi.class);
        }
        return textJokeApi;

    }


    public static API.FreshApi getFreshApi(){
        if (freshApi == null){
            freshApi = getRetrofit(Constants.BASE_FRESH_URL).create(API.FreshApi.class);
        }
        return freshApi;
    }

    public static API.FreshContentApi getContentApi(){
        if (contentApi == null){
            contentApi = getRetrofit(Constants.BASE_FRESH_CONTENT_URL).create(API.FreshContentApi.class);
        }
        return contentApi;
    }


}
