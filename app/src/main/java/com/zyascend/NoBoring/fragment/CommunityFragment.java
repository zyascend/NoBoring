package com.zyascend.NoBoring.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.activity.CircleDetailActivity;
import com.zyascend.NoBoring.activity.PublishActivity;
import com.zyascend.NoBoring.adapter.CommunityAdapter;
import com.zyascend.NoBoring.base.BaseAdapter;
import com.zyascend.NoBoring.base.BaseFragment;
import com.zyascend.NoBoring.utils.AVObjectKeysInterface;
import com.zyascend.NoBoring.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;




public class CommunityFragment extends BaseFragment
        implements AVObjectKeysInterface, View.OnClickListener
        , SwipeRefreshLayout.OnRefreshListener, BaseAdapter.OnItemClickListener {

    private static final String TAG = "TAG_CommunityFragment";
    AVQuery<AVObject> avQuery;
    int page = 0;

    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private boolean isFirstLoad = false;
    private List<AVObject> mlist = new ArrayList<>();
    private CommunityAdapter adapter;


    @Override
    public void onItemClick(int position) {
        if (mlist.isEmpty() || mlist.size() < position) return;
        Intent intent = new Intent(new Intent(getActivity(), CircleDetailActivity.class));
        intent.putExtra(CircleDetailActivity.INTENT_CIRCLE_ID, mlist.get(position).getObjectId());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        page = 0;
        isFirstLoad = true;
        avQuery.skip(0);
        loadData();
    }

    private void loadData() {
        Log.d(TAG, "loadData: start fetching...");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                swipeRefreshLayout.setRefreshing(false);
                if (e == null) {
                    if (isFirstLoad) {
                        adapter.clear();
                        mlist.clear();
                    }
                    adapter.addAll(list);
                    mlist.addAll(list);
                    // TODO: 2017/3/11 list为空时怎么办。
                    Log.d(TAG, "done: list size = " + list.size());
                } else {
//                    showError();
                    Log.d(TAG, "error:---------> " + e.toString());
                }
            }
        });
        page++;
    }


    public void onLoadMore() {
        Log.d(TAG, "onLoadMore: 加载第" + (page + 1) + "页");
        isFirstLoad = false;
        avQuery.skip(page * 10);
        loadData();
    }

    @Override
    protected void lazyLoad() {
        avQuery = new AVQuery<>(CIRCLES);
        avQuery.orderByDescending(CREATE_AT);
        avQuery.selectKeys(Arrays.asList(TITLE, CONTENT, POSTER, UPDATE_AT
                , COMMENT_NUM, LIKES_NUM, PIC_1, PIC_2, PIC_3));
        avQuery.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        avQuery.setMaxCacheAge(10 * 60); //设置缓存有效期为十分钟
        avQuery.limit(10);
        assert fab != null;
        fab.setOnClickListener(this);

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        });
    }

    @Override
    protected void initViews() {
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new CommunityAdapter(getActivity());
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_community;
    }

    @Override
    protected void showError() {

    }

    @Override
    protected void showLoading() {

    }

    @Override
    protected void showLoadingComplete() {

    }

    @Override
    public void onClick(View v) {
        ActivityUtils.jumpTo(getActivity(), PublishActivity.class);
    }


    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

}
