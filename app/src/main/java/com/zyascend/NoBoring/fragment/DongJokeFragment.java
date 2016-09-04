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
import com.zyascend.NoBoring.adapter.DongJokeAdapter;
import com.zyascend.NoBoring.base.BaseFragment;
import com.zyascend.NoBoring.model.DongTaiJokeResult;
import com.zyascend.NoBoring.utils.RetrofitUtils;
import com.zyascend.NoBoring.utils.map.MapDongTaiJoke;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * Created by Administrator on 2016/7/16.
 */
public class DongJokeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    private static final String TAG = "TAG_DongFragement";
    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.iv_error)
    ImageView errorImage;
    @Bind(R.id.tv_error)
    TextView errorTextView;


    private DongJokeAdapter adapter = new DongJokeAdapter();
    private LinearLayoutManager mLayoutManager;
    private int mPage = 1;
    private List<DongTaiJokeResult.ShowapiResBodyBean.DongTaiJoke> mList = new ArrayList<>();

    private void loadData() {
        unsubscrible();
        adapter.setList(null);
        subscription = RetrofitUtils.getDongTaiJokeApi()
                .getDongTaiJoke("20", String.valueOf(mPage), Constants.APP_ID,Constants.APP_SIGN)
                .map(MapDongTaiJoke.getInstance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<DongTaiJokeResult.ShowapiResBodyBean.DongTaiJoke>>() {
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
                        Log.d(TAG, "onError: it is "+e.toString());
                    }

                    @Override
                    public void onNext(List<DongTaiJokeResult.ShowapiResBodyBean.DongTaiJoke> dongTaiJokes) {
                        if (null != swipeRefreshLayout){
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        if (mPage > 1){
                            mList.addAll(dongTaiJokes);
                        }else {
                            mList = dongTaiJokes;
                        }
                        adapter.setList(mList);
                        Log.d(TAG, "onNext: list size = " + mList.size());

                    }
                });

        mPage += 1;
    }


    @Override
    protected void initViews() {

        errorImage.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);

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
        swipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE,Color.GREEN);
        swipeRefreshLayout.setOnRefreshListener(this);
        onRefresh();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_textjoke;
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
