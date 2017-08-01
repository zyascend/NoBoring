package com.zyascend.NoBoring.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zyascend.NoBoring.http.API;
import com.zyascend.NoBoring.utils.Constants;
import com.zyascend.NoBoring.activity.BaseDetailActivity;
import com.zyascend.NoBoring.adapter.FreshAdapter;
import com.zyascend.NoBoring.base.BaseFlatMap;

import com.zyascend.NoBoring.base.BaseRecyclerFragment;
import com.zyascend.NoBoring.dao.FreshResult;
import com.zyascend.NoBoring.dao.Fresh;
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
 * Created by Administrator on 2016/7/14.
 */
public class FreshFragment extends BaseRecyclerFragment<FreshAdapter> implements SwipeRefreshLayout.OnRefreshListener
        , RecyclerArrayAdapter.OnLoadMoreListener {

    private static final String TAG = "TAG_NewsContent";
    private static final String QUERY_INCLUDE = "url,date,tags,author,title,comment_count,custom_fields";
    private static final String QUERY_CUSTOM = "thumb_c,views";
    private static final String QUERY_TYPE = "get_recent_posts";


    private int page;
    private List<Fresh> mList = new ArrayList<>();
    private API.FreshApi api;
    private boolean isRefresh;


    @Override
    public void doOnInitViews() {
        api = RetrofitService.init().createAPI(Constants.BASE_FRESH_URL, API.FreshApi.class);
    }

    @Override
    protected void doOnItemClick(int position) {
        jumpToDetailActivity(position);
    }


    @Override
    protected FreshAdapter initAdapter() {
        return new FreshAdapter(getActivity());
    }

    private void jumpToDetailActivity(int position) {
        Intent intent = new Intent(getActivity(), BaseDetailActivity.class);
        intent.putExtra(BaseDetailActivity.INTENT_DETAIL_TYPE,0);
        intent.putExtra(BaseDetailActivity.INTENT_ENTITY,mList.get(position));
        startActivity(intent);
    }

    private void loadData() {

        api.getFresh(QUERY_TYPE, QUERY_INCLUDE, page, QUERY_CUSTOM)
                .compose(RxTransformer.INSTANCE.<FreshResult>transform(lifeCycleSubject))
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Log.d(TAG, "-------->do onSubscrible");
                    }
                })
                .flatMap(FlatMapFresh.getInstance())
                .subscribe(new CacheSubscriber<List<Fresh>>() {
                    @Override
                    protected void onRealError() {
                        showError();
                    }

                    @Override
                    protected List<Fresh> getFromCache() {
                        return DaoUtils.getInstance().getFreshs();
                    }

                    @Override
                    public void onNext(List<Fresh> freshes) {
                        showLoadingComplete();
                        if (isRefresh){
                            mList.clear();
                            adapter.clear();
                            isRefresh = false;
                        }
                        mList.addAll(freshes);
                        adapter.addAll(freshes);
                    }
                });
    }

    @Override
    public void onRefresh() {
        page = 1;
        isRefresh = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
                page = 2;
            }
        }, 500);
    }

    @Override
    public void onLoadMore() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
                page++;
            }
        }, 500);
    }


    private static class FlatMapFresh extends BaseFlatMap<FreshResult,List<Fresh>> {

        private static FlatMapFresh instance;

        public static FlatMapFresh getInstance(){
            if (instance == null){
                instance =  new FlatMapFresh();
            }
            return instance;
        }

        @Override
        protected List<Fresh> getFromCache() {
            return DaoUtils.getInstance().getFreshs();
        }

        @Override
        protected Observable<List<Fresh>> doForMap(FreshResult freshResult) {

            List<Fresh> items = new ArrayList<>();
            try {
                for (FreshResult.PostsBean post : freshResult.getPosts()) {
                    Fresh item = new Fresh(
                            post.getId()
                            , post.getTags().get(0).getTitle()
                            , post.getAuthor().getName()
                            , post.getTitle()
                            , post.getUrl()
                            , post.getCustom_fields().getThumb_c().get(0));
                    items.add(item);
                }
            } catch (Exception e) {
                return Observable.error(e);
            }
            return createObservable(items);
        }

        @Override
        protected void saveToCache(List<Fresh> data) {
            DaoUtils.getInstance().saveFresh(data);
        }
    }

}
