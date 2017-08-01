package com.zyascend.NoBoring.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zyascend.NoBoring.R;

import butterknife.Bind;

/**
 *
 * Created by Administrator on 2016/11/27.
 */

public abstract class BaseRecyclerFragment<T extends RecyclerArrayAdapter> extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener
        , RecyclerArrayAdapter.OnLoadMoreListener{

    public EasyRecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Bind(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @Bind(R.id.ll_error)
    LinearLayout llError;

    protected T adapter;

    @Override
    protected void lazyLoad() {
        if (recyclerView != null){
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setRefreshing(true);
                    onRefresh();
                }
            });
        }
    }

    @Override
    protected void initViews() {
        llError.setVisibility(View.GONE);
        llError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setRefreshing(true);
                        onRefresh();
                    }
                });
            }
        });
        adapter = initAdapter();
        if (adapter == null){
            return;
        }
        adapter.setMore(R.layout.load_more, this);
        adapter.setNoMore(R.layout.no_more);
        adapter.setError(R.layout.load_error);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                doOnItemClick(position);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.setRefreshListener(this);

        doOnInitViews();
    }

    public void doOnInitViews() {

    }

    protected abstract void doOnItemClick(int position);

    protected abstract T initAdapter();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_easy_recycler;
    }

    @Override
    protected void showError() {
        if (recyclerView != null){
            recyclerView.setRefreshing(false);
            recyclerView.setVisibility(View.GONE);
        }
        if (llError != null){
            llError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void showLoading() {

    }

    @Override
    protected void showLoadingComplete() {

        if (recyclerView != null){
            recyclerView.setRefreshing(false);
            recyclerView.setVisibility(View.VISIBLE);
        }

        if (llError != null){
            llError.setVisibility(View.GONE);
        }
    }

}
