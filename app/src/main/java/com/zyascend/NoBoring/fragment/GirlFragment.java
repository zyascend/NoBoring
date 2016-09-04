package com.zyascend.NoBoring.fragment;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.activity.PhotoActivity;
import com.zyascend.NoBoring.adapter.GirlAdapter;
import com.zyascend.NoBoring.base.BaseFragment;
import com.zyascend.NoBoring.data.Database;
import com.zyascend.NoBoring.model.GirlResult;
import com.zyascend.NoBoring.utils.RetrofitUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 *
 * Created by Administrator on 2016/7/19.
 */
public class GirlFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener
        ,RecyclerArrayAdapter.OnLoadMoreListener {

    private static final String TAG = "TAG_GirlFragment";

    @Bind(R.id.easy_recyclerview)
    EasyRecyclerView easyRecyclerview;

    private List<GirlResult.Girl> mList;
    private GirlAdapter adapter;
    private Handler handler = new Handler();
    private int page = 1;
    private Database mSqlite;

    @Override
    protected void initViews() {

        mSqlite = Database.getInstance(getActivity());
        mList = new ArrayList<>();
        adapter = new GirlAdapter(getContext());
        adapter.setMore(R.layout.load_more,this);
        adapter.setNoMore(R.layout.no_more);
        adapter.setError(R.layout.load_error);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                enterActivity(position);
            }
        });

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        easyRecyclerview.setLayoutManager(staggeredGridLayoutManager);
        easyRecyclerview.setAdapterWithProgress(adapter);
        easyRecyclerview.setRefreshListener(this);

        onRefresh();

        Log.d(TAG, "initView: ");
    }

    private void enterActivity(int position) {
        Intent intent = new Intent(getActivity(),PhotoActivity.class);
        intent.putExtra("title",adapter.getItem(position).desc);
        intent.putExtra("url",adapter.getItem(position).url);
        intent.putExtra("isGif",false);
        startActivity(intent);

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_girl;
    }

    @Override
    protected void showEorror() {

    }

    @Override
    protected void showLoading() {

    }

    @Override
    protected void showLoadingComplete() {

    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.clear();
                loadGirl(20,1);
                page = 2;
            }
        },1000);
        Log.d(TAG, "onRefresh: ");
    }

    private void loadGirl(final int num, int page) {
        unsubscrible();
        subscription = RetrofitUtils.getGirlApi()
                .getGirl(num,page)
                .map(new Func1<GirlResult, List<GirlResult.Girl>>() {
                    @Override
                    public List<GirlResult.Girl> call(GirlResult girlResult) {
                        return girlResult.girls;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<GirlResult.Girl>>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: "+e.toString());
                        adapter.addAll(mSqlite.getAllGirls());
                    }
                    @Override
                    public void onNext(List<GirlResult.Girl> girls) {
                        mList = girls;
                        Log.d(TAG, "onNext: size = " + mList.size());
                        adapter.addAll(mList);

                    }
                });
    }

    @Override
    public void onLoadMore() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadGirl(20,page);
                page++;
            }
        }, 1000);
    }
}
