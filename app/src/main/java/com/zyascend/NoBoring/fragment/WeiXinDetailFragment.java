package com.zyascend.NoBoring.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.base.BaseFragment;
import com.zyascend.NoBoring.model.Item;
import com.zyascend.NoBoring.utils.ActivityUtils;

import butterknife.Bind;


/**
 * Created by zyascend on 2016/8/16.
 *
 */
public class WeiXinDetailFragment extends BaseFragment implements NestedScrollView.OnScrollChangeListener, View.OnClickListener {

    private static final String WEIXIN_BUNDLE = "weixin_bundle";
    private static final String TAG = "TAG_WeixinFragment";

    @Bind(R.id.web_view)
    WebView webView;
    @Bind(R.id.nested_scrollview)
    NestedScrollView nestedScrollview;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;



    private Item mItem;

    public static WeiXinDetailFragment getInstance(Item item) {
        WeiXinDetailFragment fragment = new WeiXinDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(WEIXIN_BUNDLE, item);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItem = getArguments().getParcelable(WEIXIN_BUNDLE);
    }

    @Override
    protected void initViews() {

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setBlockNetworkImage(false);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                showLoadingComplete();
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                showEorror();
            }
        });
        nestedScrollview.setOnScrollChangeListener(this);
        fab.setOnClickListener(this);
        hideFab();
        if (mItem != null) {
            Log.d(TAG, " url = " + mItem.getUrl());
            webView.loadUrl(mItem.getUrl());
            /**
             * 设置Loading布局
             */
        }
    }

    private void hideFab() {
        nestedScrollview.post(new Runnable() {
            @Override
            public void run() {
                fab.hide();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_weixin_detail;
    }

    @Override
    protected void showEorror() {
        webView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.GONE);
        ActivityUtils.showSnackBar(nestedScrollview,getString(R.string.laodingError));
    }

    @Override
    protected void showLoading() {
        webView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void showLoadingComplete() {
        progressBar.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);

    }


    @Override
    public void onStop() {
        super.onStop();
        webView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }


    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

        if ((oldScrollY - scrollY) > ViewConfiguration.get(getActivity()).getScaledTouchSlop()) {
            fab.show();
        } else if ((scrollY - oldScrollY > ViewConfiguration.get(getActivity()).getScaledTouchSlop())) {
            fab.hide();
        }

    }

    @Override
    public void onClick(View v) {
        nestedScrollview.smoothScrollTo(0, 0);
    }


}
