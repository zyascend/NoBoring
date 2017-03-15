package com.zyascend.NoBoring.utils.view;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2017/3/11.
 */

public class FloatScrollView extends ScrollView {

    private int downX;
    private int downY;
    private int mTouchSlop;


    public FloatScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatScrollView(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public FloatScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

    }




    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent(e);
//        boolean intercept = false;
//        switch (ev.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                intercept = false;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                intercept = !isFloating();
//                break;
//            case MotionEvent.ACTION_UP:
//                intercept = false;
//                break;
//        }
//
//        return intercept;
    }
}
