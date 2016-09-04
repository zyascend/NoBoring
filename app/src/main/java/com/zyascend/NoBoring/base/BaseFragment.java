package com.zyascend.NoBoring.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.leakcanary.RefWatcher;
import com.zyascend.NoBoring.R;

import butterknife.ButterKnife;
import rx.Subscription;

/**
 *
 * Created by Administrator on 2016/7/13.
 */
public abstract class BaseFragment extends Fragment{

    protected Subscription subscription;

    protected View rootView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
            ButterKnife.bind(this, rootView);
            initViews();
        }
        ButterKnife.bind(this,rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscrible();
        ButterKnife.unbind(this);

        RefWatcher refWatcher = BaseApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    protected void unsubscrible(){
        if (subscription != null && subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }


    protected abstract void initViews();

    protected abstract int getLayoutId();

    protected abstract void showEorror();

    protected abstract void showLoading();

    protected abstract void showLoadingComplete();


}
