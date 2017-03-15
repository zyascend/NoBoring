package com.zyascend.NoBoring.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.base.BaseFragment;

import butterknife.Bind;

/**
 * `
 * Created by Administrator on 2016/11/23.
 */

public class QutuFragment extends BaseFragment {

    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void initViews() {
        viewpager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return new GirlFragment();

                }
                return new GirlFragment();
            }

            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0:
                        return "校园招聘";
                    case 1:
                        return "实习招聘";
                    default:
                        return "校园招聘";
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
