package com.zyascend.NoBoring.activity;


import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.zyascend.NoBoring.fragment.FreshFragment;
import com.zyascend.NoBoring.fragment.GirlFragment;
import com.zyascend.NoBoring.fragment.WeiXinFragment;
import com.zyascend.NoBoring.utils.ActivityUtils;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.base.BaseActivity;
import com.zyascend.NoBoring.fragment.JokeFragment;
import com.zyascend.NoBoring.utils.SPUtils;

import butterknife.Bind;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "TAG_MainActivity";

    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private long mPressedTime = 0;
    private SwitchCompat mNoPicSwitch;
    private SwitchCompat mNightSwitch;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

        setDrawer();

       loadFragment();

    }


    private void setDrawer() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();




        Menu menu = navView.getMenu();
        MenuItem pic= menu.findItem(R.id.menu_noPic);
        mNoPicSwitch = (SwitchCompat)MenuItemCompat
                    .getActionView(pic).findViewById(R.id.picSwitch);
        mNoPicSwitch.setChecked(SPUtils.getBoolean(SPUtils.SP_KEY_NOPIC,false,this));
        mNoPicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mNoPicSwitch.setChecked(isChecked);
                SPUtils.putBoolean(MainActivity.this,SPUtils.SP_KEY_NOPIC,isChecked);
                loadFragment();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });




        MenuItem night = menu.findItem(R.id.menu_night);
        mNightSwitch = (SwitchCompat) MenuItemCompat
                .getActionView(night).findViewById(R.id.nightSwitch);
        mNightSwitch.setChecked(SPUtils.getBoolean(SPUtils.SP_KEY_NIGHT,false,this));
        mNightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mNightSwitch.setChecked(isChecked);
                SPUtils.putBoolean(MainActivity.this,SPUtils.SP_KEY_NIGHT,isChecked);
                switchNightMode(isChecked);
                drawerLayout.closeDrawers();

            }
        });


        navView.setNavigationItemSelectedListener(this);
    }

    private void switchNightMode(boolean isChecked) {
        if (isChecked){
            //turn on night mode
            ChangeToNight();
            Toast.makeText(MainActivity.this, "Night Mode On!!!", Toast.LENGTH_SHORT).show();
        }else {
            //turn off night mode
            ChangeToDay();
            Toast.makeText(MainActivity.this, "Night Mode Off!!!", Toast.LENGTH_SHORT).show();
        }
        recreate();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        long mNowTime = System.currentTimeMillis();
        if((mNowTime - mPressedTime) > 2000){
            ActivityUtils.showSnackBar(findViewById(R.id.coordinator),"再按一下退出");
            mPressedTime = mNowTime;
        }else{
            this.finish();
            System.exit(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            ActivityUtils.jumpTo(this,AboutActivity.class);
        }

        return true;
    }


    public void loadFragment() {
        setToolbarTitle("新鲜事");
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), new FreshFragment(),R.id.fragmentContent);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_news:
                loadFragment();
                break;

            case R.id.menu_joke:
                setToolbarTitle("笑话");
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),new JokeFragment(),R.id.fragmentContent);
                break;

            case R.id.menu_girl:
                setToolbarTitle("妹纸");
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),new GirlFragment(),R.id.fragmentContent);
                break;

            case R.id.menu_wechat:
                setToolbarTitle("微信精选");
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),new WeiXinFragment(),R.id.fragmentContent);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }





}
