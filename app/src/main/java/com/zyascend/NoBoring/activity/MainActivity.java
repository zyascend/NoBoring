package com.zyascend.NoBoring.activity;


import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.base.BaseActivity;
import com.zyascend.NoBoring.fragment.CommunityFragment;
import com.zyascend.NoBoring.fragment.DuanziFragment;
import com.zyascend.NoBoring.fragment.GirlFragment;
import com.zyascend.NoBoring.fragment.QuwenFragment;
import com.zyascend.NoBoring.fragment.ShiPinFragment;

import com.zyascend.NoBoring.utils.ActivityUtils;
import com.zyascend.NoBoring.utils.CacheCleanUtils;
import com.zyascend.NoBoring.utils.LogUtils;
import com.zyascend.NoBoring.utils.glide.CircleTransform;
import com.zyascend.NoBoring.utils.rxbus.DateEvent;
import com.zyascend.NoBoring.utils.rxbus.NextEvent;
import com.zyascend.NoBoring.utils.rxbus.RxBus;
import com.zyascend.NoBoring.utils.rxbus.RxBusSubscriber;
import com.zyascend.NoBoring.utils.rxbus.RxSubscriptions;
import com.zyascend.NoBoring.utils.SPUtils;

import butterknife.Bind;
import rx.Subscription;

import static com.zyascend.NoBoring.utils.SPUtils.USER_NAME;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnTabSelectListener, OnTabReselectListener, View.OnClickListener {

    private static final String TAG = "TAG_MainActivity";
    private static final String FRAG_TAG_QUWEN = "tag_quwen";
    private static final String FRAG_TAG_DUANZI = "tag_duanzi";
    private static final String FRAG_TAG_GIRL = "tag_girl";
    private static final String FRAG_TAG_SHIPING = "tag_shiping";
    private static final String FRAG_TAG_COMMUNITY = "tag_commu";

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
    private CommunityFragment communityFragment;
    private boolean isShow = false;
    private TextView mCacheText;
    private TextView userName;
    private ImageView userHeadPic;
    private boolean logined;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        subscribeEvent();
        setDrawer();
        loadFragment();
//        checkIfUpdate();
    }


    private void setDrawer() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        setupUserInfo();

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

    private void setupUserInfo() {

        //获取左侧headView,设置User信息
        View headerView = navView.getHeaderView(0);
        userName = (TextView) headerView.findViewById(R.id.tv_userName);
        userHeadPic = (ImageView) headerView.findViewById(R.id.ivProfilePic);
        userHeadPic.setOnClickListener(this);
        userName.setOnClickListener(this);

        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser == null){
            logined = false;
            return;
        }
        logined = true;//已登录
        currentUser.fetchIfNeededInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (avObject != null){
                    userName.setText(avObject.getString("username"));
                    Glide.with(MainActivity.this)
                            .load(avObject.getString("avatarUrl"))
                            .error(R.mipmap.launcher)
                            .transform(new CircleTransform(MainActivity.this))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(userHeadPic);
                    saveInfo(avObject);
                }else if (e != null){
                    LogUtils.e(e.getMessage());
                }
            }
        });
    }

    private void saveInfo(AVObject avObject) {
        String session = avObject.getString("sessionToken");
        LogUtils.d("session = "+session);
        SPUtils.putString(session,SPUtils.SESSION_TOKEN);
        SPUtils.putString(avObject.getString("username"),SPUtils.SESSION_TOKEN);
    }

    private void restartActivity() {
        this.finish();
        this.startActivity(new Intent(this, MainActivity.class));
    }

    private void switchNightMode(boolean isChecked) {
        if (isChecked) {
            ChangeToNight();
        } else {
            ChangeToDay();
        }
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
        if (saveState == null) {
            duanziFragment = new DuanziFragment();
            girlFragment = new GirlFragment();
            shipinFragment = new ShiPinFragment();
            quwenFragment = new QuwenFragment();
            communityFragment = new CommunityFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContent, quwenFragment,FRAG_TAG_QUWEN)
                    .add(R.id.fragmentContent, duanziFragment,FRAG_TAG_DUANZI)
                    .add(R.id.fragmentContent, girlFragment,FRAG_TAG_GIRL)
                    .add(R.id.fragmentContent, shipinFragment,FRAG_TAG_SHIPING)
                    .add(R.id.fragmentContent, communityFragment,FRAG_TAG_COMMUNITY)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }else {
            FragmentManager manager = getSupportFragmentManager();
            duanziFragment = (DuanziFragment) manager.findFragmentByTag(FRAG_TAG_DUANZI);
            girlFragment = (GirlFragment) manager.findFragmentByTag(FRAG_TAG_GIRL);
            shipinFragment = (ShiPinFragment) manager.findFragmentByTag(FRAG_TAG_SHIPING);
            quwenFragment = (QuwenFragment) manager.findFragmentByTag(FRAG_TAG_QUWEN);
            communityFragment = (CommunityFragment) manager.findFragmentByTag(FRAG_TAG_COMMUNITY);
        }
        bottomBar.setOnTabSelectListener(this);
        bottomBar.setOnTabReselectListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_cache:
                CacheCleanUtils.getInstance().clearAllCache();
                setCacheText();
                break;
            case R.id.menu_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_exit:
                //退出登录
                SPUtils.putString(null,SPUtils.SESSION_TOKEN);
                AVUser.logOut();
                Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                intent1.putExtra(LoginActivity.FROM_EXIT_LOGIN,true);
                startActivity(intent1);
                MainActivity.this.finish();
                break;
            case R.id.menu_download:
                Intent intent2 = new Intent(this, DownLoadActivity.class);
                startActivity(intent2);
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



    @Override
    public void onTabSelected(@IdRes int tabId) {
        if (isAnyFragmentNull()) return;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        toggleNextMenuItem(false);
        switch (tabId) {
            case R.id.tab_quwen:
                setToolbarTitle("趣闻");
                transaction.show(quwenFragment)
                        .hide(duanziFragment)
                        .hide(shipinFragment)
                        .hide(girlFragment)
                        .hide(communityFragment)
                        .commit();
                break;
            case R.id.tab_duanzi:
                setToolbarTitle("段子");
                transaction.show(duanziFragment)
                        .hide(quwenFragment)
                        .hide(shipinFragment)
                        .hide(girlFragment)
                        .hide(communityFragment)
                        .commit();
                break;
            case R.id.tab_shipin:
                setToolbarTitle("视频");
                transaction.show(shipinFragment)
                        .hide(quwenFragment)
                        .hide(duanziFragment)
                        .hide(girlFragment)
                        .hide(communityFragment)
                        .commit();
                break;
            case R.id.tab_qutu:
                setToolbarTitle("美图");
                transaction.show(girlFragment)
                        .hide(quwenFragment)
                        .hide(shipinFragment)
                        .hide(duanziFragment)
                        .hide(communityFragment)
                        .commit();
                break;
            case R.id.tab_community:
                setToolbarTitle("发现");
                transaction.show(communityFragment)
                        .hide(quwenFragment)
                        .hide(shipinFragment)
                        .hide(duanziFragment)
                        .hide(girlFragment)
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
        if (isShow) {
            last.setVisible(true);
            today.setVisible(true);
        } else {
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
        } else if (id == R.id.action_today) {
            RxBus.getDefault().postSticky(new NextEvent(1));
        }
        return true;
    }


    private Subscription mRxSubSticky;

    private void toggleNextMenuItem(boolean show) {
        boolean needInvalidate = show != this.isShow;
        this.isShow = show;
        if (needInvalidate) invalidateOptionsMenu();
    }

    private void subscribeEvent() {
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
                            if (dateEvent == null) return;
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



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivProfilePic:
            case R.id.tv_userName:
                if (!logined){
                    //跳转到登录界面
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                }else {
                    //跳转到用户界面
                    Intent intent2 = new Intent(MainActivity.this,UserActivity.class);
                    intent2.putExtra(USER_NAME,userName.getText());
                    startActivity(intent2);
                    MainActivity.this.finish();
                }
                break;
        }
    }
}
