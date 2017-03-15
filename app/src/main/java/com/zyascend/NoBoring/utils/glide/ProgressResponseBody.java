package com.zyascend.NoBoring.utils.glide;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 可以回调下载进度的ResponseBody
 * Created by Administrator on 2017/3/10.
 */
public class ProgressResponseBody extends ResponseBody {


    private ResponseBody responseBody;
    private ProgressListener progressListener;
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ProgressListener listener, ResponseBody body) {
        this.responseBody = body;
        this.progressListener = listener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(calculateReadProgress(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source calculateReadProgress(BufferedSource source) {
        return new ForwardingSource(source) {

            long totalReaded = 0;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long currentRead = super.read(sink, byteCount);
                //是否加载完成
                boolean isDone = currentRead == -1;
                //统计总共加载了多少
                totalReaded += !isDone ? currentRead : 0;
                if(progressListener != null)
                    progressListener.onProgress(totalReaded, responseBody.contentLength(),isDone);
                return currentRead;
            }
        };
    }
}
