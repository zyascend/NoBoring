package com.zyascend.NoBoring.fragment;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.activity.CircleDetailActivity;
import com.zyascend.NoBoring.activity.PublishActivity;
import com.zyascend.NoBoring.adapter.CommunityAdapter;
import com.zyascend.NoBoring.base.BaseRecyclerFragment;
import com.zyascend.NoBoring.utils.AVObjectKeysInterface;
import com.zyascend.NoBoring.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 *
 * Created by Administrator on 2017/3/8.
 */

public class CommunityFragment extends BaseRecyclerFragment<CommunityAdapter>
        implements AVObjectKeysInterface, View.OnClickListener {

    private static final String TAG = "TAG_CommunityFragment";
    AVQuery<AVObject> avQuery;
    int page = 0;

    @Bind(R.id.fab)
    FloatingActionButton fab;
    private boolean isFirstLoad = false;
    private List<AVObject> mlist = new ArrayList<>();

    @Override
    public void doOnInitViews() {
        super.doOnInitViews();
        avQuery = new AVQuery<>(CIRCLES);
        avQuery.orderByDescending(CREATE_AT);
        avQuery.selectKeys(Arrays.asList(TITLE, CONTENT,POSTER,UPDATE_AT
                ,COMMENT_NUM,LIKES_NUM,PIC_1,PIC_2,PIC_3));
        avQuery.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        avQuery.setMaxCacheAge(10 * 60); //设置缓存有效期为十分钟
        avQuery.limit(10);
        assert fab != null;
        fab.setOnClickListener(this);
    }


    @Override
    protected void doOnItemClick(int position) {
        if (mlist.isEmpty() || mlist.size()<position)return;
        Intent intent = new Intent(new Intent(getActivity(), CircleDetailActivity.class));
        intent.putExtra(CircleDetailActivity.INTENT_CIRCLE_ID,mlist.get(position).getObjectId());
        startActivity(intent);
    }

    @Override
    protected CommunityAdapter initAdapter() {
        return new CommunityAdapter(getActivity());
    }

    @Override
    public void onRefresh() {
        page = 0;
        isFirstLoad = true;
        avQuery.skip(0);
        loadData();
    }

    private void loadData() {
//        avQuery.include("owner");
        Log.d(TAG, "loadData: start fetching...");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (isFirstLoad){
                        adapter.clear();
                        mlist.clear();
                    }
                    adapter.addAll(list);
                    mlist.addAll(list);
                    // TODO: 2017/3/11 list为空时怎么办。
                    Log.d(TAG, "done: list size = "+list.size());
                } else {
//                    showError();
                    Log.d(TAG, "error:---------> "+e.toString());
                }
            }
        });
        page++;
    }

    @Override
    public void onLoadMore() {
        Log.d(TAG, "onLoadMore: 加载第"+(page+1)+"页");
        isFirstLoad = false;
        avQuery.skip(page * 10);
        loadData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_community;
    }

    @Override
    public void onClick(View v) {
        ActivityUtils.jumpTo(getActivity(),PublishActivity.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }
}
