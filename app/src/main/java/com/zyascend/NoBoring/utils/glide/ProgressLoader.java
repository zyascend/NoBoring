package com.zyascend.NoBoring.utils.glide;

import android.os.Handler;

import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.stream.StreamModelLoader;

import java.io.InputStream;

/**
 * 用于显示进度的Glide加载器
 * Created by Administrator on 2017/3/10.
 */

public class ProgressLoader implements StreamModelLoader<String> {

    private Handler handler;

    public ProgressLoader(Handler handler) {
        this.handler = handler;
        if (handler == null){
            throw new RuntimeException("Handler can not be null");
        }
    }

    @Override
    public DataFetcher<InputStream> getResourceFetcher(String model, int width, int height) {
        return new ProgressDataFetcher(model,handler);
    }
}
