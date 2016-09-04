package com.zyascend.NoBoring.fragment;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.activity.DetailActivity;
import com.zyascend.NoBoring.adapter.FreshAdapter;
import com.zyascend.NoBoring.base.BaseAdapter;
import com.zyascend.NoBoring.base.BaseFragment;

import com.zyascend.NoBoring.data.Database;
import com.zyascend.NoBoring.model.Item;
import com.zyascend.NoBoring.utils.ActivityUtils;
import com.zyascend.NoBoring.utils.RetrofitUtils;
import com.zyascend.NoBoring.utils.map.MapFresh;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;
import rx.schedulers.Schedulers;

/**
 *
 * Created by Administrator on 2016/7/14.
 */
public class FreshFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "TAG_NewsContent";

    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.iv_error)
    ImageView errorImage;
    @Bind(R.id.tv_error)
    TextView errorTextView;

    private FreshAdapter adapter;
    private int page;
    private List<Item> mList = new ArrayList<>();
    private Database mSqlite;
    private LinearLayoutManager mLayoutManager;

    public FreshFragment(){}


    @Override
    protected void initViews() {

        errorImage.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);

        mSqlite = Database.getInstance(getActivity());
        adapter = new FreshAdapter(getActivity());
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    Log.d(TAG, "onScrollStateChanged: IDLE");
                    int lastPosition = mLayoutManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == adapter.getItemCount()){
                        loadData();
                        Log.d(TAG, "onScrollStateChanged: loadData()");
                    }

                }
            }
        });

        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion) {
                ActivityUtils.startActivityForDetail(getActivity(),mList.get(postion), DetailActivity.TAG_FRESH);
            }
        });
        swipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE,Color.GREEN);
        swipeRefreshLayout.setOnRefreshListener(this);
        onRefresh();

    }

    private void loadData() {

        unsubscrible();
        adapter.setList(null);

        subscription = RetrofitUtils.getFreshApi()
                .getFresh("get_recent_posts"
                        , "url,date,tags,author,title,comment_count,custom_fields"
                        , "thumb_c,views"
                        , page)
                .map(MapFresh.getInstance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Item>>() {
                    @Override
                    public void onCompleted() {
                        showLoadingComplete();
                    }

                    @Override
                    public void onError(Throwable e) {

                        mList = mSqlite.getAllNews();

                        if (null != mList){
                            adapter.setList(mList);
                            Log.d(TAG, "onError: picUrl = " + mList.get(0).getUrl());
                        }else {
                            showEorror();
                        }
                        if (swipeRefreshLayout != null){
                            swipeRefreshLayout.setRefreshing(false);
                        }

                    }

                    @Override
                    public void onNext(List<Item> items) {
                        if (swipeRefreshLayout != null){
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        if (page > 1){
                            mList.addAll(items);
                        }else {
                            mList = items;
                        }
                        adapter.setList(mList);
                    }
                });

        page += 1;
    }

    @Override
    public void onRefresh() {
        page = 1;
        loadData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_fresh;
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

    static {
        RxJavaPlugins.getInstance().registerErrorHandler(new RxJavaErrorHandler() {
            @Override
            public void handleError(Throwable e) {
                e.printStackTrace();
                Log.e(TAG, "handleError: e= "+e.toString());
            }
        });
    }
}
