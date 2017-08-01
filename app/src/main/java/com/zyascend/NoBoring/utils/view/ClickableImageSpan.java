package com.zyascend.NoBoring.utils.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;
import android.view.View;

/**
 * ClickableImageSpan 继承自 ImageSpan，使其能响应点击事件，并图片垂直居中显示
 * @author lee
 *
 */
public abstract class ClickableImageSpan extends ImageSpan {

	public ClickableImageSpan(Drawable b) {
		super(b);
	}

	/** 图片垂直居中显示 */
	@Override
	public int getSize(Paint paint, CharSequence text, int start, int end,  
			Paint.FontMetricsInt fontMetricsInt) {

		Drawable drawable = getDrawable();  
		Rect rect = drawable.getBounds();  
		if (fontMetricsInt != null) {  
			Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();  
			int fontHeight = fmPaint.bottom - fmPaint.top;  
			int drHeight = rect.bottom - rect.top;  

			int top = drHeight / 2 - fontHeight / 4;  
			int bottom = drHeight / 2 + fontHeight / 4;  

			fontMetricsInt.ascent = -bottom;  
			fontMetricsInt.top = -bottom;  
			fontMetricsInt.bottom = top;  
			fontMetricsInt.descent = top;  
		}  
		return rect.right;  
	}  

	/** 图片垂直居中显示 */
	@Override  
	public void draw(Canvas canvas, CharSequence text, int start, int end,  
			float x, int top, int y, int bottom, Paint paint) { 

		Drawable drawable = getDrawable();  
		canvas.save();  
		int transY = 0;  
		transY = ((bottom - top) - drawable.getBounds().bottom) / 2 + top;  
		canvas.translate(x, transY);  
		drawable.draw(canvas);  
		canvas.restore();  
	}

	
	/** 添加点击事件  */
	public abstract void onClick(View view);
}


