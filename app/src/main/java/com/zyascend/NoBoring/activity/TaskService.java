package com.zyascend.NoBoring.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.bean.DownLoadBean;
import com.zyascend.NoBoring.bean.UploadBean;
import com.zyascend.NoBoring.utils.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;

import static com.zyascend.NoBoring.R.id.progressBar;
import static com.zyascend.NoBoring.R.id.up;

/**
 * 功能：负责视频下载和图片上传
 * 作者：zyascend on 2017/8/15 15:59
 * 邮箱：zyascend@qq.com
 */

public class TaskService extends Service {

    private static final String DEFAULT_FOLDER = Environment.getExternalStorageDirectory().getAbsolutePath() + "/NoBoringDownload/";
    public static final String BEAN = "download_bean";
    public static final String TASK_TYPE = "task";
    public static final String TYPE_UPLOAD = "task_upload";
    public static final String TYPE_DOWNLOAD = "task_download";
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private Notification notification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.d("service created");
        OkDownload.getInstance()
                .setFolder(DEFAULT_FOLDER);
        OkDownload.getInstance()
                .getThreadPool().setCorePoolSize(3);

        mNotifyManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);

        Intent resultIntent = new Intent(this, DownLoadActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d("service onStartCommand");
        if (TextUtils.equals(intent.getStringExtra(TASK_TYPE),TYPE_UPLOAD)){
            //开启上传任务
            startUpLoad(intent);
        }else {
            //开启下载任务
            startDownload(intent);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startUpLoad(Intent intent) {
        final UploadBean upload = (UploadBean) intent.getSerializableExtra(BEAN);

            try {
            //注意以下情况：
            //文件未上传完毕，就退出了Activity，导致空指针异常
            //解决方案：开启一个服务来上传，上传完毕通知刷新
            final AVFile avfile = AVFile.withAbsoluteLocalPath(upload.fileName, upload.filePath);
            avfile.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    //关联Post对象
                    if (e != null){
                        publisj(avfile.getObjectId());
                    }else {
                        showError();
                    }
                }
            }, new ProgressCallback() {
                @Override
                public void done(Integer integer) {
                    showUpLoadNotification(integer,upload.fileName);
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showError() {

    }

    private void showUpLoadNotification(Integer progress, String fileName) {
        mBuilder.setContentTitle(fileName)
                .setContentText("正在上传")
                .setSmallIcon(R.drawable.ic_file_download)
                .setContentIntent(null)//设置不能点击
                .setProgress(100,progress,false);
        LogUtils.d("id = "+1000+" name = "+fileName);
        mNotifyManager.notify(1000,mBuilder.build());
    }

    private void startDownload(Intent intent) {

        DownLoadBean downLoad = (DownLoadBean) intent.getSerializableExtra(BEAN);

        GetRequest<File> request = OkGo.get(downLoad.downloadUrl);

        OkDownload.request(downLoad.downloadUrl, request)//
                .priority(50)//
                .fileName(downLoad.name)
                .extra1(downLoad)//
                .save()//
                .register(new DownloadListener(downLoad.downloadUrl) {
                    @Override
                    public void onStart(Progress progress) {
                        showNotification(progress);
                    }

                    @Override
                    public void onProgress(Progress progress) {
                        mBuilder.setProgress(10000,(int) (progress.fraction * 10000), false);
                        mNotifyManager.notify(((DownLoadBean)progress.extra1).id,mBuilder.build());

                    }
                    @Override
                    public void onError(Progress progress) {
                        mBuilder.setContentText("下载出错");
                        mNotifyManager.notify(((DownLoadBean)progress.extra1).id,mBuilder.build());
                    }
                    @Override
                    public void onFinish(File file, Progress progress) {
                        mNotifyManager.cancel(((DownLoadBean) progress.extra1).id);
                    }

                    @Override
                    public void onRemove(Progress progress) {
                        mNotifyManager.cancel(((DownLoadBean) progress.extra1).id);
                    }
                })
                .start();
                //.register(new LogDownloadListener())//
    }

    private void showNotification(Progress progress) {
        LogUtils.d("service download start");
        DownLoadBean downLoadBean = (DownLoadBean)progress.extra1;
        String title = downLoadBean.name;
        mBuilder.setContentTitle(title)
                .setContentText("正在下载")
                .setSmallIcon(R.drawable.ic_file_download)
                .setProgress(10000,(int) (progress.fraction * 10000), false);
        LogUtils.d("id = "+downLoadBean.id+"name = "+title);
        mNotifyManager.notify(downLoadBean.id,mBuilder.build());
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        OkDownload.getInstance().pauseAll();
    }
}
