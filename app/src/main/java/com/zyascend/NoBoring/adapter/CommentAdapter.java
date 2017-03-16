package com.zyascend.NoBoring.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.base.BaseAdapter;
import com.zyascend.NoBoring.utils.AVObjectKeysInterface;
import com.zyascend.NoBoring.utils.ActivityUtils;
import com.zyascend.NoBoring.utils.CircleTransform;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 * Created by Administrator on 2017/3/11.
 */

public class CommentAdapter extends BaseAdapter implements AVObjectKeysInterface {


    private final CircleTransform cf;
    private final Context mContext;

    private List<AVObject> mList = new ArrayList<>();

    public void addAll(List<AVObject> list){
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public CommentAdapter(Context context) {
        super();
        this.mContext = context;
        cf = new CircleTransform(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comments, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        setData(holder, position);
    }

    private void setData(final RecyclerView.ViewHolder holder, int position) {

        final CommentHolder thisHolder = (CommentHolder) holder;
        AVObject data = mList.get(position);
        if (data == null)return;
        AVObject avObject = AVObject.createWithoutData(COMMENT, data.getObjectId());
        avObject.fetchInBackground(POSTER, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                AVUser user = avObject.getAVUser(POSTER);
                if (user!=null){
                    thisHolder.tvUser.setText(user.getUsername());
                    AVFile head = user.getAVFile(HEAD_PIC);
                    if (head != null){
                        String headUrl = head.getUrl();
                        if (!TextUtils.isEmpty(headUrl)){

                            // TODO: 2017/3/16

                            Glide.with(mContext)
                                    .load(headUrl)
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .transform(cf)
                                    .into(thisHolder.ivPerson);
                        }
                    }
                }
            }
        });
        thisHolder.tvUpdateAt.setText(ActivityUtils.formatDate(data.getUpdatedAt()));
        thisHolder.tvLikes.setText(String.valueOf(data.getNumber(LIKES_NUM)));
        String content = data.getString(CONTENT);
        if (content!=null && content.contains("@")){
            int fstart=content.indexOf("@");
            int fend=content.indexOf(":");
            SpannableStringBuilder style=new SpannableStringBuilder(content);
            style.setSpan(new ForegroundColorSpan(Color.BLUE),fstart,fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            thisHolder.tvContent.setText(style);
        }else {
            thisHolder.tvContent.setText(content);
        }

        thisHolder.tvLikes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AVObject object = mList.get(thisHolder.getAdapterPosition());
                    object.increment(LIKES_NUM);
                    object.saveInBackground();
                    notifyItemChanged(thisHolder.getAdapterPosition(),"null");
                }
            });

        thisHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null){
                    mOnItemClickListener.onItemClick(thisHolder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void clear() {
        mList.clear();
    }


    class CommentHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_person)
        ImageView ivPerson;
        @Bind(R.id.tv_user)
        TextView tvUser;
        @Bind(R.id.tv_updateAt)
        TextView tvUpdateAt;
        @Bind(R.id.tv_likes)
        TextView tvLikes;
        @Bind(R.id.tv_content)
        TextView tvContent;
        public CommentHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }


//    class CommmentHolder extends BaseViewHolder<AVObject> {
//
//        private ImageView ivPerson;
//        private TextView tvUser;
//        private TextView tvUpdateAt;
//        private TextView tvLikes;
//        private TextView tvContent;
//
//        public CommmetHolder(ViewGroup parent) {
//            super(parent,);
//            ivPerson = $(R.id.iv_person);
//            tvUser = $(R.id.tv_user);
//            tvUpdateAt = $(R.id.tv_updateAt);
//            tvLikes = $(R.id.tv_likes);
//            tvContent = $(R.id.tv_content);
//
//            tvLikes.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    AVObject object = getAllData().get(getAdapterPosition());
//                    object.increment(LIKES_NUM);
//                    object.saveInBackground();
//                    notifyItemChanged(getAdapterPosition());
//                }
//            });
//
//        }
//
//        @Override
//        public void setData(AVObject data) {
//            super.setData(data);
//            if (data == null)return;
//
//
//            AVObject avObject = AVObject.createWithoutData(COMMENT, data.getObjectId());
//            avObject.fetchInBackground(POSTER, new GetCallback<AVObject>() {
//                @Override
//                public void done(AVObject avObject, AVException e) {
//                    AVUser user = avObject.getAVUser(POSTER);
//                    if (user!=null){
//                        tvUser.setText(user.getUsername());
//                        AVFile head = user.getAVFile(HEAD_PIC);
//                        if (head != null){
//                            String headUrl = head.getUrl();
//                            if (!TextUtils.isEmpty(headUrl)){
//                                Glide.with(getContext())
//                                        .load(headUrl)
//                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                                        .transform(cf)
//                                        .into(ivPerson);
//                            }
//                        }
//                    }
//                }
//            });
//
//            tvUpdateAt.setText(ActivityUtils.formatDate(data.getUpdatedAt()));
//            tvLikes.setText(String.valueOf(data.getNumber(LIKES_NUM)));
//            String content = data.getString(CONTENT);
//            if (content!=null && content.contains("@")){
//                int fstart=content.indexOf("@");
//                int fend=content.indexOf(":");
//                SpannableStringBuilder style=new SpannableStringBuilder(content);
//                style.setSpan(new ForegroundColorSpan(Color.BLUE),fstart,fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//                tvContent.setText(style);
//            }else {
//                tvContent.setText(content);
//            }
//
//        }
//    }
}
