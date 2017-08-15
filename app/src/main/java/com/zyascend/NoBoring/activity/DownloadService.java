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

import com.lzy.okgo.OkGo;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.bean.DownLoadBean;

import java.io.File;

/**
 * 功能：
 * 作者：zyascend on 2017/8/15 15:59
 * 邮箱：zyascend@qq.com
 */

public class DownloadService extends Service {

    private static final String DEFAULT_FOLDER = Environment.getExternalStorageDirectory().getAbsolutePath() + "/NoBoringDownload/";
    public static final String DOWNLOAD_BEAN = "download_bean";
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

        startDownload(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void startDownload(Intent intent) {

        DownLoadBean downLoad = (DownLoadBean) intent.getSerializableExtra(DOWNLOAD_BEAN);

        GetRequest<File> request = OkGo.get(downLoad.downloadUrl);

        OkDownload.request(downLoad.downloadUrl, request)//
                .priority(50)//
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
        DownLoadBean downLoadBean = (DownLoadBean)progress.extra1;
        String title = downLoadBean.name;
        mBuilder.setContentTitle(title)
                .setContentText("正在下载")
                .setSmallIcon(R.drawable.ic_file_download)
                .setProgress(10000,(int) (progress.fraction * 10000), false);
        mNotifyManager.notify(downLoadBean.id,mBuilder.build());
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        OkDownload.getInstance().pauseAll();
    }
}
