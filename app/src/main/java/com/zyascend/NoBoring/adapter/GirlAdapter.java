package com.zyascend.NoBoring.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.data.Database;
import com.zyascend.NoBoring.model.GirlResult;
import com.zyascend.NoBoring.utils.LruCacheUtils;


/**
 *
 * Created by Administrator on 2016/7/19.
 */
public class GirlAdapter extends RecyclerArrayAdapter<GirlResult.Girl> {
    private static final String TAG = "TAG_GirlAdapter";
    private final Database mSqlite;
    private Context mContext;
    public GirlAdapter(Context context) {
        super(context);
        mContext = context;
        mSqlite = Database.getInstance(mContext);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new GrilViewHolder(parent);
    }

    class GrilViewHolder extends BaseViewHolder<GirlResult.Girl> {

        private ImageView image;

        public GrilViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_girl);
            image = $(R.id.image);
        }

        @Override
        public void setData(final GirlResult.Girl data) {
            super.setData(data);

            final String url = data.url;

            if (mSqlite.getGirl(url) == null){
                mSqlite.addGirls(data);
            }

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
