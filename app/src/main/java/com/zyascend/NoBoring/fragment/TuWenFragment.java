package com.zyascend.NoBoring.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;

import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zyascend.NoBoring.http.API;
import com.zyascend.NoBoring.utils.Constants;
import com.zyascend.NoBoring.activity.PhotoActivity;
import com.zyascend.NoBoring.adapter.TuWenAdapter;
import com.zyascend.NoBoring.base.BaseFlatMap;
import com.zyascend.NoBoring.base.BaseRecyclerFragment;
import com.zyascend.NoBoring.dao.TuWenJoke;
import com.zyascend.NoBoring.dao.TuWenJokeResult;
import com.zyascend.NoBoring.utils.rx.CacheSubscriber;
import com.zyascend.NoBoring.utils.DaoUtils;
import com.zyascend.NoBoring.http.RetrofitService;
import com.zyascend.NoBoring.utils.rx.RxTransformer;


import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 *
 * Created by zyascend on 2016/7/16.
 */
public class TuWenFragment extends BaseRecyclerFragment<TuWenAdapter> implements SwipeRefreshLayout.OnRefreshListener,RecyclerArrayAdapter.OnLoadMoreListener {


    private static final String TAG = "TAG_DongFragement";
    private static final String REQUEST_TYPE = "10";

    private String mPage;
    private List<TuWenJoke> mList = new ArrayList<>();
    private boolean isRefresh;
    private API.BudejieApi api;
    private int currentNp;


    @Override
    public void doOnInitViews() {
        api = RetrofitService.init().createAPI(Constants.BASE_URL_BUDEJIE, API.BudejieApi.class);

    }

    private void loadData() {
        if (api == null)return;
        api.getTuWenJoke(mPage)
                .compose(RxTransformer.INSTANCE.<TuWenJokeResult>transform(lifeCycleSubject))
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        
                    }
                })
                .map(new Func1<TuWenJokeResult, TuWenJokeResult>() {
                    @Override
                    public TuWenJokeResult call(TuWenJokeResult tuWenJokeResult) {
                        currentNp = tuWenJokeResult.getInfo().getNp();
                        return tuWenJokeResult;
                    }
                })
                .flatMap(TuwenFlatMap.getInstance())
                .subscribe(new CacheSubscriber<List<TuWenJoke>>() {
                    @Override
                    protected void onRealError() {
                        showError();
                    }

                    @Override
                    protected List<TuWenJoke> getFromCache() {
                        return DaoUtils.getInstance().getTuWenJokes();
                    }

                    @Override
                    public void onCompleted() {
                        showLoadingComplete();
                    }


                    @Override
                    public void onNext(List<TuWenJoke> tuWenJokes) {
                        if (isRefresh){
                            mList.clear();
                            adapter.clear();
                            isRefresh = false;
                        }
                        mList.addAll(tuWenJokes);
                        adapter.addAll(tuWenJokes);
                    }
                });
                
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        mPage = "0-20.json";
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 500);
    }

    @Override
    public void onLoadMore() {
        mPage = String.valueOf(currentNp) + "-20.json";
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 500);
    }

    @Override
    protected void doOnItemClick(int position) {
        TuWenJoke joke = mList.get(position);
        Intent i = new Intent(getActivity(), PhotoActivity.class);
        i.putExtra(PhotoActivity.EXTRA_TITLE,joke.getTitle());
        i.putExtra(PhotoActivity.EXTRA_URL,joke.getPicUrl());
        //time 属性就是type属性，表明图片类型
        i.putExtra(PhotoActivity.EXTRA_IS_GIF, TextUtils.equals("gif",mList.get(position).getTime()));
        startActivity(i);
    }

    @Override
    protected TuWenAdapter initAdapter() {
        return new TuWenAdapter(getActivity());
    }

    private static class TuwenFlatMap extends BaseFlatMap<TuWenJokeResult,List<TuWenJoke>> {

        private static TuwenFlatMap instance;


        public static TuwenFlatMap getInstance(){
            if (instance == null){
                instance = new TuwenFlatMap();
            }
            return instance;
        }

        @Override
        protected List<TuWenJoke> getFromCache() {
            return DaoUtils.getInstance().getTuWenJokes();
        }

        @Override
        protected Observable<List<TuWenJoke>> doForMap(TuWenJokeResult tuWenJokeResult) {
            List<TuWenJoke> list = new ArrayList<>();
            try {
                for (TuWenJokeResult.ListBean bean : tuWenJokeResult.getList()){
                    if (bean == null)break;
                    TuWenJoke joke = null;
                    if (TextUtils.equals(bean.getType(),"image")){
                        joke = new TuWenJoke(Long.parseLong(bean.getId())
                                ,bean.getText()
                                ,bean.getImage().getThumbnail_small().get(0)
                                ,bean.getImage().getBig().get(0)
                                ,bean.getImage().getHeight()
                                ,bean.getType()
                                ,bean.getShare_url());
                    }else {
                        joke = new TuWenJoke(Long.parseLong(bean.getId())
                                ,bean.getText()
                                ,bean.getGif().getGif_thumbnail().get(0)
                                ,bean.getGif().getImages().get(0)
                                ,bean.getGif().getHeight()
                                ,bean.getType()
                                ,bean.getShare_url());
                    }
                    list.add(joke);
                }

            }catch (Exception e){
                return Observable.error(e);
            }
            return createObservable(list);
        }

        @Override
        protected void saveToCache(List<TuWenJoke> data) {
            DaoUtils.getInstance().saveTuWenJokes(data);
        }
    }
}
