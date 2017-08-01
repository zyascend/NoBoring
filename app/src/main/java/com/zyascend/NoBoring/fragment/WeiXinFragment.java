package com.zyascend.NoBoring.fragment;

import android.content.Intent;

import android.support.v4.widget.SwipeRefreshLayout;


import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zyascend.NoBoring.http.API;
import com.zyascend.NoBoring.utils.Constants;
import com.zyascend.NoBoring.activity.BaseDetailActivity;
import com.zyascend.NoBoring.adapter.WeiXinAdapter;
import com.zyascend.NoBoring.base.BaseFlatMap;
import com.zyascend.NoBoring.base.BaseRecyclerFragment;
import com.zyascend.NoBoring.dao.WeiXin;
import com.zyascend.NoBoring.dao.WeiXinResult;
import com.zyascend.NoBoring.utils.rx.CacheSubscriber;
import com.zyascend.NoBoring.utils.DaoUtils;
import com.zyascend.NoBoring.http.RetrofitService;
import com.zyascend.NoBoring.utils.rx.RxTransformer;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action0;

/**
 *
 * Created by zyascend on 2016/7/17.
 */
public class WeiXinFragment extends BaseRecyclerFragment<WeiXinAdapter> implements SwipeRefreshLayout.OnRefreshListener,
        RecyclerArrayAdapter.OnLoadMoreListener{

    private static final String TAG = "TAG_WeixinFragment";


    private List<WeiXin> mList = new ArrayList<>();
    private int mPage = 1;
    private API.WeiXinNewsApi api;
    private boolean isRefresh;

    @Override
    protected WeiXinAdapter initAdapter() {
        return new WeiXinAdapter(getActivity());
    }

    @Override
    public void doOnInitViews() {
        api = RetrofitService.init().createAPI(Constants.TIAN_URL, API.WeiXinNewsApi.class);
    }

    @Override
    protected void doOnItemClick(int position) {
        Intent intent = new Intent(getActivity(), BaseDetailActivity.class);
        intent.putExtra(BaseDetailActivity.INTENT_DETAIL_TYPE,1);
        intent.putExtra(BaseDetailActivity.INTENT_ENTITY,mList.get(position));
        startActivity(intent);
    }


    private void loadData() {
        if (api == null)return;
        api.getWeixinApi(Constants.TIAN_ID, 10 * mPage)
                .compose(RxTransformer.INSTANCE.<WeiXinResult>transform(lifeCycleSubject))
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .flatMap(new WeiXinFlatMap())
                .subscribe(new CacheSubscriber<List<WeiXin>>() {
                    @Override
                    protected void onRealError() {
                        showError();
                    }

                    @Override
                    protected List<WeiXin> getFromCache() {
                        return DaoUtils.getInstance().getWeiXins();
                    }

                    @Override
                    public void onCompleted() {
                        showLoadingComplete();
                    }

                    @Override
                    public void onNext(List<WeiXin> weiXins) {
                        if (isRefresh){
                            mList.clear();
                            adapter.clear();
                            mList.addAll(weiXins);
                            adapter.addAll(weiXins);
                        }else {
                            int size = weiXins.size();
                            if (size > 10){
                                List<WeiXin> newdata = weiXins.subList(size-11,size-1);
                                mList.addAll(newdata);
                                adapter.addAll(newdata);
                            }
                        }
                        isRefresh = false;
                    }
                });
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        mPage = 1;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
                mPage = 2;
            }
        }, 500);
    }

    @Override
    public void onLoadMore() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
                mPage++;
            }
        }, 500);
    }

    private class WeiXinFlatMap extends BaseFlatMap<WeiXinResult,List<WeiXin>> {
        @Override
        protected List<WeiXin> getFromCache() {
            return DaoUtils.getInstance().getWeiXins();
        }

        @Override
        protected Observable<List<WeiXin>> doForMap(WeiXinResult weiXinResult) {
            List<WeiXin> weiXins;
            try {
                weiXins = weiXinResult.getNewslist();
            }catch (Exception e){
                return Observable.error(e);
            }
            return createObservable(weiXins);
        }

        @Override
        protected void saveToCache(List<WeiXin> data) {
            DaoUtils.getInstance().saveWeiXin(data);
        }
    }
}
