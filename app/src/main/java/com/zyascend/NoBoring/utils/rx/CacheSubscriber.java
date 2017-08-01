package com.zyascend.NoBoring.utils.rx;

import rx.Subscriber;

/**
 * Created by Administrator on 2016/11/29.
 */

public abstract class CacheSubscriber<T> extends Subscriber<T> {

    @Override
    public void onError(Throwable e) {

        //没网的状态下报错，这时候读取缓存

        T t = null;
        try {
            t = getFromCache();
        } catch (Exception e1) {
            onRealError();
            return;
        }
        if (t == null){
            onRealError();
            return;
        }
        onNext(t);
    }

    protected abstract void onRealError();

    protected abstract T getFromCache();

    @Override
    public void onCompleted() {

    }
}
