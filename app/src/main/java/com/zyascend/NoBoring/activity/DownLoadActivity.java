package com.zyascend.NoBoring.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.lzy.okserver.OkDownload;
import com.lzy.okserver.task.XExecutor;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.adapter.DownloadAdapter;
import com.zyascend.NoBoring.base.BaseActivity;
import com.zyascend.NoBoring.utils.view.DividerItemDecoration;
import com.zyascend.NoBoring.utils.view.SpacesItemDecoration;

import butterknife.Bind;

/**
 * 功能：下载管理activity
 * 作者：zyascend on 2017/8/14 16:23
 * 邮箱：zyascend@qq.com
 */

public class DownLoadActivity extends BaseActivity
        implements XExecutor.OnAllTaskEndListener
        , DownloadAdapter.DataListener {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.view_empty)
    RelativeLayout viewEmpty;

    private DownloadAdapter adapter;
    private OkDownload okDownload;

    @Override
    protected void initView() {

        setTitle("我的下载");

        okDownload = OkDownload.getInstance();
        adapter = new DownloadAdapter(this);
        adapter.addDataListener(this);
        adapter.updateData(DownloadAdapter.TYPE_ALL);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpacesItemDecoration(2));
        recyclerView.setAdapter(adapter);

        okDownload.addOnAllTaskEndListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_download;
    }

    @Override
    public void onAllTaskEnd() {
        //do something
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        okDownload.removeOnAllTaskEndListener(this);
        adapter.unRegister();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void dataEmpty() {
        //没有下载信息
        viewEmpty.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }
}
