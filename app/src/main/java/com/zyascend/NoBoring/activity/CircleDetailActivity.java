package com.zyascend.NoBoring.activity;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.adapter.CommentAdapter;
import com.zyascend.NoBoring.base.BaseActivity;
import com.zyascend.NoBoring.base.BaseAdapter;
import com.zyascend.NoBoring.utils.AVObjectKeysInterface;
import com.zyascend.NoBoring.utils.CircleTransform;
import com.zyascend.NoBoring.utils.view.FloatScrollView;
import com.zyascend.NoBoring.utils.view.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 *
 * Created by Administrator on 2017/3/11.
 */

public class CircleDetailActivity extends BaseActivity implements AVObjectKeysInterface
        , SwipeRefreshLayout.OnRefreshListener
        , RecyclerArrayAdapter.OnLoadMoreListener {

    public static final String INTENT_CIRCLE_ID = "circle_id";
    private static final String TAG = "CircleDetailActivity";
    @Bind(R.id.iv_person)
    ImageView ivPerson;
    @Bind(R.id.tv_user)
    TextView tvUser;
    @Bind(R.id.tv_comment)
    TextView tvComment;
    @Bind(R.id.tv_likes)
    TextView tvLikes;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.iv_pic1)
    ImageView ivPic1;
    @Bind(R.id.iv_pic2)
    ImageView ivPic2;
    @Bind(R.id.iv_pic3)
    ImageView ivPic3;
    @Bind(R.id.tv_comment_num)
    TextView tvCommentNum;
    @Bind(R.id.tv_comment_hotest)
    TextView tvCommentHotest;
    @Bind(R.id.tv_comment_newest)
    TextView tvCommentNewest;
    @Bind(R.id.ll_commentTab)
    RelativeLayout llCommentTab;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.rela_add_comment)
    RelativeLayout addCommentLayout;
    @Bind(R.id.et_comment)
    EditText etComment;
    @Bind(R.id.iv_likes)
    ImageView ivLikes;
    @Bind(R.id.btn_sendComment)
    Button btnSendComment;
    @Bind(R.id.view_error)
    ViewStub viewError;
    @Bind(R.id.floatScrollView)
    FloatScrollView nestedScrollView;
    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.divider)
    TextView divider;
    @Bind(R.id.tv_cancel_reply)
    TextView tvCancelReply;
    @Bind(R.id.tv_send_reply)
    TextView tvSendReply;
    @Bind(R.id.et_reply)
    EditText etReply;
    @Bind(R.id.rl_reply)
    RelativeLayout replyLayout;

    private String circleId;
    private CommentAdapter adapter;
    private AVQuery<AVObject> query;
    private AVRelation<AVObject> relation;
    private int commentPage;
    private boolean isFirstLoad = false;
    private AVObject localCircle;
    private List<AVObject> mCommentList = new ArrayList<>();
    private int commentNum;
    private String commentPoster;


    @Override
    protected void initView() {

//        addCommentLayout.setVisibility(View.GONE);


        circleId = getIntent().getStringExtra(INTENT_CIRCLE_ID);
        if (!TextUtils.isEmpty(circleId)) {
            fetchData();
        }

        adapter = new CommentAdapter(this);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                replyComments(position);
            }
        });

        recyclerView.setLayoutManager(new FullyLinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        localCircle = AVObject.createWithoutData(CIRCLES, circleId);
        query = new AVQuery<>(COMMENT);
        query.whereEqualTo(TARGET_ARTICLE, localCircle);
        query.orderByDescending(CREATE_AT);
        query.setLimit(10);
    }

    private void replyComments(int position) {
        if (mCommentList == null || mCommentList.isEmpty()
                || position >= mCommentList.size()) {

            return;
        }


        AVObject avObject = AVObject.createWithoutData(COMMENT,mCommentList.get(position).getObjectId() );
        avObject.fetchInBackground(POSTER, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                AVUser user = avObject.getAVUser(POSTER);
                commentPoster = user.getUsername();
                etReply.setHint("回复：@"+commentPoster+":");
            }
        });

        replyLayout.setVisibility(View.VISIBLE);
        addCommentLayout.setVisibility(View.INVISIBLE);

    }

    private void fetchData() {
        AVQuery<AVObject> avQuery = new AVQuery<>(CIRCLES);
        avQuery.getInBackground(circleId, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    load(avObject);
                } else {
                    showError(e);
                }
            }
        });
    }

    private void load(AVObject data) {
        localCircle.fetchInBackground(POSTER, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                AVUser user = avObject.getAVUser(POSTER);
                if (user != null) {
                    tvUser.setText(user.getUsername());
                    AVFile head = user.getAVFile(HEAD_PIC);
                    if (head != null) {
                        String headUrl = head.getUrl();
                        if (!TextUtils.isEmpty(headUrl)) {
                            Glide.with(CircleDetailActivity.this)
                                    .load(headUrl)
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .transform(new CircleTransform(CircleDetailActivity.this))
                                    .into(ivPerson);
                        }
                    }
                }
            }
        });

        commentNum = (int) data.getNumber(COMMENT_NUM);
        tvComment.setText(String.valueOf(commentNum));
        tvCommentNum.setText(String.valueOf(commentNum));
        tvLikes.setText(String.valueOf(data.getNumber(LIKES_NUM)));
        tvTitle.setText(data.getString(TITLE));
        tvContent.setText(data.getString(CONTENT));

        loadPic(data);

        loadComments();


    }

    private void loadPic(AVObject data) {
        if (data.getAVFile(PIC_1) != null) {
            loadPicWithGlide(data.getAVFile(PIC_1).getUrl(), ivPic1);
        }
        if (data.getAVFile(PIC_2) != null) {
            loadPicWithGlide(data.getAVFile(PIC_2).getUrl(), ivPic2);
        }
        if (data.getAVFile(PIC_3) != null) {
            loadPicWithGlide(data.getAVFile(PIC_3).getUrl(), ivPic2);
        }
    }

    private void loadPicWithGlide(String url, ImageView ivPic) {
        Glide.with(this)
                .load(url)
                .asBitmap()
                .into(ivPic);
    }

    private void loadComments() {
        isFirstLoad = true;
        query.skip(0);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        });
    }

    private void showError(AVException e) {
        viewError.setVisibility(View.VISIBLE);
        showMsg(e.toString());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_circle_detail;
    }

    @Override
    public void onRefresh() {
        Toast.makeText(this, "刷新中...", Toast.LENGTH_SHORT).show();
        loadData();
    }

    private void loadData() {
//        avQuery.include("owner");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (isFirstLoad) {
                        adapter.clear();
                        mCommentList.clear();
                    }
                    adapter.addAll(list);
                    mCommentList.addAll(list);
                    Log.d(TAG, "done: commment size = " + list.size());
                    // TODO: 2017/3/11 list为空时怎么办。
                } else {
                    showError(e);
                }
            }
        });
        commentPage++;
    }

    @Override
    public void onLoadMore() {
        Log.d(TAG, "onLoadMore: 加载第" + (commentPage + 1) + "页");
        isFirstLoad = false;
        query.skip(commentPage * 10);
        loadData();
    }


    @OnClick({R.id.iv_likes, R.id.btn_sendComment, R.id.tv_comment_hotest
            , R.id.tv_comment_newest,R.id.tv_cancel_reply, R.id.tv_send_reply})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_likes:
                localCircle.increment(LIKES_NUM);
                localCircle.setFetchWhenSave(true);
                localCircle.saveInBackground();
                break;
            case R.id.btn_sendComment:
                addComments(null);
                break;
            case R.id.tv_comment_hotest:
                tvCommentHotest.setTextColor(Color.YELLOW);
                tvCommentNewest.setTextColor(Color.BLACK);
                query.orderByDescending(LIKES_NUM);
                loadComments();
                break;
            case R.id.tv_comment_newest:
                tvCommentHotest.setTextColor(Color.BLACK);
                tvCommentNewest.setTextColor(Color.YELLOW);
                query.orderByAscending(CREATE_AT);
                loadComments();
                break;
            case R.id.tv_cancel_reply:
                replyLayout.setVisibility(View.GONE);
                break;
            case R.id.tv_send_reply:
                String ed_content = etReply.getText().toString();
                if (TextUtils.isEmpty(ed_content)){
                    showMsg("内容不可为空...");
                    return;
                }
                if (TextUtils.isEmpty(commentPoster))return;
                String content = "回复：@" + commentPoster + ":" + ed_content;
                addComments(content);
                break;

        }
    }

    private void addComments(String msg) {
        String content = msg;
        if (content == null) {
            content = etComment.getText().toString();
        }
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "内容不可为空", Toast.LENGTH_SHORT).show();
        } else {

            final AVObject comment = new AVObject(COMMENT);
            comment.put(CONTENT, content);
            comment.put(LIKES_NUM, 0);
            comment.put(POSTER, AVUser.getCurrentUser());
            comment.put(TARGET_ARTICLE, localCircle);
            comment.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        showMsg("OK");
                        //评论数+1
                        localCircle.increment(COMMENT_NUM);
                        localCircle.saveInBackground();
                        //textView +1
                        tvCommentNum.setText(String.valueOf(commentNum + 1));
                        tvComment.setText(String.valueOf(commentNum + 1));
                        //显示回复板
                        addCommentLayout.setVisibility(View.VISIBLE);
                        replyLayout.setVisibility(View.GONE);
                        //清空editText文字
                        etReply.setText("");
                        etComment.setText("");
                        //重新加载
                        isFirstLoad = false;
                        query.skip(mCommentList.size());
                        query.setLimit(0);
                        onRefresh();
                    } else {
                        showMsg(e.toString());
                    }
                }
            });
        }
    }

    private void showMsg(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
