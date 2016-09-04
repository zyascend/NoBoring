package com.zyascend.NoBoring.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.activity.PhotoActivity;
import com.zyascend.NoBoring.base.BaseAdapter;
import com.zyascend.NoBoring.model.DongTaiJokeResult;

import com.zyascend.NoBoring.utils.LruCacheUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 * Created by zyascend on 2016/7/16.
 */
public class DongJokeAdapter extends BaseAdapter {

    private static final String TAG = "TAG_DongTaiAdapter";
    private List<DongTaiJokeResult.ShowapiResBodyBean.DongTaiJoke> mList;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dongjoke, parent, false);

        return new DongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final DongViewHolder viewHolder = (DongViewHolder) holder;
        final String title = mList.get(position).getTitle();
        viewHolder.tvTitle.setText(title);
        final String url = mList.get(position).getImg();

        if (url.isEmpty()){
            viewHolder.ivJokeImage.setImageResource(R.drawable.ic_error);
        }else {
            viewHolder.ivPlayGif.setVisibility(View.VISIBLE);
            Bitmap bitmap = LruCacheUtils.getInstance().getBitmapFromMemCache(url);
            if (bitmap != null){
                viewHolder.ivJokeImage.setImageBitmap(bitmap);
            }else {
                LruCacheUtils.displayImageTarget(viewHolder.ivJokeImage,url,LruCacheUtils.getTarget(viewHolder.ivJokeImage,url,position));
            }
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(),PhotoActivity.class);
                intent.putExtra(PhotoActivity.EXTRA_TITLE,title);
                intent.putExtra(PhotoActivity.EXTRA_URL,url);
                intent.putExtra(PhotoActivity.EXTRA_IS_GIF,true);
                v.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setList(List<DongTaiJokeResult.ShowapiResBodyBean.DongTaiJoke> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    class DongViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_jokeImage)
        ImageView ivJokeImage;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.iv_hide_gif)
        ImageView ivPlayGif;

        public DongViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
