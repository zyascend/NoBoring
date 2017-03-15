package com.zyascend.NoBoring.utils.glide;

/**
 *
 * Created by Administrator on 2017/3/10.
 */

public interface ProgressListener {
    void onProgress(long downloadedLength,long contentLength,boolean isDone);

}
