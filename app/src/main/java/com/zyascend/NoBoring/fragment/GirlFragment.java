package com.zyascend.NoBoring.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zyascend.NoBoring.http.API;
import com.zyascend.NoBoring.utils.Constants;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.activity.PhotoActivity;
import com.zyascend.NoBoring.adapter.GirlAdapter;
import com.zyascend.NoBoring.base.BaseFlatMap;
import com.zyascend.NoBoring.base.BaseFragment;
import com.zyascend.NoBoring.dao.Girl;
import com.zyascend.NoBoring.dao.GirlResult;
import com.zyascend.NoBoring.utils.rx.CacheSubscriber;
import com.zyascend.NoBoring.utils.DaoUtils;
import com.zyascend.NoBoring.http.RetrofitService;
import com.zyascend.NoBoring.utils.rx.RxTransformer;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import rx.Observable;
import rx.functions.Action0;


/**
 *
 * Created by Administrator on 2016/7/19.
 */
public class GirlFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener
        ,RecyclerArrayAdapter.OnLoadMoreListener {

    private static final String TAG = "TAG_GirlFragment";

    @Bind(R.id.recyclerView)
    EasyRecyclerView easyRecyclerview;
    @Bind(R.id.ll_error)
    LinearLayout llError;

    private List<Girl> mList;
    private GirlAdapter adapter;
    private int page = 1;
    private API.GirlApi api;
    private boolean isRefresh;

    @Override
    protected void lazyLoad() {
        easyRecyclerview.post(new Runnable() {
            @Override
            public void run() {
                easyRecyclerview.setRefreshing(true);
                onRefresh();
            }
        });
    }

    @Override
    protected void initViews() {

        mList = new ArrayList<>();
        adapter = new GirlAdapter(getContext());
        adapter.setMore(R.layout.load_more,this);
        adapter.setNoMore(R.layout.no_more);
        adapter.setError(R.layout.load_error);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                enterActivity(position);
            }
        });

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        easyRecyclerview.setLayoutManager(staggeredGridLayoutManager);
        easyRecyclerview.setAdapterWithProgress(adapter);
        easyRecyclerview.setRefreshListener(this);

        api = RetrofitService.init().createAPI(Constants.BASE_GANK_URL, API.GirlApi.class);


    }

    private void enterActivity(int position) {
        Intent intent = new Intent(getActivity(),PhotoActivity.class);
        intent.putExtra("title",adapter.getItem(position).getDesc());
        intent.putExtra("url",adapter.getItem(position).getUrl());
        intent.putExtra("isGif",false);
        startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_easy_recycler;
    }

    @Override
    protected void showError() {
        if (llError != null){
            llError.setVisibility(View.VISIBLE);
        }

        if (easyRecyclerview != null){
            easyRecyclerview.setRefreshing(false);
            easyRecyclerview.setVisibility(View.GONE);
        }
    }

    @Override
    protected void showLoading() {

    }

    @Override
    protected void showLoadingComplete() {
        if (llError != null){
            llError.setVisibility(View.GONE);
        }

        if (easyRecyclerview != null){
            easyRecyclerview.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        page = 1;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.clear();
                loadGirl(20,page);
                page = 2;
            }
        },1000);
        Log.d(TAG, "onRefresh: ");
    }

    private void loadGirl(final int num, int page) {

        api.getGirl(num,page)
                .compose(RxTransformer.INSTANCE.<GirlResult>transform(lifeCycleSubject))
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .flatMap(MapGirl.getInstance())
                .subscribe(new CacheSubscriber<List<Girl>>() {
                    @Override
                    protected void onRealError() {
                        showError();
                    }

                    @Override
                    protected List<Girl> getFromCache() {
                        return DaoUtils.getInstance().getGirls();
                    }

                    @Override
                    public void onNext(List<Girl> girls) {
                        showLoadingComplete();
                        if (isRefresh){
                            mList.clear();
                            adapter.clear();
                            isRefresh = false;
                        }
                        mList.addAll(girls);
                        adapter.addAll(girls);
                    }
                });
    }

    @Override
    public void onLoadMore() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadGirl(20,page);
                page++;
            }
        }, 1000);
    }

    private static class MapGirl extends BaseFlatMap<GirlResult,List<Girl>> {

        private static MapGirl INSTANCE;

        @Override
        protected List<Girl> getFromCache() {
            return DaoUtils.getInstance().getGirls();
        }

        @Override
        protected Observable<List<Girl>> doForMap(GirlResult girlResult) {
            List<Girl> list;
            try {
                list = girlResult.girls;
            }catch (Exception e){
                return Observable.error(e);
            }
            return createObservable(list);
        }

        @Override
        protected void saveToCache(List<Girl> data) {
            DaoUtils.getInstance().saveGirls(data);
        }

        public static MapGirl getInstance() {
            if (INSTANCE == null){
                INSTANCE = new MapGirl();
            }
            return INSTANCE;
        }
    }
}
