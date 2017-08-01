package com.zyascend.NoBoring.http;

import com.zyascend.NoBoring.base.BaseApplication;
import com.zyascend.NoBoring.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.zyascend.NoBoring.http.ZhihuService.CACHE_INTERCEPTOR;
import static com.zyascend.NoBoring.http.ZhihuService.LOG_INTERCEPTOR;

/**
 * 功能：
 * 作者：zyascend on 2017/7/23 10:51
 * 邮箱：zyascend@qq.com
 */

public class LeanCloudService {

    private static final int DEFAULT_TIMEOUT = 20000;
    private API.LeanCloudAPI leanCloudAPI;
    private static LeanCloudService INSTANCE;

    private LeanCloudService(){

        File cacheDir = new File(BaseApplication.context.getExternalCacheDir(),"zhiHuApi-cache");
        //最大缓存为10M
        Cache cache = new Cache(cacheDir,10*1024*1024);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.cache(cache)
                .addInterceptor(LOG_INTERCEPTOR)
                .addInterceptor(CACHE_INTERCEPTOR)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("X-LC-Id", "ioJGwMTChBTt0qRdhWFhhEye-gzGzoHsz")
                                .addHeader("X-LC-Key", "VVIjOnCkOuS4s2ETvmCavUus")
                                .build();
                        return chain.proceed(request);
                    }

                })
                .addNetworkInterceptor(CACHE_INTERCEPTOR)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_LEANCLOUD)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        leanCloudAPI = retrofit.create(API.LeanCloudAPI.class);

    }


    public static LeanCloudService getInstance(){
        if (INSTANCE == null){
            synchronized (LeanCloudService.class){
                if (INSTANCE == null){
                    INSTANCE = new LeanCloudService();
                }
            }
        }
        return INSTANCE;
    }

    public API.LeanCloudAPI getAPI(){
        return leanCloudAPI;
    }
}
