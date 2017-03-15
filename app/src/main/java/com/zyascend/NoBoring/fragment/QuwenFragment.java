package com.zyascend.NoBoring.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 *
 * Created by Administrator on 2016/11/23.
 */

public class QuwenFragment extends BaseFragment {

    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected void lazyLoad() {
        
    }

    @Override
    protected void initViews() {
        viewpager.setOffscreenPageLimit(2);
        viewpager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return new FreshFragment();
                    case 1:
                        return new ZhihuFragment();
                    case 2:
                        return new WeiXinFragment();
                    default:
                        return new FreshFragment();
                }
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0:
                        return "新鲜事";
                    case 1:
                        return "知乎日报";
                    default:
                        return "微信精选";
                }
            }
        });
        tabLayout.setupWithViewPager(viewpager);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_viewpager;

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
}
