package com.zyascend.NoBoring.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.dao.BudejieVideo;
import com.zyascend.NoBoring.utils.DownloadUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Administrator on 2016/11/26.
 */

public class VideoAdapter extends RecyclerArrayAdapter<BudejieVideo> {


    public VideoAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);

        return new VideoHolder(view);
    }



    class VideoHolder extends BaseViewHolder<BudejieVideo> {
        @Bind(R.id.player)
        JCVideoPlayerStandard player;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.btn_download)
        ImageButton btnDownload;
        private String url;
        private BudejieVideo data;

        public VideoHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


        @Override
        public void setData(BudejieVideo data) {
            super.setData(data);

            if (data == null) {
                return;
            }
            this.data = data;
            player.setUp(url, JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
            tvTitle.setText(data.getTitle());
            Glide.with(getContext())
                    .load(data.getThumbUrl())
                    .placeholder(R.drawable.ic_pic_holder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(player.thumbImageView);
        }

        @OnClick(R.id.btn_download)
        public void onClick() {
//            DownloadUtils.startDownload(getContext(),data);
        }

    }
}
