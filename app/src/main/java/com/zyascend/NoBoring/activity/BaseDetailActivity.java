package com.zyascend.NoBoring.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zyascend.NoBoring.http.API;
import com.zyascend.NoBoring.utils.Constants;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.base.BaseActivity;
import com.zyascend.NoBoring.dao.Fresh;
import com.zyascend.NoBoring.dao.FreshContent;
import com.zyascend.NoBoring.dao.WeiXin;
import com.zyascend.NoBoring.dao.ZhiHuContent;
import com.zyascend.NoBoring.dao.ZhiHuResult;
import com.zyascend.NoBoring.http.RetrofitService;
import com.zyascend.NoBoring.utils.rx.RxTransformer;
import com.zyascend.NoBoring.utils.SPUtils;
import com.zyascend.NoBoring.http.ZhihuService;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscriber;
import rx.functions.Action0;

/**
 *
 * Created by Administrator on 2016/11/27.
 */

public class BaseDetailActivity extends BaseActivity implements NestedScrollView.OnScrollChangeListener {

    public static final String INTENT_DETAIL_TYPE = "intent_type";
    public static final String INTENT_ENTITY = "intent_entity";

    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.collapsingLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.webView)
    WebView webView;
    @Bind(R.id.nestedscrollView)
    NestedScrollView nestedscrollView;

    private int mType;
    private ZhiHuContent mZhihuContent;
    private Fresh mFresh;
    private WeiXin mWeixin;
    private ProgressDialog mProgressDialog;

    @Override
    protected void initView() {
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("加载中");
        initWebView();
        showLoading();
        hideGoToTopFab();

        mType = getIntent().getIntExtra(INTENT_DETAIL_TYPE, 0);
        if (mType == 0) {
            loadFresh();
        } else if (mType == 1) {
            loadWeiXin();
        } else if (mType == 2) {
            loadZhihu();
        }
    }

    private void hideGoToTopFab() {
        nestedscrollView.setOnScrollChangeListener(this);
        nestedscrollView.post(new Runnable() {
            @Override
            public void run() {
                fab.hide();
            }
        });
    }

    private void showLoading() {
        mProgressDialog.show();
    }

    private void initWebView() {

        webView.setBackgroundColor(0); // 设置背景色
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        // 是否加载图片
        webView.getSettings().setBlockNetworkImage(SPUtils.getBoolean(SPUtils.SP_KEY_NOPIC, false, this));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                showLoadingComplete();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                showError();
            }
        });
    }

    private void showError() {
        mProgressDialog.dismiss();
//        ActivityUtils.showSnackBar(nestedscrollView,"啊哦，加载出错了...");
    }

    private void showLoadingComplete() {
        mProgressDialog.dismiss();
    }

    private void loadZhihu() {
        ZhiHuResult.Story story = getIntent().getParcelableExtra(INTENT_ENTITY);
        if (story == null || story.getId() == 0) {
            showError();
        } else {
            subscribleZhihu(story.getId());
        }
    }

    private void subscribleZhihu(int id) {
        API.ZhiHuApi api = ZhihuService.getInstance().getAPI();
        api.getDetails(id)

                .compose(RxTransformer.INSTANCE.<ZhiHuContent>transform(lifeCycleSubject))
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .subscribe(new Subscriber<ZhiHuContent>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showError();
                    }

                    @Override
                    public void onNext(ZhiHuContent zhiHuContent) {
                        doOnZhihuNext(zhiHuContent);
                    }
                });

    }

    private void doOnZhihuNext(ZhiHuContent zhiHuContent) {
        if (zhiHuContent == null) return;
        mZhihuContent = zhiHuContent;
        collapsingToolbarLayout.setTitle(mZhihuContent.title);
        if (TextUtils.isEmpty(mZhihuContent.body)) {
            webView.loadUrl(mZhihuContent.share_url);
            return;
        }
        String body = mZhihuContent.body.replace("<div class=\"headline\">", "").replace("<div class=\"img-place-holder\">", "");
        String htmlData = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />" + "<br/>" + body;
        webView.loadDataWithBaseURL("file:///android_asset/", htmlData, "text/html", "UTF-8", null);
    }

    private void loadWeiXin() {
        setToolbarTitle("详情");
        collapsingToolbarLayout.setTitleEnabled(false);
        mWeixin = getIntent().getParcelableExtra(INTENT_ENTITY);
        if (mWeixin == null || mWeixin.getUrl() == null) {
            showError();
        } else {
            webView.loadUrl(mWeixin.getUrl());
        }
    }

    private void loadFresh() {
        mFresh = getIntent().getParcelableExtra(INTENT_ENTITY);
        if (mFresh == null || mFresh.getId() == 0) {
            showError();
        } else {
            collapsingToolbarLayout.setTitle(mFresh.getTitle());
            subscribleFresh(String.valueOf(mFresh.getId()));
        }
    }

    private void subscribleFresh(String id) {
        API.FreshApi api = RetrofitService.init().createAPI(Constants.BASE_FRESH_URL, API.FreshApi.class);
        api.getFreshContent("get_post", id, "content")
                .compose(RxTransformer.INSTANCE.<FreshContent>transform(lifeCycleSubject))
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .subscribe(new Subscriber<FreshContent>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {
                        showError();
                    }

                    @Override
                    public void onNext(FreshContent freshContent) {
                        doOnFreshNext(freshContent);
                    }
                });
    }

    private void doOnFreshNext(FreshContent freshContent) {
        if (freshContent == null) return;
        String result;

        try {
            result = freshContent.getPost().getContent();
        } catch (Exception e) {
            showError();
            e.printStackTrace();
            return;
        }

        if (TextUtils.isEmpty(result)) {
            webView.loadUrl(mFresh.getContent());
            return;
        }


        String htmlData = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />" + "<br/>" + result;
        webView.loadDataWithBaseURL("",htmlData,"text/html", "UTF-8", null);

//        String noFirstImg = result.replaceFirst("<img.*/>", "");
//        String body = noFirstImg.replace("<div class=\"headline\">", "");
//        webView.loadDataWithBaseURL("file:///android_asset/", htmlData, "text/html", "UTF-8", null);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            share();
        }
        return super.onOptionsItemSelected(item);
    }

    private void share() {
        String shareUrl = null;
        String title = null;
        if (mType == 0) {
            shareUrl = mFresh.getContent();
            title = mFresh.getTitle();
        } else if (mType == 1) {
            shareUrl = mWeixin.getUrl();
            title = mWeixin.getTitle();
        } else if (mType == 2) {
            shareUrl = mZhihuContent.share_url;
            title = mZhihuContent.title;
        }

        String sb = title +
                "猛戳： " + shareUrl +
                "/n" +
                "   来自「不许无聊」APP的分享   ";
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, sb);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "分享到..."));
    }


    @OnClick(R.id.fab)
    public void onClick() {
        nestedscrollView.smoothScrollTo(0,0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView = null;
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if ((oldScrollY - scrollY) > ViewConfiguration.get(this).getScaledTouchSlop()) {
            fab.show();
        } else if ((scrollY - oldScrollY > ViewConfiguration.get(this).getScaledTouchSlop())) {
            fab.hide();
        }
    }
}
