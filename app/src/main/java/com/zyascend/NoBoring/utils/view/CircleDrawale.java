package com.zyascend.NoBoring.utils.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import com.zyascend.NoBoring.R;

/**
 * Created by Administrator on 2017/3/10.
 */

public class CircleDrawale extends Drawable {

    private Paint mPaint;
    private int mWidth;
    private Bitmap mBitmap ;
    private Bitmap defaultBitmap;

    public CircleDrawale(Context context,Bitmap bitmap) {
        if (bitmap == null) {
            mBitmap = getDefaultBitmap(context);
        }
        mBitmap = bitmap;
        BitmapShader bitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);
        mWidth = Math.min(mBitmap.getWidth(), mBitmap.getHeight());
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth / 2, mPaint);
    }

    @Override
    public int getIntrinsicWidth() {
        return mWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mWidth;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public Bitmap getDefaultBitmap(Context context) {
        return BitmapFactory.decodeResource(context.getResources(), R.mipmap.launcher);
    }
}
