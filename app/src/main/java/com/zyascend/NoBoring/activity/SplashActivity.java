package com.zyascend.NoBoring.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.zyascend.NoBoring.R;

/**
 *
 * Created by Administrator on 2016/8/25.
 */
public class SplashActivity extends AppCompatActivity {

    private ImageView mImageView;
    private TextView des;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ativity_splash);

        initView();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            return;
        }
//        animateImage();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                overridePendingTransition(0, 0);
                SplashActivity.this.finish();
            }
        },1500);
        super.onWindowFocusChanged(hasFocus);
    }

    private void initView() {
        mImageView = (ImageView) findViewById(R.id.iv_splash);
        des = (TextView) findViewById(R.id.tv_des);
        Typeface typeFace = Typeface.createFromAsset(getAssets(),"NotoSansHans-Light.ttf");
        des.setTypeface(typeFace);
    }

    private void animateImage() {

        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(1800);
        mImageView.startAnimation(scaleAnimation);


        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                overridePendingTransition(0, 0);
                SplashActivity.this.finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
