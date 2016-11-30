package com.zyascend.NoBoring.base;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.squareup.leakcanary.RefWatcher;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.utils.LifeCycleEvent;

import butterknife.ButterKnife;
import rx.subjects.PublishSubject;

/**
 * Created by Administrator on 2016/7/13.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "TAG_BaseActivity";
    protected Toolbar mToolbar;
    private BaseApplication mApplication;
    private boolean mIsAddedView;
//    private View mNightView;
    public final PublishSubject<LifeCycleEvent> lifeCycleSubject = PublishSubject.create();
    private WindowManager.LayoutParams mNightViewParam;
    private WindowManager mWindowManager;
    public Bundle saveState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mApplication  = (BaseApplication) getApplication();

        if (mApplication.isNight()){
            setTheme(R.style.AppTheme_night);
        }else {
            setTheme(R.style.AppTheme_day);
        }
        lifeCycleSubject.onNext(LifeCycleEvent.CREATE);
        super.onCreate(savedInstanceState);
        saveState = savedInstanceState;
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        mIsAddedView = false;

//        if (mApplication.isNight()) {
//            initNightView();
//            mNightView.setBackgroundResource(R.color.night_mask);
//        }

        setToolBar();
        initView();
    }

    public void ChangeToDay() {
        mApplication.setIsNight(false);
//        mNightView.setBackgroundResource(android.R.color.transparent);
    }

    public void ChangeToNight() {
        mApplication.setIsNight(true);
        initNightView();
//        mNightView.setBackgroundResource(R.color.night_mask);
    }

    private void initNightView() {

        if (mIsAddedView)
            return;
        mNightViewParam = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);

        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        mNightView = new View(this);
//        mWindowManager.addView(mNightView, mNightViewParam);
        mIsAddedView = true;

    }

    private void setToolBar() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }else{

            Log.d(TAG, "setToolBar: toolbar is null");
        }
    }

    public void setToolbarTitle(String toolbarTitle) {
        ActionBar supportActionBar = this.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(toolbarTitle);
        }else {
            Log.d(TAG, "setToolbarTitle: bar_null");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void recreateOnResume() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                recreate();
            }
        }, 100);
    }

    @Override
    protected void onDestroy() {

        if (mIsAddedView) {
            mApplication = null;
//            mWindowManager.removeViewImmediate(mNightView);
            mWindowManager = null;
//            mNightView = null;
        }

        lifeCycleSubject.onNext(LifeCycleEvent.DESTROY);
        RefWatcher refWatcher = BaseApplication.getRefWatcher(this);
        refWatcher.watch(this);

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        lifeCycleSubject.onNext(LifeCycleEvent.STOP);
        super.onStop();
    }

    @Override
    protected void onPause() {
        lifeCycleSubject.onNext(LifeCycleEvent.PAUSE);
        super.onPause();
    }

    @Override
    protected void onResume() {
        lifeCycleSubject.onNext(LifeCycleEvent.RESUME);
        super.onResume();
    }


    protected abstract void initView();

    protected abstract int getLayoutId();

    public void loadFragment(){}
}
