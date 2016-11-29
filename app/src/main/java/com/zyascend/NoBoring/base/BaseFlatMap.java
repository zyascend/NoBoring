package com.zyascend.NoBoring.base;

import android.os.Looper;
import android.util.Log;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 *
 * Created by Administrator on 2016/11/25.
 */

public abstract class BaseFlatMap<T,R> implements Func1<T,Observable<R>> {
    private static final String TAG = "BaseFlatMap";
    @Override
    public Observable<R> call(T t) {

        if (Looper.getMainLooper() == Looper.myLooper()){
            Log.d(TAG, "---------> 主线程");
        }else {
            Log.d(TAG, "---------> 子线程");
        }

        if (t == null){
            Observable<R> cache = Observable.create(new Observable.OnSubscribe<R>() {
                @Override
                public void call(Subscriber<? super R> subscriber) {
                    try {
                        R r = getFromCache();
                        if (r == null){
                            subscriber.onError(new Exception("cache is empty"));
                        }
                        subscriber.onNext(r);
                        subscriber.onCompleted();
                    }catch (Exception e){
                        subscriber.onError(e);
                    }
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            return cache;

        }else {

            return doForMap(t);

        }
    }

    protected abstract R getFromCache();

    protected abstract Observable<R> doForMap(T t);

    public Observable<R> createObservable(final R data){

        return Observable.create(new Observable.OnSubscribe<R>() {
            @Override
            public void call(Subscriber<? super R> subscriber) {
                try {
                    subscriber.onNext(data);
                    saveToCache(data);
                    subscriber.onCompleted();
                }catch (Exception e){
                    subscriber.onError(e);
                }
            }
        });

    }

    protected abstract void saveToCache(R data);
}
