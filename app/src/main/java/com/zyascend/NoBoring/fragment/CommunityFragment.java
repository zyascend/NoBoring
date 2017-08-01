package com.zyascend.NoBoring.fragment;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.activity.CommentActivity;
import com.zyascend.NoBoring.activity.PublishActivity;
import com.zyascend.NoBoring.activity.UserActivity;
import com.zyascend.NoBoring.adapter.PostAdapter;
import com.zyascend.NoBoring.base.BaseFragment;
import com.zyascend.NoBoring.bean.ListResponse;
import com.zyascend.NoBoring.bean.PostResponse;
import com.zyascend.NoBoring.bean.UpdateResponse;
import com.zyascend.NoBoring.http.LeanCloudService;
import com.zyascend.NoBoring.http.RequestHelper;
import com.zyascend.NoBoring.utils.ActivityUtils;
import com.zyascend.NoBoring.utils.Constants;
import com.zyascend.NoBoring.utils.LogUtils;
import com.zyascend.NoBoring.utils.rx.RxTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

public class CommunityFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener
        , RecyclerArrayAdapter.OnLoadMoreListener, PostAdapter.ItemChildViewListener {

    private static final String TAG = "TAG_CommunityFragment";
    int page;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @Bind(R.id.ll_error)
    LinearLayout llError;

    private boolean isFirstLoad = false;
    private PostAdapter adapter;
    private boolean isRefresh;
    private List<PostResponse> mList = new ArrayList<>();

    @Override
    public void onRefresh() {
        page = 1;
        isRefresh = true;
        loadData();
    }

    private void loadData() {

        Map<String,String> requsetMap = new HashMap<>();
        requsetMap.put("order","createdAt");//or -createdAt
        requsetMap.put("limit","10");
        requsetMap.put("include","poster,picture");
        requsetMap.put("skip",String.valueOf((page-1)*10));

        LeanCloudService.getInstance().getAPI()
                .getAllPost(requsetMap)
                .compose(RxTransformer.INSTANCE.<ListResponse<PostResponse>>transform(lifeCycleSubject))
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoading();
                    }
                })
                .subscribe(new Subscriber<ListResponse<PostResponse>>() {
                    @Override
                    public void onCompleted() {
                        showLoadingComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showError();
                        Log.d(TAG, "onError: "+e.getMessage());
                    }

                    @Override
                    public void onNext(ListResponse<PostResponse> data) {
                        if (data == null || data.getResults() == null || data.getResults().isEmpty())
                            return;
                        if (isRefresh){
                            mList.clear();
                            adapter.clear();
                            isRefresh = false;
                        }
                        mList.addAll(data.getResults());
                        adapter.addAll(data.getResults());
                    }
                });
        page++;
    }

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
        adapter = new PostAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setMore(R.layout.load_more, this);
        adapter.setNoMore(R.layout.no_more);
        adapter.setError(R.layout.load_error);
        adapter.setItemChildViewListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.setRefreshListener(this);
    }

    private void doOnItemClick(int position) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_community;
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


    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.fab)
    public void onClick() {
        ActivityUtils.jumpTo(getActivity(), PublishActivity.class);
    }

    @Override
    public void onLoadMore() {
        loadData();
    }

    @Override
    public void onClick(int position, int which, int operate) {
        switch (which){
            case PostAdapter.VIEW_BTN_COMMENT:
                //添加评论
                Intent intent = new Intent(getActivity(), CommentActivity.class);
                intent.putExtra(CommentActivity.POSTER_ID,mList.get(position).getPosterId());
                intent.putExtra(CommentActivity.POST_ID,mList.get(position).getObjectId());
                startActivity(intent);
                break;
            case PostAdapter.VIEW_BTN_LIKE:
                //点赞
                if (operate == PostAdapter.OPERATE_INCRE)
                    postIncrement(position,"likesCount");
                else
                    postDecrement(position,"likesCount");
                break;
            case PostAdapter.VIEW_IV_AVATAR:
                //打开个人界面
                Intent intent2 = new Intent(getActivity(), UserActivity.class);
                intent2.putExtra(UserActivity.USER_ID,mList.get(position).getPosterId());
                startActivity(intent2);
                break;
        }
    }

    private void postDecrement(int position, String parameter) {
        LeanCloudService.getInstance().getAPI()
                .increment(mList.get(position).getObjectId(),
                        RequestHelper.getIncrementBody(parameter, Constants.OP_DECREMENT))
                .compose(RxTransformer.INSTANCE.<UpdateResponse>transform(lifeCycleSubject))
                .subscribe(new Subscriber<UpdateResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e.getMessage());
                    }

                    @Override
                    public void onNext(UpdateResponse s) {
                        LogUtils.d(s.getUpdatedAt());
                    }
                });
    }

    private void postIncrement(int position, String parameter) {
        LeanCloudService.getInstance().getAPI()
                .increment(mList.get(position).getObjectId(),
                        RequestHelper.getIncrementBody(parameter, Constants.OP_INCREMENT))
                .compose(RxTransformer.INSTANCE.<UpdateResponse>transform(lifeCycleSubject))
                .subscribe(new Subscriber<UpdateResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e.getMessage());
                    }

                    @Override
                    public void onNext(UpdateResponse s) {

                    }
                });
    }
}
