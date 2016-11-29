package com.zyascend.NoBoring.base;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.leakcanary.RefWatcher;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.utils.LifeCycleEvent;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;
import rx.subjects.PublishSubject;

/**
 *
 * Created by Administrator on 2016/7/13.
 */
public abstract class BaseFragment extends Fragment{

    private static final String TAG = "BaseFragment";
    protected Subscription subscription;
    public final PublishSubject<LifeCycleEvent> lifeCycleSubject = PublishSubject.create();
    public Handler handler = new Handler();
    protected boolean isInit = false;
    protected boolean isLoad = false;
    protected View rootView;
    private static final String STATE_SAVE_IS_HIDDEN = "hidden";
    private boolean isVisible;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        lifeCycleSubject.onNext(LifeCycleEvent.CREATE);
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
            ButterKnife.bind(this, rootView);
            initViews();
        }

        ButterKnife.bind(this,rootView);

//        isInit = true;
//        /**初始化的时候去加载数据**/
//        isCanLoadData();
        Log.d(TAG, "-----> lazy loaded");
        lazyLoad();
        return rootView;
    }


    @Override
    public void onPause() {
        lifeCycleSubject.onNext(LifeCycleEvent.PAUSE);
        Log.d(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    public void onStop() {
        lifeCycleSubject.onNext(LifeCycleEvent.STOP);
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    public void onStart() {
        lifeCycleSubject.onNext(LifeCycleEvent.START);
        Log.d(TAG, "onStart: ");
        super.onStart();
    }

    /**
     * 视图是否已经对用户可见，系统的方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        isCanLoadData();
        if (!getUserVisibleHint()){
            doOnInvisible();
        }else {
            doOnVisible();
        }
    }

    public void doOnVisible() {

    }

    public void doOnInvisible() {

    }


    private void isCanLoadData() {
        if (!isInit) {
            return;
        }

        if (getUserVisibleHint()) {
            lazyLoad();
            isLoad = true;
        } else {
            if (isLoad) {
                stopLoad();
            }
        }
    }

    @Override
    public void onDestroyView() {
        lifeCycleSubject.onNext(LifeCycleEvent.DESTROY);
        super.onDestroyView();
        unsubscrible();
        isInit = false;
        isLoad = false;
        ButterKnife.unbind(this);

        RefWatcher refWatcher = BaseApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    protected void unsubscrible(){
        if (subscription != null && subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }



    protected abstract void lazyLoad();

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
     */
    protected void stopLoad() {}


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_SAVE_IS_HIDDEN,isHidden());
        super.onSaveInstanceState(outState);

    }


    protected abstract void initViews();

    protected abstract int getLayoutId();

    protected abstract void showError();

    protected abstract void showLoading();

    protected abstract void showLoadingComplete();


    static{
        RxJavaPlugins.getInstance().registerErrorHandler(new RxJavaErrorHandler() {
            @Override
            public void handleError(Throwable e) {
                e.printStackTrace();
                Log.e(TAG, "handleError: e= "+e.toString());
            }
        });
    }

}
