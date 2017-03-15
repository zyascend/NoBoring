package com.zyascend.NoBoring.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.dao.ZhiHuResult;
import com.zyascend.NoBoring.utils.SPUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 * Created by Administrator on 2016/11/27.
 */

public class ZhiHuAdapter extends RecyclerArrayAdapter<ZhiHuResult.Story> {

    private  boolean isNopic;

    public ZhiHuAdapter(Context context) {
        super(context);
        isNopic = SPUtils.getBoolean(SPUtils.SP_KEY_NOPIC,false,context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 0 || isNopic) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zhihu, parent, false);
            return new StoryHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weixin, parent, false);
            return new TopStoryHolder(view);
        }
    }

    @Override
    public int getViewType(int position) {
        return getAllData().get(position).getType();
    }

    class StoryHolder extends BaseViewHolder<ZhiHuResult.Story> {
        @Bind(R.id.iv_preview)
        ImageView ivPreview;
        @Bind(R.id.tv_title)
        TextView tvTitle;

        public StoryHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void setData(ZhiHuResult.Story data) {
            super.setData(data);
            if (data == null)return;
            tvTitle.setText(data.getTitle());
            if (!SPUtils.getBoolean(SPUtils.SP_KEY_NOPIC,false,getContext())){
                ivPreview.setVisibility(View.VISIBLE);
                Glide.with(getContext())
                        .load(data.getImages().get(0))
                        .placeholder(R.drawable.ic_pic_holder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivPreview);
            }else {
                ivPreview.setVisibility(View.GONE);
            }
        }
    }

    class TopStoryHolder extends BaseViewHolder<ZhiHuResult.Story> {

        @Bind(R.id.iv_image)
        ImageView ivImage;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_des)
        TextView tvDes;
        @Bind(R.id.tv_ct)
        TextView tvCt;

        public TopStoryHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }

        @Override
        public void setData(ZhiHuResult.Story data) {
            super.setData(data);
            if (data == null) return;
            tvDes.setVisibility(View.GONE);
            tvCt.setVisibility(View.GONE);
            tvTitle.setText(data.getTitle());
            if (!SPUtils.getBoolean(SPUtils.SP_KEY_NOPIC,false,getContext())){
                Glide.with(getContext())
                        .load(data.getImages().get(0))
                        .placeholder(R.drawable.ic_pic_holder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivImage);
            }else {
                ivImage.setVisibility(View.GONE);
            }

        }
    }
}
