package com.zyascend.NoBoring.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.dao.WeiXin;
import com.zyascend.NoBoring.dao.WeiXinResult;
import com.zyascend.NoBoring.utils.LruCacheUtils;
import com.zyascend.NoBoring.utils.SPUtils;

/**
 *
 * Created by zyascend on 2016/7/17.
 */
public class WeiXinAdapter extends RecyclerArrayAdapter<WeiXin> {


    private static final String TAG = "TAG_WeixinAdapter";
    private Context mContext;

    public WeiXinAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new WeixinHolder(parent);
    }

    private class WeixinHolder extends BaseViewHolder<WeiXin> {

        private ImageView image;
        private TextView title,des,date;

        WeixinHolder(ViewGroup parent) {
            super(parent,R.layout.item_weixin);
            image = $(R.id.iv_image);
            title = $(R.id.tv_title);
            des = $(R.id.tv_des);
            date = $(R.id.tv_ct);

        }


        @Override
        public void setData(WeiXin weiXin) {
            super.setData(weiXin);

            if (weiXin == null){
                return;
            }

            String imageUrl = weiXin.getUrl();
            title.setText(weiXin.getTitle());
            des.setText(weiXin.getDescription());
            date.setText(weiXin.getCtime());

            if (! SPUtils.getBoolean(SPUtils.SP_KEY_NOPIC,false,mContext)) {


                Glide.with(getContext())
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ic_pic_holder)
                        .into(image);

//                if (TextUtils.isEmpty(imageUrl)) {
//                    image.setImageResource(R.drawable.ic_error);
//                }else {
//                    Bitmap bitmap = LruCacheUtils.getInstance().getBitmapFromMemCache(imageUrl);
//                    if (bitmap != null){
//                        image.setImageBitmap(bitmap);
//                        Log.d(TAG, "setData: from memory");
//                    }else {
//                        Log.d(TAG, "setData: null");
//                        LruCacheUtils.displayImageTarget(image,imageUrl,
//                                LruCacheUtils.getTarget(image,imageUrl,getAdapterPosition()));
//                    }
//                }
                image.setVisibility(View.VISIBLE);
            }else {
                image.setVisibility(View.GONE);
            }

        }
    }


//    public WeiXinAdapter(Context context){
//        mContext = context;
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weixin, parent, false);
//
//        return new WeiXinViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
//
//        WeiXinViewHolder viewHolder = (WeiXinViewHolder) holder;
//        WeiXinResult.WeiXin weiXin = mList.get(position);
//

//
//        if (mOnItemClickListener != null) {
//
//            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mOnItemClickListener.onItemClick(holder.getAdapterPosition());
//                }
//            });
//        }
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return mList == null ? 0 : mList.size();
//    }
//
//    public void setList(List<WeiXinResult.WeiXin> mList) {
//        this.mList = mList;
//        notifyDataSetChanged();
//    }
//
//    class WeiXinViewHolder extends RecyclerView.ViewHolder {
//
//        @Bind(R.id.iv_jokeImage)
//        ImageView imageView;
//        @Bind(R.id.tv_title)
//        TextView tvTitle;
//        @Bind(R.id.tv_des)
//        TextView tvDes;
//        @Bind(R.id.tv_ct)
//        TextView tvCt;
//
//        public WeiXinViewHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this,itemView);
//        }
//    }
}
