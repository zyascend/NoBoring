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
import com.zyascend.NoBoring.base.BaseAdapter;
import com.zyascend.NoBoring.bean.ParcelableUser;
import com.zyascend.NoBoring.utils.glide.CircleTransform;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 功能：
 * 作者：zyascend on 2017/7/25 19:45
 * 邮箱：zyascend@qq.com
 */

public class ListDialogAdapter extends BaseAdapter {

    private final Context mContext;
    private final CircleTransform cf;


    private List<ParcelableUser> mList;

    public void addAll(List<ParcelableUser> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();

    }

    public ListDialogAdapter(Context context) {
        super();
        this.mContext = context;
        mList = new ArrayList<>();
        cf = new CircleTransform(context);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_follow, parent, false);
        return new ListDialogHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ListDialogHolder) holder).bind(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ListDialogHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_head)
        ImageView ivHead;
        @Bind(R.id.tv_userName)
        TextView tvUserName;

        public ListDialogHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(final ParcelableUser user) {
            if (user == null)return;

            Glide.with(mContext)
                    .load(user.avatarUrl)
                    .error(R.drawable.ic_pic_holder)
                    .placeholder(R.drawable.ic_pic_holder)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .transform(cf)
                    .into(ivHead);
            tvUserName.setText(user.userName);

            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, UserActivity.class);
                    intent.putExtra(UserActivity.USER_ID,user.userId);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
