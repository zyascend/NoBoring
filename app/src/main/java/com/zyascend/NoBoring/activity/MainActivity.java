package com.zyascend.NoBoring.activity;


import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.base.BaseActivity;
import com.zyascend.NoBoring.fragment.DuanziFragment;
import com.zyascend.NoBoring.fragment.GirlFragment;
import com.zyascend.NoBoring.fragment.JokeFragment;
import com.zyascend.NoBoring.fragment.QuwenFragment;
import com.zyascend.NoBoring.fragment.ShiPinFragment;
import com.zyascend.NoBoring.fragment.WeiXinFragment;
import com.zyascend.NoBoring.utils.ActivityUtils;
import com.zyascend.NoBoring.utils.CacheCleanUtils;
import com.zyascend.NoBoring.utils.rxbus.DateEvent;
import com.zyascend.NoBoring.utils.rxbus.NextEvent;
import com.zyascend.NoBoring.utils.rxbus.RxBus;
import com.zyascend.NoBoring.utils.rxbus.RxBusSubscriber;
import com.zyascend.NoBoring.utils.rxbus.RxSubscriptions;
import com.zyascend.NoBoring.utils.SPUtils;

import butterknife.Bind;
import rx.Subscription;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnTabSelectListener, OnTabReselectListener {
    private static final String TAG = "TAG_MainActivity";

    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.bottomBar)
    BottomBar bottomBar;

    private long mPressedTime = 0;
    private SwitchCompat mNoPicSwitch;
    private SwitchCompat mNightSwitch;
    private DuanziFragment duanziFragment;
    private GirlFragment girlFragment;
    private ShiPinFragment shipinFragment;
    private QuwenFragment quwenFragment;
    private boolean isShow = false;
    private TextView mCacheText;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        subscribleEvent();
        setDrawer();
        loadFragment();
    }


    private void setDrawer() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        Menu menu = navView.getMenu();
        MenuItem pic = menu.findItem(R.id.menu_noPic);
        mNoPicSwitch = (SwitchCompat) MenuItemCompat
                .getActionView(pic).findViewById(R.id.picSwitch);
        mNoPicSwitch.setChecked(SPUtils.getBoolean(SPUtils.SP_KEY_NOPIC, false, this));
        mNoPicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mNoPicSwitch.setChecked(isChecked);
                SPUtils.putBoolean(MainActivity.this, SPUtils.SP_KEY_NOPIC, isChecked);
                restartActivity();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });


        MenuItem night = menu.findItem(R.id.menu_night);
        mNightSwitch = (SwitchCompat) MenuItemCompat
                .getActionView(night).findViewById(R.id.nightSwitch);
        mNightSwitch.setChecked(SPUtils.getBoolean(SPUtils.SP_KEY_NIGHT, false, this));
        mNightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mNightSwitch.setChecked(isChecked);
                SPUtils.putBoolean(MainActivity.this, SPUtils.SP_KEY_NIGHT, isChecked);
                switchNightMode(isChecked);
                drawerLayout.closeDrawers();

            }
        });

        MenuItem cache = menu.findItem(R.id.menu_cache);
        mCacheText = (TextView) MenuItemCompat.getActionView(cache).findViewById(R.id.tv_cache);
        setCacheText();
        navView.setNavigationItemSelectedListener(this);
    }

    private void restartActivity() {
        this.finish();
        this.startActivity(new Intent(this, MainActivity.class));
    }

    private void switchNightMode(boolean isChecked) {
        if (isChecked) {
            //turn on night mode
            ChangeToNight();
            Toast.makeText(MainActivity.this, "Night Mode On!!!", Toast.LENGTH_SHORT).show();
        } else {
            //turn off night mode
            ChangeToDay();
            Toast.makeText(MainActivity.this, "Night Mode Off!!!", Toast.LENGTH_SHORT).show();
        }
//        recreateOnResume();
        this.finish();
        this.startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        long mNowTime = System.currentTimeMillis();
        if ((mNowTime - mPressedTime) > 2000) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            mPressedTime = mNowTime;
        } else {
            this.finish();
            System.exit(0);
        }
    }


    public void loadFragment() {
        setToolbarTitle("段子");
        if (saveState == null){
        }

        duanziFragment = new DuanziFragment();
        girlFragment = new GirlFragment();
        shipinFragment = new ShiPinFragment();
        quwenFragment = new QuwenFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContent,quwenFragment)
                .add(R.id.fragmentContent,duanziFragment)
                .add(R.id.fragmentContent, girlFragment)
                .add(R.id.fragmentContent,shipinFragment)
                .commit();

        bottomBar.setOnTabSelectListener(this);
        bottomBar.setOnTabReselectListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_change_theme:
                changeTheme();
                break;
            case R.id.menu_cache:
                CacheCleanUtils.getInstance().clearAllCache();
                setCacheText();
                break;
            case R.id.menu_about:
                Intent intent = new Intent(this,AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_exit:
                this.finish();
                System.exit(0);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setCacheText() {
        try {
            mCacheText.setText(CacheCleanUtils.getInstance().getTotalCacheSize());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeTheme() {

    }


    @Override
    public void onTabSelected(@IdRes int tabId) {
        if (isAnyFragmentNull())return;
        toggleNextMenuItem(false);
        switch (tabId){
            case R.id.tab_quwen:
                setToolbarTitle("趣闻");
                getSupportFragmentManager().beginTransaction()
                        .show(quwenFragment)
                        .hide(duanziFragment)
                        .hide(shipinFragment)
                        .hide(girlFragment)
                        .commit();
                break;
            case R.id.tab_duanzi:
                setToolbarTitle("段子");
                getSupportFragmentManager().beginTransaction()
                        .show(duanziFragment)
                        .hide(quwenFragment)
                        .hide(shipinFragment)
                        .hide(girlFragment)
                        .commit();
                break;
            case R.id.tab_shipin:
                setToolbarTitle("视频");
                getSupportFragmentManager().beginTransaction()
                        .show(shipinFragment)
                        .hide(quwenFragment)
                        .hide(duanziFragment)
                        .hide(girlFragment)
                        .commit();
                break;
            case R.id.tab_qutu:
                setToolbarTitle("美图");
                getSupportFragmentManager().beginTransaction()
                        .show(girlFragment)
                        .hide(quwenFragment)
                        .hide(shipinFragment)
                        .hide(duanziFragment)
                        .commit();
                break;
        }
    }

    private boolean isAnyFragmentNull() {
        return quwenFragment == null || duanziFragment == null
                || shipinFragment == null || girlFragment == null;
    }

    @Override
    public void onTabReSelected(@IdRes int tabId) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem last = menu.findItem(R.id.action_last);
        MenuItem today = menu.findItem(R.id.action_today);
        if(isShow){
            last.setVisible(true);
            today.setVisible(true);
        }else {
            last.setVisible(false);
            today.setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_last) {
            RxBus.getDefault().postSticky(new NextEvent(0));
        }else if (id == R.id.action_today){
            RxBus.getDefault().postSticky(new NextEvent(1));
        }
        return true;
    }


    private Subscription mRxSubSticky;

    private void toggleNextMenuItem(boolean show){
        boolean needInvalidate = show != this.isShow;
        this.isShow = show;
        if (needInvalidate)invalidateOptionsMenu();
    }

    private void subscribleEvent(){
        if (mRxSubSticky != null && !mRxSubSticky.isUnsubscribed()) {
            RxSubscriptions.remove(mRxSubSticky);
        } else {
            DateEvent event = RxBus.getDefault().getStickyEvent(DateEvent.class);
            Log.i(TAG, "获取到StickyEvent--->" + event);
            mRxSubSticky = RxBus.getDefault().toObservableSticky(DateEvent.class)
                    // 建议在Sticky时,在操作符内主动try,catch
                    .subscribe(new RxBusSubscriber<DateEvent>() {
                        @Override
                        protected void onEvent(DateEvent dateEvent) {
                            if (dateEvent == null)return;
                            setToolbarTitle(dateEvent.date);
                            toggleNextMenuItem(dateEvent.isShow);
                        }
                    });
        }
        RxSubscriptions.add(mRxSubSticky);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxSubscriptions.remove(mRxSubSticky);
        RxBus.getDefault().removeAllStickyEvents();
    }
}
