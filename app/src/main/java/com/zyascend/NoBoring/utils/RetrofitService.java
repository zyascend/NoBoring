package com.zyascend.NoBoring.utils;

import com.zyascend.NoBoring.API;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/11/24.
 */

public class RetrofitService {


    private static RetrofitService SERVICE;
    private static OkHttpClient CLIENT;
    private static final int DEFAULT_TIMEOUT = 20000;

    private RetrofitService(){
        CLIENT = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    public static RetrofitService init(){
        if (SERVICE == null){
            synchronized (RetrofitService.class){
                if (SERVICE == null){
                    SERVICE = new RetrofitService();
                }
            }
        }
        return SERVICE;
    }

    public <T> T createAPI(String baseUrl, Class<T> apiClass){

        return new Retrofit.Builder()
                .client(CLIENT)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build().create(apiClass);
    }




}
