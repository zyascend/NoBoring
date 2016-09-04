package com.zyascend.NoBoring.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.base.BaseAdapter;
import com.zyascend.NoBoring.model.WeiXinResult;
import com.zyascend.NoBoring.utils.LruCacheUtils;
import com.zyascend.NoBoring.utils.SPUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 * Created by zyascend on 2016/7/17.
 */
public class WeiXinAdapter extends BaseAdapter {


    private static final String TAG = "TAG_WeixinAdapter";
    private Context mContext;
    private List<WeiXinResult.WeiXin> mList;
    public WeiXinAdapter(Context context){
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weixin, parent, false);

        return new WeiXinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        WeiXinViewHolder viewHolder = (WeiXinViewHolder) holder;
        WeiXinResult.WeiXin weiXin = mList.get(position);

        String title = weiXin.getTitle();
        String des = weiXin.getDescription();
        String ct = weiXin.getCtime();
        String imageUrl = weiXin.getPicUrl();
        viewHolder.tvTitle.setText(title);
        viewHolder.tvDes.setText(des);
        viewHolder.tvCt.setText(ct);

        if (! SPUtils.getBoolean(SPUtils.SP_KEY_NOPIC,false,mContext)) {

            if (TextUtils.isEmpty(imageUrl)) {
                viewHolder.imageView.setImageResource(R.drawable.ic_error);
            }else {
                Bitmap bitmap = LruCacheUtils.getInstance().getBitmapFromMemCache(imageUrl);
                if (bitmap != null){
                    viewHolder.imageView.setImageBitmap(bitmap);
                    Log.d(TAG, "setData: from memory");
                }else {
                    Log.d(TAG, "setData: null");
                    LruCacheUtils.displayImageTarget(viewHolder.imageView,imageUrl,
                            LruCacheUtils.getTarget(viewHolder.imageView,imageUrl,position));
                }
            }
            viewHolder.imageView.setVisibility(View.VISIBLE);
        }else {
            viewHolder.imageView.setVisibility(View.GONE);
        }

        if (mOnItemClickListener != null) {

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setList(List<WeiXinResult.WeiXin> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    class WeiXinViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_jokeImage)
        ImageView imageView;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_des)
        TextView tvDes;
        @Bind(R.id.tv_ct)
        TextView tvCt;

        public WeiXinViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
