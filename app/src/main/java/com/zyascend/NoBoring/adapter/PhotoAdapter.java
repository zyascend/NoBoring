package com.zyascend.NoBoring.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.base.BaseAdapter;
import com.zyascend.NoBoring.dao.Photo;
import com.zyascend.NoBoring.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 功能：
 * 作者：zyascend on 2017/7/22 11:07
 * 邮箱：zyascend@qq.com
 */

public class PhotoAdapter extends BaseAdapter {


    private final Context mContext;

    private List<Photo> mList;

    public void addAll(List<Photo> list) {
        LogUtils.d("add all "+list.size());
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public PhotoAdapter(Context context) {
        super();
        this.mContext = context;
        mList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_photo, parent, false);
        return new PhotoHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((PhotoHolder) holder).bind(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class PhotoHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_photo)
        ImageView ivPhoto;
        public PhotoHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }

        public void bind(Photo photo) {
            LogUtils.d("bind:path = "+photo.getPath());
            //ivPhoto.getLayoutParams().height = ivPhoto.getWidth();
            String path = "file://" + photo.getPath();
            Glide.with(mContext)
                    .load(photo.getPath())
                    .placeholder(R.drawable.ic_pic_holder)
                    .error(R.drawable.ic_pic_holder)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(ivPhoto);
        }

        @OnClick(R.id.iv_photo)
        public void onClick() {
            if (mOnItemClickListener != null){
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }
}
