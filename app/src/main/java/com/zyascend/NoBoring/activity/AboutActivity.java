package com.zyascend.NoBoring.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.base.BaseActivity;
import com.zyascend.NoBoring.base.BaseApplication;
import com.zyascend.NoBoring.utils.ActivityUtils;
import com.zyascend.NoBoring.utils.CacheCleanUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/8/20.
 */
public class AboutActivity extends BaseActivity {


    private static final String GITHUB_ADDRESS = "https://github.com/zyascend/NoBoring";
    private static final String WEIBO_ADDRESS = "http://weibo.com/zyascend";
    private static final String EMAIL_ADDRESS = "1334553391@qq.com";
    private static final String TAG = "TAG_AboutActivity";


    @Bind(R.id.activity_about_image)
    ImageView activityAboutImage;
    @Bind(R.id.tv_cache_size)
    TextView mCacheSize;
    @Bind(R.id.github_ad)
    TextView githubAd;
    @Bind(R.id.weibo)
    TextView weibo;
    @Bind(R.id.email)
    TextView email;
    @Bind(R.id.nestedscrollView)
    NestedScrollView nestedscrollView;

    CacheCleanUtils mUtils;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.layout_clear_cache)
    RelativeLayout layoutClearCache;
    @Bind(R.id.layout_advice)
    RelativeLayout layoutAdvice;

    @Override
    protected void initView() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        CoordinatorLayout.LayoutParams param = (CoordinatorLayout.LayoutParams) toolbar.getLayoutParams();
        param.setMargins(0 , getStatusBarHeight() , 0 , 0);
        toolbar.setLayoutParams(param);
        setToolbarTitle("设置");
        mUtils = CacheCleanUtils.getInstance();

        try {
            mCacheSize.setText(getSize());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        Log.d(TAG, "getStatusBarHeight: result = " + result);
        return result;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    private String getSize() throws Exception {
        return mUtils.getTotalCacheSize();
    }

    /**
     * 缓存清理点击事件
     */
    public void clearCache() throws Exception {
        mUtils.clearAllCache();
        ActivityUtils.showSnackBar(findViewById(R.id.coordinator), "缓存已清理");
        mCacheSize.setText(getSize());
    }

    /**
     * 反馈点击事件
     */
    public void advice() {
        sendEmail();
    }


    @OnClick({R.id.github_ad, R.id.weibo, R.id.email,R.id.layout_clear_cache, R.id.layout_advice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.github_ad:
                ActivityUtils.openUrl(this, GITHUB_ADDRESS);
                break;
            case R.id.weibo:
                ActivityUtils.openUrl(this, WEIBO_ADDRESS);
                break;
            case R.id.email:
                sendEmail();
                break;
            case R.id.layout_advice:
                sendEmail();
                break;
            case R.id.layout_clear_cache:
                try {
                    clearCache();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void sendEmail() {

        Intent email = new Intent(Intent.ACTION_SENDTO);

        if (ActivityUtils.isIntentSafe(email)) {

            email.setData(Uri.parse("mailto:" + EMAIL_ADDRESS));
            email.putExtra(Intent.EXTRA_SUBJECT, "NoBoring用户信息反馈");
            email.putExtra(Intent.EXTRA_TEXT, "你好，");
            startActivity(email);

        } else {

            ClipboardManager clipboardManager = (ClipboardManager) this.getSystemService(Activity.CLIPBOARD_SERVICE);
            clipboardManager.setPrimaryClip(ClipData.newPlainText(null, EMAIL_ADDRESS));
            ActivityUtils.showSnackBar(findViewById(R.id.coordinator), "地址已复制");

        }
    }


}
