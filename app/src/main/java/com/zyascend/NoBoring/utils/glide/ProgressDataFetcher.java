package com.zyascend.NoBoring.utils.glide;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/10.
 */
public class ProgressDataFetcher implements DataFetcher<InputStream> {

    private static final String TAG = "ProgressDataFetcher";
    private String mUrl;
    private Handler mHandler;
    private boolean isCancel;
    private Call progressCall;
    private InputStream inputStream;

    public ProgressDataFetcher(String url, Handler handler) {
        this.mUrl = url;
        this.mHandler = handler;
    }

    @Override
    public InputStream loadData(Priority priority) throws Exception {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ProgressInterceptor(obtainListener()))
                .build();
        Request request = new Request.Builder().url(mUrl).build();
        try {
            progressCall = client.newCall(request);
            Response response = progressCall.execute();
            if (isCancel){
                return null;
            }
            if (!response.isSuccessful()){
                //处理失败逻辑
                throw new IOException("Unexpected code " + response);
            }
            inputStream = response.body().byteStream();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "loadData: error------>"+e.toString());
            return null;
        }
        return inputStream;
    }

    private ProgressListener obtainListener() {
        return new ProgressListener() {
            @Override
            public void onProgress(long downloadedLength, long contentLength, boolean isDone) {
                Message message = mHandler.obtainMessage();
                message.arg1 = (int) downloadedLength;
                message.arg2 = (int) contentLength;
                message.obj = isDone;
                mHandler.sendMessage(message);
            }
        };
    }

    @Override
    public void cleanup() {
        if (inputStream != null) {
            try {
                inputStream.close();
                inputStream = null;
            } catch (IOException e) {
                inputStream = null;
            }
        }
        if (progressCall != null) {
            progressCall.cancel();
        }
    }

    @Override
    public String getId() {
        return mUrl;
    }

    @Override
    public void cancel() {
        isCancel = true;
    }
}
