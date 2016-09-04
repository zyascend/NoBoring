package com.zyascend.NoBoring.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.base.BaseFragment;
import com.zyascend.NoBoring.model.FreshContent;
import com.zyascend.NoBoring.model.Item;
import com.zyascend.NoBoring.utils.ActivityUtils;
import com.zyascend.NoBoring.utils.RetrofitUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/16.
 */
public class FreshDetailFragment extends BaseFragment implements View.OnClickListener
,NestedScrollView.OnScrollChangeListener{

    private static final String FRESH_BUNDLE = "fresh_bundle";
    private static final String TAG = "TAG_FreshDetail";
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.author)
    TextView author;
    @Bind(R.id.tag)
    TextView tag;
    @Bind(R.id.webView)
    WebView webView;
    @Bind(R.id.nestedscrollView)
    NestedScrollView nestedscrollView;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.titleImage)
    ImageView titleImage;

    private Item mItem;
    private int mFreshId;
    private String mContent;

    public static FreshDetailFragment getInstance(Item item) {
        FreshDetailFragment fragment = new FreshDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(FRESH_BUNDLE, item);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItem = getArguments().getParcelable(FRESH_BUNDLE);
    }

    @Override
    protected void initViews() {

//        loadPic();

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
        nestedscrollView.setOnScrollChangeListener(this);
        hideFab();

        fab.setOnClickListener(this);

        if (mItem != null) {
            title.setText(mItem.getTitle());
            author.setText(mItem.getAuthor());
            tag.setText(mItem.getTag());
            mFreshId = mItem.getId();
        }

        loadContent(mFreshId);

    }

    private void loadPic() {

        Glide.with(getActivity())
                .load(mItem.getUrl())
                .asBitmap()
                .placeholder(R.drawable.splash_back)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(titleImage);

    }

    private void hideFab() {
        nestedscrollView.post(new Runnable() {
            @Override
            public void run() {
                fab.hide();
            }
        });
    }

    private void loadContent(int mFreshId) {
        /**
         * Loading布局开始加载
         */
        unsubscrible();
        subscription = RetrofitUtils.getContentApi()
                .getFreshContent("get_post",String.valueOf(mFreshId),"content")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FreshContent>() {
                    @Override
                    public void onCompleted() {
                        loadContentToWebView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        /**
                         * 显示加载出错的布局
                         */
                        showEorror();
                    }

                    @Override
                    public void onNext(FreshContent freshContent) {
                        if (freshContent == null){
                            Log.d(TAG, "onNext: fresh nuuuuuu");
                        }else {
                            if (freshContent.getPost() == null){
                                Log.d(TAG, "onNext: post nulll");
                            }else {
                                if (freshContent.getPost().getContent() == null){
                                    Log.d(TAG, "onNext: content null");
                                }else {
                                    mContent = freshContent.getPost().getContent();
                                }
                            }

                        }
//
//
                    }
                });
    }

    private void loadContentToWebView() {

        if (mContent != null) {
            String body = mContent + "<style>img{max-width:100%; height:auto;}</style>";
            Log.d(TAG, "body =  "  + body );

            webView.loadDataWithBaseURL("", body, "text/html", "utf-8",null);

            /**
             * Loading布局停止加载
             */

        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fresh_detail;
    }

    @Override
    protected void showEorror() {
        webView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.GONE);
        ActivityUtils.showSnackBar(nestedscrollView,getString(R.string.laodingError));
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
    public void onClick(View v) {
        nestedscrollView.smoothScrollTo(0, 0);

    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if ((oldScrollY - scrollY) > ViewConfiguration.get(getActivity()).getScaledTouchSlop()) {
            fab.show();
        } else if ((scrollY - oldScrollY > ViewConfiguration.get(getActivity()).getScaledTouchSlop())) {
            fab.hide();
        }

        if ((oldScrollY - scrollY) > 100 ){
            getActivity().setTitle(mItem.getTitle());
        }
    }
}
