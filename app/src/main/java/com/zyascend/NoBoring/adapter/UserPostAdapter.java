package com.zyascend.NoBoring.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.base.BaseAdapter;
import com.zyascend.NoBoring.bean.PostResponse;
import com.zyascend.NoBoring.utils.ActivityUtils;
import com.zyascend.NoBoring.utils.glide.CircleTransform;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 功能：
 * 作者：zyascend on 2017/7/24 19:44
 * 邮箱：zyascend@qq.com
 */

public class UserPostAdapter extends BaseAdapter {

    private static final int TYPE_GRID = 100;
    private static final int TYPE_LIST = 200;
    private final Context mContext;
    private final ArrayList<PostResponse> mList;
    private final CircleTransform cf;


    public void setGrid(boolean grid,boolean needRefreshData) {
        isGrid = grid;
        if (needRefreshData){
            notifyDataSetChanged();
        }
    }

    public void addData(List<PostResponse> data) {
        mList.clear();
        mList.addAll(data);
        notifyDataSetChanged();
    }

    private boolean isGrid;

    public UserPostAdapter(Context context) {
        this.isGrid = true;
        this.mContext = context;
        this.mList = new ArrayList<PostResponse>();
        cf = new CircleTransform(context);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_GRID) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_photo, parent, false);
            return new UserGridHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_post, parent, false);
            return new UserListAdapter(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isGrid) {
            ((UserGridHolder) holder).bind(mList.get(position));
        } else {
            ((UserListAdapter) holder).bind(mList.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return isGrid ? TYPE_GRID : TYPE_LIST;
    }



    class UserGridHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_photo)
        ImageView ivPhoto;

        public UserGridHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(PostResponse data) {

        }
    }

    class UserListAdapter extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_avatar)
        ImageView ivAvatar;
        @Bind(R.id.tv_userName)
        TextView tvUserName;
        @Bind(R.id.tv_date)
        TextView tvDate;
        @Bind(R.id.iv_pic)
        ImageView ivPic;
        @Bind(R.id.tv_likeCount)
        TextView tsLikesCounter;
        @Bind(R.id.tv_commentCount)
        TextView tsCommentCounter;
        @Bind(R.id.tv_content)
        TextView tvContent;
        @Bind(R.id.btn_like)
        ImageButton btnLike;
        @Bind(R.id.btn_comments)
        ImageButton btnComments;

        public UserListAdapter(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(PostResponse data) {
            if (data == null)return;
            tvContent.setText(data.getContent());
            tvUserName.setText(data.getPoster().getUsername());
            try {
                DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DDTHH:MM:SS.MMMZ", Locale.CHINA);
                Date date = dateFormat.parse(data.getUpdatedAt());
                tvDate.setText(ActivityUtils.formatDate(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Glide.with(mContext)
                    .load(data.getPoster().getAvatarUrl())
                    .error(R.drawable.ic_pic_holder)
                    .placeholder(R.drawable.ic_pic_holder)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .transform(cf)
                    .into(ivAvatar);

            Glide.with(mContext)
                    .load(data.getPicture().getUrl())
                    .error(R.drawable.ic_pic_holder)
                    .placeholder(R.drawable.ic_pic_holder)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(ivPic);

            tsLikesCounter.setText(data.getLikesCount());
            tsCommentCounter.setText(data.getLikesCount());

        }

        @OnClick({R.id.iv_avatar, R.id.iv_pic, R.id.btn_like, R.id.btn_comments})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_avatar:

                    break;
                case R.id.iv_pic:

                    break;
                case R.id.btn_like:

                    break;
                case R.id.btn_comments:

                    break;
            }
        }

    }
}
