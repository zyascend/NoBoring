package com.zyascend.NoBoring.fragment;

import android.graphics.Color;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zyascend.NoBoring.Constants;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.adapter.TextJokeAdapter;
import com.zyascend.NoBoring.base.BaseAdapter;
import com.zyascend.NoBoring.base.BaseFragment;
import com.zyascend.NoBoring.data.Database;
import com.zyascend.NoBoring.model.TextJokeResult;
import com.zyascend.NoBoring.utils.ActivityUtils;
import com.zyascend.NoBoring.utils.RetrofitUtils;
import com.zyascend.NoBoring.utils.map.MapTextJoke;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.internal.util.unsafe.MpmcArrayQueue;
import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;
import rx.schedulers.Schedulers;

/**
 *
 * Created by Administrator on 2016/7/16.
 */
public class TextJokeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseAdapter.OnItemClickListener {

    private static final String TAG = "TAG_TextJokeFragment";
    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.iv_error)
    ImageView errorImage;
    @Bind(R.id.tv_error)
    TextView errorTextView;

    private Database mSqlite = Database.getInstance(getActivity());


    private TextJokeAdapter adapter = new TextJokeAdapter(getActivity());
    private LinearLayoutManager mLayoutManager;
    private int mPage = 1;
    private List<TextJokeResult.ShowapiResBodyBean.TextJoke> mList = new ArrayList<>();

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

        adapter.setOnItemClickListener(this);

        swipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE,Color.GREEN);
        swipeRefreshLayout.setOnRefreshListener(this);
        onRefresh();
    }


    private void loadData() {

        /**
         * TODO
         * 封装String类型参数
         */
        unsubscrible();
        subscription = RetrofitUtils.getTextJokeApi()
                .getTextJoke("20",String.valueOf(mPage), Constants.APP_ID,Constants.START_TIME,Constants.APP_SIGN)
                .map(MapTextJoke.getInstance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<TextJokeResult.ShowapiResBodyBean.TextJoke>>() {
                    @Override
                    public void onCompleted() {
                        showLoadingComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (null != swipeRefreshLayout){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        mList = mSqlite.getAllJokes();
                        if (mList != null){
                            adapter.setList(mList);
                        }else {
                            showEorror();
                        }
                        Log.d(TAG, "onError: it is : "+e.toString());
                    }

                    @Override
                    public void onNext(List<TextJokeResult.ShowapiResBodyBean.TextJoke> textJokes) {

                        if (null != swipeRefreshLayout){
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        if (mPage > 1){
                            mList.addAll(textJokes);
                        }else {
                            mList = textJokes;
                        }
                        Log.d(TAG, "onNext: mlist = "+ mList.size());
                        adapter.setList(textJokes);
                    }
                });

        mPage += 1;
    }


//    private String getLatestTime() {
//        String time = "";
//        try {
//          SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//          Date curDate = new Date(System.currentTimeMillis());//获取当前时间
//          time = formatter.format(curDate);
//        } catch (Exception e) {
//          e.printStackTrace();
//          Log.d(TAG, "getLatestTime: 格式化时间出错");
//        }
//        return "2015-07-10";
//    }

    @Override
    public void onRefresh() {
        mPage = 1;
        loadData();
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
    public void onItemClick(int position) {
        ActivityUtils.shareJokes(getActivity(),mList.get(position).getText());
    }

}
