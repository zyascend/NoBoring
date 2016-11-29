package com.zyascend.NoBoring.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.litesuits.common.io.FileUtils;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.base.BaseActivity;
import com.zyascend.NoBoring.utils.ActivityUtils;
import com.zyascend.NoBoring.utils.RxTransformer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 *
 * Created by Administrator on 2016/7/19.
 */
public class PhotoActivity extends BaseActivity {


    private static final String TAG = "TAG_PhotoActivity";

    @Bind(R.id.photo)
    SubsamplingScaleImageView largeView;
    @Bind(R.id.gif_imageView)
    ImageView gifView;


    private String title;
    private String url;
    private boolean isGif;

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_IS_GIF = "isGif";
    private ProgressDialog dialog;

    @Override
    protected void initView() {

        largeView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);

        largeView.setMinScale(1.0F);//最小显示比例

        largeView.setMaxScale(5.0F);//最大显示比例

        dialog = new ProgressDialog(this);
        title = getIntent().getStringExtra(EXTRA_TITLE);
        url = getIntent().getStringExtra(EXTRA_URL);
        isGif = getIntent().getBooleanExtra(EXTRA_IS_GIF,false);
        setToolbarTitle(title);

        showLoading();

        if (!isGif){
            loadLargePic();
        }else{
            loadGif();
        }
    }

    private void loadGif() {
        largeView.setVisibility(View.GONE);
        gifView.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(url)
                .asGif()
                .listener(new RequestListener<String, GifDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GifDrawable> target, boolean isFirstResource) {
                        showError();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, String model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        showSuccess();
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
        .into(gifView);
    }

    private void loadLargePic() {
        largeView.setVisibility(View.VISIBLE);
        gifView.setVisibility(View.GONE);
        Glide.with(PhotoActivity.this)
                .load(url)
                .downloadOnly(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                        showSuccess();
                        largeView.setImage(ImageSource.uri(Uri.fromFile(resource)),new ImageViewState(1.5F, new PointF(0, 0), 0));
                    }

                    @Override
                    public void onStart() {
                        super.onStart();

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        showError();
                    }
                });
    }

    private void showError() {
        Toast.makeText(this, "啊哦，加载失败了...", Toast.LENGTH_SHORT).show();
    }

    private void showSuccess(){
        dialog.dismiss();
    }

    private void showLoading() {
        dialog.setMessage("图片加载中...");
        dialog.show();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_detail;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;
            case R.id.action_share:
                ActivityUtils.share(this,title,url);
                break;
            case R.id.action_save:
                if (ContextCompat.checkSelfPermission(this , Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                    Toast.makeText(this, "权限已获取，请重试", Toast.LENGTH_SHORT).show();
                } else {
                    savePhoto();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void savePhoto() {

        dialog.setMessage("下载中...");
        dialog.show();

        Observable.just(url)
                .map(new Func1<String, Uri>() {
                    @Override
                    public Uri call(String s) {
                        return handleBitmap(s);
                    }
                })
                .compose(RxTransformer.INSTANCE.<Uri>transform(lifeCycleSubject))
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (Looper.getMainLooper() == Looper.myLooper()){
                            Log.d(TAG, "call: -------->这是主线程");
                        }
                    }
                })
                .subscribe(new Subscriber<Uri>() {
                    @Override
                    public void onCompleted() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        Toast.makeText(PhotoActivity.this, "啊哦，下载失败了...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Uri uri) {
                        Toast.makeText(PhotoActivity.this, "已报存至"+uri.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private Uri handleBitmap(String url) {

        File result = null;
        try {
            result = Glide
                    .with(PhotoActivity.this)
                    .load(url)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();

        }

        File appDir = new File(Environment.getExternalStorageDirectory(), "NoBoring");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String name = isGif ? ".gif" : ".jpg";
        File file = new File(appDir, title + name);
        try {
            assert result != null;
            FileUtils.copyFile(result,file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Uri uri = Uri.fromFile(file);
        Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        getApplicationContext().sendBroadcast(scannerIntent);

//        if (bitmap != null) {
//            File appDir = new File(Environment.getExternalStorageDirectory(), "NoBoring");
//            if (!appDir.exists()) {
//                appDir.mkdir();
//            }
//            File file = new File(appDir, title + ".jpg");
//            try {
//                FileOutputStream fos = new FileOutputStream(file);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                fos.flush();
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            Uri uri = Uri.fromFile(file);
//
//            Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
//            getApplicationContext().sendBroadcast(scannerIntent);

        return uri;

    }

}
