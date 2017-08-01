package com.zyascend.NoBoring.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zyascend.NoBoring.http.API;
import com.zyascend.NoBoring.utils.Constants;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.adapter.VideoAdapter;
import com.zyascend.NoBoring.base.BaseRecyclerFragment;
import com.zyascend.NoBoring.dao.BudejieVideo;
import com.zyascend.NoBoring.dao.BudejieVideoResult;
import com.zyascend.NoBoring.http.RetrofitService;
import com.zyascend.NoBoring.utils.rx.RxTransformer;
import com.zyascend.NoBoring.utils.rx.FlatMapBudejieVideo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;

/**
 *
 * Created by Administrator on 2016/11/24.
 */


public class ShiPingListFragment extends BaseRecyclerFragment<VideoAdapter> implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {


    private static final String TAG = "TAG_ShiPingListFragment";
    private static final String BUNDLE = "video_type";

    private static final int TYPE_GIRL = 0;
    private static final int TYPE_GAME = 1;
    private static final int TYPE_ZHISHI = 2;

    @Bind(R.id.recyclerView)
    EasyRecyclerView easyRecyclerView;
    @Bind(R.id.ll_error)
    LinearLayout llError;

    private int mType;
    private String page = "0-20.json";
    private List<BudejieVideo> mList = new ArrayList<>();
    private API.BudejieApi api;
    private boolean isRefresh;

    public static ShiPingListFragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE, position);
        ShiPingListFragment fragment = new ShiPingListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getInt(BUNDLE);
        Log.d(TAG, "onCreate: " + mType);
    }

    @Override
    public void doOnInitViews() {
        api = RetrofitService.init().createAPI(Constants.BASE_URL_BUDEJIE, API.BudejieApi.class);
        getRecyclerView().setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                JCVideoPlayer.onScroll();
            }
        });
    }

    @Override
    protected void doOnItemClick(int position) {
        //donoting
    }

    @Override
    protected VideoAdapter initAdapter() {
        return new VideoAdapter(getActivity());
    }

    private Observable<BudejieVideoResult> getObservableByType(int type) {

        switch (type){
            case 0:
                return api.getGirlVideo(page);
            case 1:
                return api.getLengZhishiVideo(page);
            case 2:
                return api.getGameVideo(page);
            default:
                return api.getGirlVideo(page);
        }
    }

    private void loadData() {

        getObservableByType(mType)
                .compose(RxTransformer.INSTANCE.<BudejieVideoResult>transform(lifeCycleSubject))
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .flatMap(FlatMapBudejieVideo.getInstance(mType))
                .subscribe(new Subscriber<List<BudejieVideo>>() {
                    @Override
                    public void onCompleted() {
                        showLoadingComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showError();
                    }

                    @Override
                    public void onNext(List<BudejieVideo> budejieVideos) {
                        if (adapter == null)return;
                        if (isRefresh){
                            adapter.clear();
                            mList.clear();
                            isRefresh = false;
                        }
                        adapter.addAll(budejieVideos);
                        mList.addAll(budejieVideos);
                    }
                });
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        page = "0-20.json";
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 500);
    }

    @Override
    public void onLoadMore() {

        if (mList == null || mList.isEmpty()){
            return;
        }

        String np = mList.get(mList.size()-1).getNp();
        Log.d(TAG, "onLoadMore: np = "+ np);
        page = np + "-20.json";
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 500);
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void doOnInvisible() {
        super.doOnInvisible();
        JCVideoPlayer.releaseAllVideos();
    }
}
