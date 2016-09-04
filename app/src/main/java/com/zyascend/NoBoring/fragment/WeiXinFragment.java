package com.zyascend.NoBoring.fragment;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zyascend.NoBoring.Constants;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.activity.DetailActivity;
import com.zyascend.NoBoring.adapter.WeiXinAdapter;
import com.zyascend.NoBoring.base.BaseAdapter;
import com.zyascend.NoBoring.base.BaseFragment;
import com.zyascend.NoBoring.data.Database;
import com.zyascend.NoBoring.model.Item;
import com.zyascend.NoBoring.model.WeiXinResult;
import com.zyascend.NoBoring.utils.ActivityUtils;
import com.zyascend.NoBoring.utils.RetrofitUtils;
import com.zyascend.NoBoring.utils.map.MapWeiXin;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * Created by zyascend on 2016/7/17.
 */
public class WeiXinFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "TAG_WeixinFragment";

    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.iv_error)
    ImageView errorImage;
    @Bind(R.id.tv_error)
    TextView errorTextView;


    private LinearLayoutManager mLayoutManager;
    private WeiXinAdapter adapter ;
    private List<WeiXinResult.WeiXin> mList = new ArrayList<>();
    private int mPage = 1;
    private Database mSqlite = Database.getInstance(getActivity());

    @Override
    protected void initViews() {

        errorImage.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);

        adapter = new WeiXinAdapter(getActivity());
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastPosition = mLayoutManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == adapter.getItemCount()) {
                        loadData();
                    }
                }
            }
        });
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                WeiXinResult.WeiXin weiXin = mList.get(position);
                Item item = new Item("",0,"",weiXin.getTitle(),"",weiXin.getUrl());
                ActivityUtils.startActivityForDetail(getActivity(),item, DetailActivity.TAG_WEIXIN);
            }
        });
        swipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE,Color.GREEN);
        swipeRefreshLayout.setOnRefreshListener(this);
        onRefresh();
    }

    private void loadData() {
        unsubscrible();
        adapter.setList(null);
        subscription = RetrofitUtils.getWeixinApi()
                .getWeixinApi(Constants.TIAN_ID, 10 * mPage)
                .map(MapWeiXin.getInstance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<WeiXinResult.WeiXin>>() {
                    @Override
                    public void onCompleted() {
                        showLoadingComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (null != swipeRefreshLayout){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        showEorror();
                        Log.d(TAG, "onError: it is "+ e.toString());
                    }

                    @Override
                    public void onNext(List<WeiXinResult.WeiXin> weiXins) {
                        if (null != swipeRefreshLayout){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        Log.d(TAG, "onNext: size = " + weiXins.size());
                        if (mPage > 1){
                            mList.addAll(weiXins);
                        }else {
                            mList = weiXins;
                        }
                        adapter.setList(mList);
                    }
                });
        mPage += 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_weixin;
    }

    @Override
    protected void showEorror() {

        if (errorImage != null && errorTextView != null){
            errorTextView.setVisibility(View.VISIBLE);
            errorImage.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void showLoading() {

    }

    @Override
    protected void showLoadingComplete() {
        if (errorImage != null && errorTextView != null){
            errorImage.setVisibility(View.GONE);
            errorTextView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onRefresh() {
        mPage = 1;
        loadData();
    }
}
