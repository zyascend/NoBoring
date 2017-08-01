package com.zyascend.NoBoring.utils.glide;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 *
 * Created by Administrator on 2017/3/10.
 */

public class ProgressInterceptor implements Interceptor {

    private ProgressListener listener;

    public ProgressInterceptor(ProgressListener listener) {
        this.listener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new ProgressResponseBody(listener,originalResponse.body()))
                .build();
    }
}
