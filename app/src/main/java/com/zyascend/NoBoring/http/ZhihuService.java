package com.zyascend.NoBoring.http;

import android.text.TextUtils;
import android.util.Log;

import com.zyascend.NoBoring.utils.Constants;
import com.zyascend.NoBoring.base.BaseApplication;
import com.zyascend.NoBoring.utils.LogUtils;
import com.zyascend.NoBoring.utils.NetStateUtil;

import java.io.File;
import java.io.IOException;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/11/27.
 */

public class ZhihuService {

    private static final int DEFAULT_TIMEOUT = 20000;
    private API.ZhiHuApi zhiHuApi;
    private static ZhihuService INSTANCE;

    private ZhihuService(){

        File cacheDir = new File(BaseApplication.context.getExternalCacheDir(),"zhiHuApi-cache");
        //最大缓存为10M
        Cache cache = new Cache(cacheDir,10*1024*1024);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cache(cache)
                .addInterceptor(LOG_INTERCEPTOR)
                .addInterceptor(CACHE_INTERCEPTOR)
                .addNetworkInterceptor(CACHE_INTERCEPTOR)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_ZHIHU)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        zhiHuApi = retrofit.create(API.ZhiHuApi.class);
    }


    public static ZhihuService getInstance(){
        if (INSTANCE == null){
            synchronized (ZhihuService.class){
                if (INSTANCE == null){
                    INSTANCE = new ZhihuService();
                }
            }
        }
        return INSTANCE;
    }

    public API.ZhiHuApi getAPI(){
        return zhiHuApi;
    }

    private static final int MAX_AGE_DEFAULT = 30;
    private static final int MAX_STALE_DEFAULT = 60 * 60 * 6; // 没网失效6小时

    public static final Interceptor CACHE_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            if (!NetStateUtil.isNetworkAvailable()){
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response baseResponse = chain.proceed(request);
            Response finalResponse;

            if (NetStateUtil.isNetworkAvailable()){
                //检查CacheControl是否为空
                String cacheControl = request.cacheControl().toString();
                if (TextUtils.isEmpty(cacheControl)){
                    cacheControl = "public, max-age=" + MAX_AGE_DEFAULT;
                }

                finalResponse = baseResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .removeHeader("ETag")
                        .header("Cache-Control", cacheControl)
                        .header("X-LC-Id","ioJGwMTChBTt0qRdhWFhhEye-gzGzoHsz")
                        .header("X-LC-Key","VVIjOnCkOuS4s2ETvmCavUus")
                        .build();
            }else {
                finalResponse = baseResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .removeHeader("ETag")
                        //标识只使用缓存（当前无网）
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + MAX_STALE_DEFAULT)
                        .header("X-LC-Id","ioJGwMTChBTt0qRdhWFhhEye-gzGzoHsz")
                        .header("X-LC-Key","VVIjOnCkOuS4s2ETvmCavUus")
                        .build();
            }
            return finalResponse;
        }
    };


    public static final Interceptor LOG_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
            log(String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            log(String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            log("body = "+bodyToString(request));
            return response;
        }
    };

    private static String bodyToString(final Request request){
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();

            if (copy.body() != null){
                copy.body().writeTo(buffer);
            }

            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    private static void log(String msg){
        LogUtils.d("[ Request ] ",msg);
    }

}
