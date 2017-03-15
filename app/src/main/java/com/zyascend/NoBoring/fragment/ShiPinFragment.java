package com.zyascend.NoBoring.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.base.BaseFragment;

import butterknife.Bind;

/**
 *
 * Created by Administrator on 2016/11/23.
 */

public class ShiPinFragment extends BaseFragment {

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
                return ShiPingListFragment.getInstance(position);
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0:
                        return "美女";
                    case 1:
                        return "冷知识";
                    case 2:
                        return "游戏";
                }
                return "美女";
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
