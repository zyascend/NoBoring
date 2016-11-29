package com.zyascend.NoBoring.fragment;

import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;

import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zyascend.NoBoring.API;
import com.zyascend.NoBoring.Constants;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.adapter.TextJokeAdapter;
import com.zyascend.NoBoring.base.BaseFlatMap;

import com.zyascend.NoBoring.base.BaseRecyclerFragment;
import com.zyascend.NoBoring.dao.TextJoke;
import com.zyascend.NoBoring.dao.TextJokeResult;
import com.zyascend.NoBoring.utils.ActivityUtils;
import com.zyascend.NoBoring.utils.CacheSubscriber;
import com.zyascend.NoBoring.utils.DaoUtils;
import com.zyascend.NoBoring.utils.RetrofitService;
import com.zyascend.NoBoring.utils.RxTransformer;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;

/**
 *
 * Created by Administrator on 2016/7/16.
 */
public class TextJokeFragment extends BaseRecyclerFragment<TextJokeAdapter> implements RecyclerArrayAdapter.OnLoadMoreListener
        , SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "TAG_TextJokeFragment";


    private List<TextJoke> mList = new ArrayList<>();
    private String page;
    private API.BudejieApi api;
    private boolean issRefresh;

    @Override
    protected TextJokeAdapter initAdapter() {
        return new TextJokeAdapter(getActivity());
    }

    @Override
    public void doOnInitViews() {
        api = RetrofitService.init().createAPI(Constants.BASE_URL_BUDEJIE, API.BudejieApi.class);
    }

    @Override
    protected void doOnItemClick(final int position) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("选项")
                .setMessage("分享给朋友？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityUtils.share(getActivity(),mList.get(position).getContent(),mList.get(position).getUrl());
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();

    }

    private void loadData() {
        api.getTextJoke(page)
                .compose(RxTransformer.INSTANCE.<TextJokeResult>transform(lifeCycleSubject))
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        //do sonmething
                    }
                })
                .flatMap(new TextJokeFlatMap())
                .subscribe(new CacheSubscriber<List<TextJoke>>() {

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        showLoadingComplete();
                    }

                    @Override
                    protected void onRealError() {
                        showError();
                    }

                    @Override
                    protected List<TextJoke> getFromCache() {
                        return DaoUtils.getInstance().getTextJokes();
                    }

                    @Override
                    public void onNext(List<TextJoke> textJokes) {

                        if (issRefresh){
                            mList.clear();
                            adapter.clear();
                            issRefresh = false;
                        }
                        mList.addAll(textJokes);
                        adapter.addAll(textJokes);
                    }
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_easy_recycler;
    }


    @Override
    public void onRefresh() {
        issRefresh = true;
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
        page = np + "-20.json";
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 500);
    }


    private class TextJokeFlatMap extends BaseFlatMap<TextJokeResult,List<TextJoke>> {

        @Override
        protected List<TextJoke> getFromCache() {
            return DaoUtils.getInstance().getTextJokes();
        }

        @Override
        protected Observable<List<TextJoke>> doForMap(TextJokeResult textJokeResult) {
            List<TextJoke> textJokes = new ArrayList<>();
            try {
                for (TextJokeResult.ListBean list : textJokeResult.getList()){
                    TextJoke textJoke = new TextJoke(Long.parseLong(list.getId())
                            ,list.getShare_url()
                            ,list.getText()
                            ,String.valueOf(textJokeResult.getInfo().getNp()));
                    textJokes.add(textJoke);
                }

            }catch (Exception e){
                return Observable.error(e);
            }
            return createObservable(textJokes);
        }

        @Override
        protected void saveToCache(List<TextJoke> data) {
            DaoUtils.getInstance().saveTextJokes(data);
        }
    }
}
