package com.zyascend.NoBoring.utils.rx;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 *
 * Created by Administrator on 2016/11/22.
 */

public enum RxTransformer {

        INSTANCE;

    public <T> Observable.Transformer<T,T> transform(final PublishSubject<LifeCycleEvent> lifecycleSubject){

        return new Observable.Transformer<T, T>(){
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                Observable<LifeCycleEvent> lifeObservable
                        = lifecycleSubject.takeFirst(new Func1<LifeCycleEvent, Boolean>() {
                    @Override
                    public Boolean call(LifeCycleEvent lifeCycleEvent) {

                        //若PublishSubject发送的事件与DESTROY/STOP/PAUSE一致时
                        //这个lifeObservable就对外发射数据

                        return LifeCycleEvent.DESTROY.equals(lifeCycleEvent)
                                || LifeCycleEvent.STOP.equals(lifeCycleEvent) || LifeCycleEvent.PAUSE.equals(lifeCycleEvent);
                    }
                });
                return tObservable
                        //一旦lifecycleObservable对外发射了数据，
                        //就自动把当前的Observable(也就是网络请求的Observable)停掉
                        .takeUntil(lifeObservable)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        //默认情况下，doOnSubscribe()执行在 subscribe()发生的线程
                        //doOnSubscribe()之后有 subscribeOn()的话，它将执行在离它最近的subscribeOn()所指定的线程
                        //使其始终运行在主线程
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };

    }
}
