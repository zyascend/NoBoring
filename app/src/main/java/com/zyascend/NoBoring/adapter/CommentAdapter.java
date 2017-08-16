package com.zyascend.NoBoring.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.activity.UserActivity;
import com.zyascend.NoBoring.bean.CommentResponse;
import com.zyascend.NoBoring.bean.FollowResponse;
import com.zyascend.NoBoring.utils.LogUtils;
import com.zyascend.NoBoring.utils.glide.CircleTransform;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 功能：
 * 作者：zyascend on 2017/8/15 23:07
 * 邮箱：zyascend@qq.com
 */

public class CommentAdapter extends RecyclerView.Adapter {

    private final Context mContext;
    private CircleTransform circleTrans;

    public void addAll(List<CommentResponse> mList) {
        this.mList.addAll(mList);
        notifyDataSetChanged();
    }

    public void addOne(CommentResponse one) {
        //在第一行添加数据
        mList.add(0, one);
        notifyItemInserted(0);
    }

    private List<CommentResponse> mList;

    public CommentAdapter(Context context) {
        mList = new ArrayList<>();
        this.mContext = context;
        circleTrans = new CircleTransform(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comments, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommentHolder h = (CommentHolder) holder;
        h.bindData(mList.get(position));
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_person)
        ImageView ivPerson;
        @Bind(R.id.tv_user)
        TextView tvUser;
        @Bind(R.id.tv_updateAt)
        TextView tvUpdateAt;
        @Bind(R.id.tv_content)
        TextView tvContent;
        private String userId;

        public CommentHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindData(CommentResponse comment) {
            if (comment == null)return;
            CommentResponse.PosterBean poster = comment.getPoster();
            this.userId = poster.getObjectId();
            LogUtils.d("avatarUrl = "+ poster.getAvatarUrl());
            Glide.with(mContext)
                    .load(poster.getAvatarUrl())
                    .transform(circleTrans)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .error(R.mipmap.launcher)
                    .into(ivPerson);
            tvUser.setText(poster.getUsername());
            tvContent.setText(comment.getContent());
            tvUpdateAt.setText(comment.getUpdatedAt());
        }

        @OnClick({R.id.iv_person, R.id.tv_user})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_person:
                case R.id.tv_user:
                    Intent intent = new Intent(mContext, UserActivity.class);
                    intent.putExtra(UserActivity.USER_ID,userId);
                    mContext.startActivity(intent);
                    break;
            }
        }
    }

}
