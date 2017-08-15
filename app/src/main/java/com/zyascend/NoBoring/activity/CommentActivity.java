package com.zyascend.NoBoring.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.adapter.CommentAdapter;
import com.zyascend.NoBoring.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    protected void initView() {
        postId = getIntent().getStringExtra(POST_ID);
        if (TextUtils.isEmpty(postId)){
            showEmpty();
            return;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentAdapter();
        recyclerView.setAdapter(adapter);

    }

    private void showEmpty() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_comment;
    }


    @OnClick(R.id.btn_sendComment)
    public void onClick() {

    }
}
