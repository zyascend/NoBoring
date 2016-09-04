package com.zyascend.NoBoring.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.base.BaseActivity;
import com.zyascend.NoBoring.fragment.FreshDetailFragment;
import com.zyascend.NoBoring.fragment.WeiXinDetailFragment;
import com.zyascend.NoBoring.model.Item;
import com.zyascend.NoBoring.utils.ActivityUtils;

/**
 *
 * Created by Administrator on 2016/8/16.
 */
public class DetailActivity extends BaseActivity {

    public static final String DETAIL_TAG = "detail_tag";
    public static final String DETAIL_ITEM = "detail_item";
    public static final String TAG_FRESH = "tag_fresh";
    public static final String TAG_WEIXIN = "tag_weixin";
    private static final String TAG = "TAG_DetailActivity";

    private String mTitle ;
    private String mUrl ;
    private String mTag;
    private Item mItem;


    @Override
    protected void initView() {

        mTag = getIntent().getStringExtra(DETAIL_TAG);
        mItem = getIntent().getParcelableExtra(DETAIL_ITEM);

        mTitle = mItem.getTitle();
        mUrl = mItem.getUrl();
        setToolbarTitle(mTitle);

        if (mTag != null && mTag.equals(TAG_WEIXIN)){
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),WeiXinDetailFragment.getInstance(mItem),R.id.fragmentContent);
        }else{
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),FreshDetailFragment.getInstance(mItem),R.id.fragmentContent);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_share:
                if (mTag != null && mTag.equals(TAG_WEIXIN)){
                    ActivityUtils.share(this,mTitle,mUrl);
                }else {
                    ActivityUtils.share(this,mTitle,mItem.getContent());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
