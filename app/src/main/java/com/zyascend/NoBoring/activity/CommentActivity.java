package com.zyascend.NoBoring.activity;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.adapter.CommentAdapter;
import com.zyascend.NoBoring.base.BaseActivity;
import com.zyascend.NoBoring.bean.CommentResponse;
import com.zyascend.NoBoring.bean.CreateResponse;
import com.zyascend.NoBoring.bean.ListResponse;
import com.zyascend.NoBoring.bean.PostResponse;
import com.zyascend.NoBoring.http.LeanCloudService;
import com.zyascend.NoBoring.http.RequestHelper;
import com.zyascend.NoBoring.http.RetrofitService;
import com.zyascend.NoBoring.utils.SPUtils;
import com.zyascend.NoBoring.utils.rx.RxTransformer;
import com.zyascend.NoBoring.utils.view.DividerItemDecoration;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscriber;
import rx.functions.Action0;

/**
 * 功能：
 * 作者：zyascend on 2017/7/31 12:15
 * 邮箱：zyascend@qq.com
 */

public class CommentActivity extends BaseActivity {

    public static final String POST_ID = "postId";
    public static final String POSTER_ID = "posterId";

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.et_comment)
    EditText etComment;
    @Bind(R.id.btn_sendComment)
    Button btnSendComment;
    @Bind(R.id.coordinatorView)
    CoordinatorLayout coordinatorView;
    private String postId;

    private CommentAdapter adapter;
    private String TAG = "TAG_Comment";
    private String posterId;

    @Override
    protected void initView() {
//        postId = getIntent().getStringExtra(POST_ID);
        postId = "59754cbf128fe155ce7334d2";
        if (TextUtils.isEmpty(postId)){
            showEmpty();
            return;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        adapter = new CommentAdapter(this);
        recyclerView.setAdapter(adapter);

        fetchData();
    }

    private void fetchData() {

        LeanCloudService.getInstance().getAPI()
                .getComments(buildRequsetMap())
                .compose(RxTransformer.INSTANCE.<ListResponse<CommentResponse>>transform(lifeCycleSubject))
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoading();
                    }
                })
                .subscribe(new Subscriber<ListResponse<CommentResponse>>() {
                    @Override
                    public void onCompleted() {
                        showLoadingComplete();
                    }
                    @Override
                    public void onError(Throwable e) {
                        showError();
                        Log.d(TAG, "onError: "+e.getMessage());
                    }
                    @Override
                    public void onNext(ListResponse<CommentResponse> data) {
                        if (data == null || data.getResults().isEmpty()){
                            showEmpty();
                            return;
                        }
                        adapter.addAll(data.getResults());
                    }
                });

    }

    private Map<String,String> buildRequsetMap() {
        Map<String,String> requsetMap = new HashMap<>();
        requsetMap.put("order","-createdAt");//or -createdAt
        requsetMap.put("include","poster");
        String whereValue = "{\"postId\":\"" +
                postId +
                "\"}";
        requsetMap.put("where",whereValue);
        return requsetMap;
    }

    private void showError() {

    }

    private void showLoadingComplete() {

    }

    private void showLoading() {

    }

    private void showEmpty() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_comment;
    }


    @OnClick(R.id.btn_sendComment)
    public void onClick() {
        final String content = etComment.getText().toString();
        if (TextUtils.isEmpty(content)){
            Toast.makeText(this, "评论不可为空...", Toast.LENGTH_SHORT).show();
            return;
        }
//        posterId = SPUtils.getString(SPUtils.USER_ID,null);
        posterId = "59754d1bfe88c2c1d45b156d";

        if (TextUtils.isEmpty(posterId)){
            Toast.makeText(this, "未登录，请登录", Toast.LENGTH_SHORT).show();
            // TODO: 2017/8/16 跳转到登陆界面
            return;
        }
//        LeanCloudService.getInstance().getAPI()
//                .postComment(RequestHelper.getPostBody(content,postId,posterId))
//                .compose(RxTransformer.INSTANCE.<CreateResponse>transform(lifeCycleSubject))
//                .doOnSubscribe(new Action0() {
//                    @Override
//                    public void call() {
//                        showLoading();
//                    }
//                })
//                .subscribe(new Subscriber<CreateResponse>() {
//                    @Override
//                    public void onCompleted() {
//                        showLoadingComplete();
//                    }
//                    @Override
//                    public void onError(Throwable e) {
//                        showError();
//                        Log.d(TAG, "onError: "+e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(CreateResponse createResponse) {
//                        addNewComment(content,createResponse);
//                    }
//                });

        AVObject comment = new AVObject("Comment");// 构建 Comment 对象
        comment.put("content", content);
        comment.put("postId", postId);
        comment.put("poster", AVObject.createWithoutData("_User", posterId));
        comment.setFetchWhenSave(true);
        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null){
//                    addNewComment(content,"刚刚");
                    fetchData();
                }
            }
        });

    }

    private void addNewComment(String content,String createAt) {
        CommentResponse response = new CommentResponse();
        response.setContent(content);
        response.setCreatedAt(createAt);
        CommentResponse.PosterBean bean = new CommentResponse.PosterBean();
        bean.setAvatarUrl(SPUtils.getString(SPUtils.AVATAR_URL,null));
        bean.setUsername("Make It");
        bean.setObjectId(posterId);
        response.setPoster(bean);
        adapter.addOne(response);
    }
}
