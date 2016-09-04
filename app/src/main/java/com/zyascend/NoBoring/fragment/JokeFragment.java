package com.zyascend.NoBoring.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 * Created by Administrator on 2016/7/15.
 */
public class JokeFragment extends BaseFragment {

    private static final String TAG = "TAG_JokeFragment";
    @Bind(android.R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_joke;
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
    protected void initViews() {
        Log.d(TAG, "initView: ffffff");
        /**
         * 知识点：
         * 在Fragment中使用ViewPager用
         * FragmentStatePagerAdapter + getChildFragmentManager()
         */
        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new TextJokeFragment();
                    case 1:
                        return new DongJokeFragment();
                    default:
                        return new TextJokeFragment();
                }
            }

            @Override
            public int getCount() {
                Log.d(TAG, "getCount: ");
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {

                Log.d(TAG, "getPageTitle: ");
                switch (position){
                    case 0:
                        return "段子";
                    case 1:
                        return "趣图";
                    default:
                        return "段子";

                }
            }
        });
        tabs.setupWithViewPager(viewPager);
        tabs.setTabMode(TabLayout.MODE_FIXED);

        Log.d(TAG, "initViews: ");
    }

//    private void loadData() {
//        unsubscrible();
//

//        String maxRes = String.valueOf(10);
//        String pages = String.valueOf(1);
//
//        subscription = RetrofitUtils.getDongTaiJokeApi()
//                .getDongTaiJoke(maxRes, pages, Constants.APP_ID, Constants.APP_SIGN)
//                .map(MapDongTaiJoke.getInstance())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<DongTaiJokeResult.ShowapiResBodyBean.DongTaiJoke>>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.d(TAG, "onCompleted: ");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.d(TAG, "onError: ");
//                    }
//
//                    @Override
//                    public void onNext(List<DongTaiJokeResult.ShowapiResBodyBean.DongTaiJoke> dongTaiJokes) {
//                        Log.d(TAG, "onNext: size = " + dongTaiJokes.size());
//                    }
//                });
//    }



}
