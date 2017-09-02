package com.zyascend.NoBoring.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.util.LruCache;
import com.fenchtose.nocropper.CropperCallback;
import com.fenchtose.nocropper.CropperView;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.adapter.PhotoAdapter;
import com.zyascend.NoBoring.base.BaseActivity;
import com.zyascend.NoBoring.base.BaseAdapter;
import com.zyascend.NoBoring.bean.DownLoadBean;
import com.zyascend.NoBoring.bean.UploadBean;
import com.zyascend.NoBoring.dao.PhotoFolder;
import com.zyascend.NoBoring.utils.ActivityUtils;
import com.zyascend.NoBoring.utils.LogUtils;
import com.zyascend.NoBoring.utils.LruCacheUtils;
import com.zyascend.NoBoring.utils.picture.PhotoUtils;
import com.zyascend.NoBoring.utils.picture.luban.Luban;
import com.zyascend.NoBoring.utils.picture.luban.OnCompressListener;
import com.zyascend.NoBoring.utils.rx.RxTransformer;
import com.zyascend.NoBoring.utils.picture.BitmapUtils;
import com.zyascend.NoBoring.utils.rxbus.BackEvent;
import com.zyascend.NoBoring.utils.rxbus.NextEvent;
import com.zyascend.NoBoring.utils.rxbus.RxBus;
import com.zyascend.NoBoring.utils.rxbus.RxBusSubscriber;
import com.zyascend.NoBoring.utils.rxbus.RxSubscriptions;
import com.zyascend.NoBoring.utils.view.CoordinatorLinearLayout;
import com.zyascend.NoBoring.utils.view.CoordinatorRecyclerView;
import com.zyascend.NoBoring.utils.view.SpacesItemDecoration;
import com.zyascend.NoBoring.utils.view.ViewUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;


public class PublishActivity extends BaseActivity {

    private static final int CODE_READ = 1;
    private static final float TOP_REMAIN_HEIGHT = 48;
    @Bind(R.id.fileSpinner)
    Spinner fileSpinner;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.photo_frame)
    FrameLayout photoFrame;
    @Bind(R.id.recyclerView)
    CoordinatorRecyclerView photoRecyclerView;
    @Bind(R.id.parent_layout)
    CoordinatorLinearLayout parentLayout;
    @Bind(R.id.cropperView)
    CropperView cropperView;
    @Bind(R.id.btn_snap)
    ImageView btnSnap;
    @Bind(R.id.btn_rotate)
    ImageView btnRotate;

    private ProgressDialog progressDialog;
    private Map<String, PhotoFolder> folderMap;
    private PhotoAdapter adapter;
    private ArrayList<String> mFolderNameList;
    private PhotoFolder currentFolder;
    private boolean isSnappedToCenter;
    private Bitmap mBitmap;
    private String picPath;

    @Override
    protected void initView() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("图片处理中...");
        progressBar.setVisibility(View.VISIBLE);

        setLayoutSize();
        initRecyclerView();

        //检查动态权限
        requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, CODE_READ);
//        subscribeEvent();
    }


    private void setLayoutSize() {
        int topViewHeight = ViewUtils.dip2px(TOP_REMAIN_HEIGHT) + ViewUtils.getScreenWidth();
        int topBarHeight = ViewUtils.dip2px(TOP_REMAIN_HEIGHT);
        parentLayout.setTopViewParam(topViewHeight, topBarHeight);
        photoFrame.getLayoutParams().height = ViewUtils.getScreenWidth();
        photoRecyclerView.getLayoutParams().height = ViewUtils.getScreenHeight() - topBarHeight;
        parentLayout.getLayoutParams().height = topViewHeight + ViewUtils.getScreenHeight() - topBarHeight;
        photoRecyclerView.setCoordinatorListener(parentLayout);
    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        photoRecyclerView.setLayoutManager(gridLayoutManager);
        photoRecyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelOffset(R.dimen.decor_offset), 4));
        adapter = new PhotoAdapter(this);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                loadPhoto(position);
            }
        });
        photoRecyclerView.setAdapter(adapter);
    }

    private void loadPhoto(int position) {
        String filePath = currentFolder.getPhotoList().get(position).getPath();
        this.picPath = filePath;
        mBitmap = BitmapFactory.decodeFile(filePath);
        if (mBitmap == null){
            Toast.makeText(this, "加载图片出错", Toast.LENGTH_SHORT).show();
            return;
        }
        LogUtils.d("loadBitmap: " + mBitmap.getWidth() + " " + mBitmap.getHeight());

        int maxP = Math.max(mBitmap.getWidth(), mBitmap.getHeight());
        float scale1280 = (float)maxP / 1280;

        if (cropperView.getWidth() != 0) {
            cropperView.setMaxZoom(cropperView.getWidth() * 2 / 1280f);
        } else {
            ViewTreeObserver vto = cropperView.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    cropperView.getViewTreeObserver().removeOnPreDrawListener(this);
                    cropperView.setMaxZoom(cropperView.getWidth() * 2 / 1280f);
                    return true;
                }
            });

        }
        mBitmap = Bitmap.createScaledBitmap(mBitmap, (int)(mBitmap.getWidth()/scale1280),
                (int)(mBitmap.getHeight()/scale1280), true);
        cropperView.setImageBitmap(mBitmap);
    }

    private void requestPermission(String permission, int code) {
        if (!isAllowed(permission)) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, code);
        } else {
            getPhotoFolder();
        }
    }

    private void getPhotoFolder() {
        if (!ActivityUtils.isExternalStorageAvailable()) {
            Toast.makeText(this, "未检测到存储设备", Toast.LENGTH_SHORT).show();
            return;
        }

        Observable.create(new Observable.OnSubscribe<Map<String, PhotoFolder>>() {
            @Override
            public void call(Subscriber<? super Map<String, PhotoFolder>> subscriber) {
                Map<String, PhotoFolder> map = PhotoUtils.getPhotoFolder();
                if (map == null || map.isEmpty()) {
                    subscriber.onError(new Throwable("获取图片出错"));
                } else {
                    subscriber.onNext(map);
                    subscriber.onCompleted();
                }
            }
        })
                .compose(RxTransformer.INSTANCE.<Map<String, PhotoFolder>>transform(lifeCycleSubject))
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                })
                .subscribe(new Subscriber<Map<String, PhotoFolder>>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(PublishActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Map<String, PhotoFolder> folders) {
                        folderMap = folders;
                        setSpinner();
                    }
                });
    }

    private void setSpinner() {
        mFolderNameList = new ArrayList<>(folderMap.keySet());
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mFolderNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fileSpinner.setAdapter(adapter);
        fileSpinner.setSelection(mFolderNameList.indexOf("所有图片"));
        fileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toggleFolder(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void toggleFolder(int position) {

        currentFolder = folderMap.get(mFolderNameList.get(position));
        loadPhoto(0);

        adapter.addAll(currentFolder.getPhotoList());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODE_READ) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhotoFolder();
            } else {
                Toast.makeText(PublishActivity.this, "您没有授权该权限，请重试并打开授权", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean isAllowed(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                PackageManager.PERMISSION_GRANTED
                        == ActivityCompat.checkSelfPermission(this, permission);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish_pro;
    }


    private void publish() {
        Luban.with(PublishActivity.this)
                .load(new File(picPath))//传人要压缩的图片
                .setCompressListener(new OnCompressListener() {
                    //设置回调
                    @Override
                    public void onStart() {
                        progressDialog.setProgress(0);
                        progressDialog.show();
                    }
                    @Override
                    public void onSuccess(File file) {
                        //上传图片
                        progressDialog.dismiss();
                        onBitmapCompressed(file);
                    }
                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e.getMessage());
                        progressDialog.dismiss();
                    }
                })
                .launch();

    }

    private void onBitmapCompressed(File file) {
        if (file == null || !file.exists()){
            Toast.makeText(this, "上传出错", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, TaskService.class);
        UploadBean bean = new UploadBean(file.getAbsolutePath(),file.getName(),"");
        intent.putExtra(TaskService.BEAN,bean);
        intent.putExtra(TaskService.TASK_TYPE, TaskService.TYPE_UPLOAD);
        startService(intent);
        onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_continue:
                // TODO: 2017/7/22
                publish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.btn_snap, R.id.btn_rotate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_snap:
                if (isSnappedToCenter) {
                    cropperView.cropToCenter();
                } else {
                    cropperView.fitToCenter();
                }
                isSnappedToCenter = !isSnappedToCenter;
                break;
            case R.id.btn_rotate:
                if (mBitmap == null) {
                    return;
                }
                // TODO:2017/7/30 注意此处可能 OutOfMemoryError
                mBitmap = BitmapUtils.rotateBitmap(mBitmap, 90);
                cropperView.setImageBitmap(mBitmap);
                break;
        }
    }

    private void cropImageAsync() {
        cropperView.getCroppedBitmapAsync(new CropperCallback() {
            @Override
            public void onCropped(Bitmap bitmap) {
                if (bitmap != null) {
//                    compress(bitmap);
                }
            }
            @Override
            public void onOutOfMemoryError() {
                LogUtils.e("OutOfMemoryError");
            }
        });
    }

//    private Subscription mNextSub;
//
//    private void subscribeEvent(){
//        if (mNextSub != null && !mNextSub.isUnsubscribed()) {
//            //清除以前的订阅
//            RxSubscriptions.remove(mNextSub);
//        } else {
//            mNextSub = RxBus.getDefault().toObservableSticky(BackEvent.class)
//                    // 建议在Sticky时,在操作符内主动try,catch
//                    .subscribe(new RxBusSubscriber<BackEvent>() {
//                        @Override
//                        protected void onEvent(BackEvent event) {
//                            //上传成功，返回
//                            onBackPressed();
//                        }
//                    });
//        }
//        RxSubscriptions.add(mNextSub);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        RxSubscriptions.remove(mNextSub);
    }
}
