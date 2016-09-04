package com.zyascend.NoBoring.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.litesuits.common.io.FileUtils;
import com.litesuits.common.utils.FileUtil;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.base.BaseActivity;
import com.zyascend.NoBoring.utils.ActivityUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoView;

/**
 *
 * Created by Administrator on 2016/7/19.
 */
public class PhotoActivity extends BaseActivity {


    private static final String TAG = "TAG_PhotoActivity";
    @Bind(R.id.photoview)
    PhotoView photoView;


    private String title;
    private String url;
    private boolean isGif;
    private Bitmap mBitmap;

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_IS_GIF = "isGif";

    @Override
    protected void initView() {
        title = getIntent().getStringExtra(EXTRA_TITLE);
        url = getIntent().getStringExtra(EXTRA_URL);
        isGif = getIntent().getBooleanExtra(EXTRA_IS_GIF,false);
        setToolbarTitle(title);
        loadPhoto(isGif);
    }

    private void loadPhoto(boolean isGif) {
        if (!isGif){
            Glide.with(PhotoActivity.this)
                    .load(url)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            photoView.setImageBitmap(resource);
                            mBitmap = resource;
                        }
                    });
        }else{
            Glide.with(this)
                    .load(url)
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(photoView);
        }
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
                } else {
                    if (isGif){
                        saveGif();
                    }else {
                        savePhoto();
                    }
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveGif() {
        GifTask task = new GifTask(this);
        task.execute(url);
    }

    private void savePhoto() {

        Observable observable = Observable.just(mBitmap)
                .map(new Func1<Bitmap, Uri>() {
                    @Override
                    public Uri call(Bitmap bitmap) {
                        return handleBitmap(bitmap);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new Observer<Uri>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e){
                Log.e(TAG, "onError: "+ e.toString());
            }

            @Override
            public void onNext(Uri uri) {
                Toast.makeText(PhotoActivity.this, "已报存至"+uri.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private Uri handleBitmap(Bitmap bitmap) {

        if (bitmap != null) {
            File appDir = new File(Environment.getExternalStorageDirectory(), "NoBoring");
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            File file = new File(appDir, title + ".jpg");
            try {
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Uri uri = Uri.fromFile(file);

            Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
            getApplicationContext().sendBroadcast(scannerIntent);

            return uri;
        }
        return null;
    }

     class GifTask extends AsyncTask<String, Void, File> {
        private final Context context;

        public GifTask(Context context) {
            this.context = context;
        }

        @Override
        protected File doInBackground(String... params) {
            String url = params[0]; // should be easy to extend to share multiple images at once
            try {
                return Glide
                        .with(context)
                        .load(url)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();// needs to be called on background thread

            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(File result) {
            if (result == null) {
                return;
            }
            File appDir = new File(Environment.getExternalStorageDirectory(), "NoBoring");
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            File file = new File(appDir, title + ".gif");

            try {
                FileUtils.copyFile(result,file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Uri uri = Uri.fromFile(file);
            Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
            getApplicationContext().sendBroadcast(scannerIntent);
            ActivityUtils.showSnackBar(findViewById(R.id.container),"已保存"+uri.toString());

        }
    }

}
