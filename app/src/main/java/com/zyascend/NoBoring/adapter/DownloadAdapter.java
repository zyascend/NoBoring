package com.zyascend.NoBoring.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.activity.DownLoadActivity;
import com.zyascend.NoBoring.bean.DownLoadBean;
import com.zyascend.NoBoring.utils.ActivityUtils;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：2017/6/5
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {

    public static final int TYPE_ALL = 0;
    public static final int TYPE_FINISH = 1;
    public static final int TYPE_ING = 2;


    private List<DownloadTask> values;
    private NumberFormat numberFormat;
    private LayoutInflater inflater;
    private Context context;
    private int type;

    public void addDataListener(DataListener listener) {
        this.dataListener = listener;
    }
    private DataListener dataListener;
    public interface DataListener{
        void dataEmpty();
    }

    public DownloadAdapter(Context context) {
        this.context = context;
        numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(2);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateData(int type) {
        //将数据库的数据恢复
        this.type = type;
        if (type == TYPE_ALL) values = OkDownload.restore(DownloadManager.getInstance().getAll());
        if (type == TYPE_FINISH)
            values = OkDownload.restore(DownloadManager.getInstance().getFinished());
        if (type == TYPE_ING)
            values = OkDownload.restore(DownloadManager.getInstance().getDownloading());
        if (values.isEmpty() && dataListener != null){
            dataListener.dataEmpty();
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_download, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DownloadTask task = values.get(position);
        String tag = createTag(task);
        task.register(new ListDownloadListener(tag, holder));
        holder.setTag(tag);
        holder.setTask(task);
        holder.bind();
        holder.refresh(task.progress);
    }

    public void unRegister() {
        Map<String, DownloadTask> taskMap = OkDownload.getInstance().getTaskMap();
        for (DownloadTask task : taskMap.values()) {
            task.unRegister(createTag(task));
        }
    }

    private String createTag(DownloadTask task) {
        return type + "_" + task.progress.tag;
    }

    @Override
    public int getItemCount() {
        return values == null ? 0 : values.size();
    }

    @OnClick({R.id.btn_state, R.id.iv_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_state:
                break;
            case R.id.iv_delete:
                break;
        }
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_pic)
        ImageView ivPic;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_size)
        TextView tvSize;
        @Bind(R.id.btn_state)
        TextView btnState;
        @Bind(R.id.iv_delete)
        ImageView ivDelete;
        @Bind(R.id.progressBar)
        ProgressBar progressBar;
        @Bind(R.id.tv_progress)
        TextView tvProgress;
        private DownloadTask task;
        private String tag;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setTask(DownloadTask task) {
            this.task = task;
        }

        public void bind() {
            Progress progress = task.progress;
            DownLoadBean downloadInfo = (DownLoadBean) progress.extra1;
            if (downloadInfo != null) {
                Glide.with(context)
                        .load(downloadInfo.picUrl)
                        .error(R.drawable.ic_pic_holder)
                        .into(ivPic);
                tvTitle.setText(downloadInfo.name);
            } else {
                tvTitle.setText(progress.fileName);
            }
        }

        public void refresh(Progress progress) {
            String currentSize = Formatter.formatFileSize(context, progress.currentSize);
            String totalSize = Formatter.formatFileSize(context, progress.totalSize);
            tvSize.setText(currentSize + "/" + totalSize);
            switch (progress.status) {
                case Progress.NONE:
                    btnState.setText("下载");
                    break;
                case Progress.PAUSE:
                    btnState.setText("继续");
                    break;
                case Progress.ERROR:
                    btnState.setText("出错");
                    break;
                case Progress.WAITING:
                    btnState.setText("等待");
                    break;
                case Progress.FINISH:
                    btnState.setText("打开");
                    break;
                case Progress.LOADING:
                    String speed = Formatter.formatFileSize(context, progress.speed);
                    btnState.setText("暂停");
                    break;
            }
            tvProgress.setText(numberFormat.format(progress.fraction));
            progressBar.setMax(10000);
            progressBar.setProgress((int) (progress.fraction * 10000));
        }

        @OnClick(R.id.btn_state)
        public void start() {
            Progress progress = task.progress;
            switch (progress.status) {
                case Progress.PAUSE:
                case Progress.NONE:
                case Progress.ERROR:
                    task.start();
                    break;
                case Progress.LOADING:
                    task.pause();
                    break;
                case Progress.FINISH:
                    openMp4(new File(progress.filePath));
                    break;
            }
            refresh(progress);
        }

        private void openMp4(File file) {

        }

        @OnClick(R.id.iv_delete)
        public void remove() {
            task.remove(true);
            updateData(type);
        }

//        @OnClick(R.id.restart)
//        public void restart() {
//            task.restart();
//        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }
    }

    private class ListDownloadListener extends DownloadListener {

        private ViewHolder holder;

        ListDownloadListener(Object tag, ViewHolder holder) {
            super(tag);
            this.holder = holder;
        }

        @Override
        public void onStart(Progress progress) {
            Toast.makeText(context, "开始下载", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProgress(Progress progress) {
            if (tag == holder.getTag()) {
                holder.refresh(progress);
            }
        }

        @Override
        public void onError(Progress progress) {
            Throwable throwable = progress.exception;
            if (throwable != null) throwable.printStackTrace();
        }

        @Override
        public void onFinish(File file, Progress progress) {
            Toast.makeText(context, "下载完成:" + progress.filePath, Toast.LENGTH_SHORT).show();
            updateData(type);
        }

        @Override
        public void onRemove(Progress progress) {
            Toast.makeText(context, "文件已删除", Toast.LENGTH_SHORT).show();
        }
    }
}
