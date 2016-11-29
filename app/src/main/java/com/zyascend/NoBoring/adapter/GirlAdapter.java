package com.zyascend.NoBoring.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.dao.Girl;
import com.zyascend.NoBoring.dao.GirlResult;
import com.zyascend.NoBoring.utils.LruCacheUtils;


/**
 *
 * Created by Administrator on 2016/7/19.
 */
public class GirlAdapter extends RecyclerArrayAdapter<Girl> {
    private static final String TAG = "TAG_GirlAdapter";
    private Context mContext;
    public GirlAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new GirlViewHolder(parent);
    }

    private class GirlViewHolder extends BaseViewHolder<Girl> {

        private ImageView image;

        GirlViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_girl);
            image = $(R.id.image);
        }

        @Override
        public void setData(final Girl data) {
            super.setData(data);

            final String url = data.getUrl();

            Bitmap bitmap = LruCacheUtils.getInstance().getBitmapFromMemCache(url);
            if (bitmap != null){
                image.setImageBitmap(bitmap);
                Log.d(TAG, "setData: from memory");
            }else {
                Log.d(TAG, "setData: null");
                LruCacheUtils.displayImageTarget(image,url,LruCacheUtils.getTarget(image,url,getAdapterPosition()));
            }
        }

    }


}
