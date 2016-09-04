package com.zyascend.NoBoring.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

/**
 *
 * Created by Administrator on 2016/8/24.
 */
public class LruCacheUtils extends LruCache<String,Bitmap> {


    private static int MAXMEMONRY = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private static LruCacheUtils cacheUtils;

    private LruCacheUtils(int maxSize) {
        super(maxSize);
    }


    public static LruCacheUtils getInstance() {
        if (cacheUtils == null) {

            cacheUtils = new LruCacheUtils(MAXMEMONRY / 5);
        }
        return cacheUtils;
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    @Override
    protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
        super.entryRemoved(evicted, key, oldValue, newValue);
    }

    public void clearCache() {
        if (cacheUtils.size() > 0) {
            cacheUtils.evictAll();
        }
    }

    public synchronized void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (cacheUtils.get(key) != null) {
            return;
        }
        if (!isEmpty(key) && bitmap != null) {
            cacheUtils.put(key, bitmap);
        }
    }



    public synchronized Bitmap getBitmapFromMemCache(String key) {
        if (isEmpty(key)) {
            return null;
        }
        Bitmap bm = cacheUtils.get(key);
        if (bm != null && !bm.isRecycled()) {
            return bm;
        }
        return null;
    }


    public synchronized void removeImageCache(String key) {
        if (isEmpty(key)) {
            return;
        }
        Bitmap bm = cacheUtils.remove(key);
        if (bm != null && !bm.isRecycled()) {
            bm.recycle();
        }
    }



    public boolean isEmpty(String... str) {
        if (str == null) {
            return true;
        }
        for (String s : str) {
            if (s == null || s.isEmpty() || s.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static void displayImageTarget(final ImageView imageView, final String
            url, BitmapImageViewTarget target) {
        Glide.with(imageView.getContext())
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(target);
    }

    public static BitmapImageViewTarget getTarget(ImageView imageView, final String url,
                                            final int position) {
        return new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                super.setResource(resource);
                LruCacheUtils.getInstance().addBitmapToMemoryCache(url,
                        resource);
            }
        };
    }
}

