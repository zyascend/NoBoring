package com.zyascend.NoBoring.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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

import static java.security.AccessController.getContext;


public class CommunityAdapter extends BaseAdapter implements AVObjectKeysInterface {
    private final CircleTransform cf;
    private static final String TAG = "CommunityAdapter";
    private final Context mContext;

    private List<AVObject> mList = new ArrayList<>();

    public CommunityAdapter(Context context) {
        super();
        cf = new CircleTransform(context);
        mContext = context;
    }


    public void addAll(List<AVObject> list) {
        assert list != null;
        mList.addAll(list);
        notifyItemRangeInserted(mList.size() - 1, list.size());
    }


    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_community, parent, false);
        return new CommunityHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        setData(holder,position);
    }

    private void setData(RecyclerView.ViewHolder holder, int position) {
        final CommunityHolder communityHolder = (CommunityHolder) holder;
        AVObject data = mList.get(position);
        if (data == null)return;

        AVObject avObject = AVObject.createWithoutData(CIRCLES, data.getObjectId());
        avObject.fetchInBackground(POSTER, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                AVUser user = avObject.getAVUser(POSTER);
                if (user!=null){
                    communityHolder.tvUser.setText(user.getUsername());
                    AVFile head = user.getAVFile(HEAD_PIC);
                    if (head != null){
                        String headUrl = head.getUrl();
                        Log.d(TAG, "setData: headUrl = "+headUrl+"name = "+user.getUsername());
                        if (!TextUtils.isEmpty(headUrl)){
                            Glide.with(mContext)
                                    .load(headUrl)
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .transform(cf)
                                    .into(communityHolder.ivPerson);
                        }
                    }
                }
            }
        });

        communityHolder.tvUpdateAt.setText(ActivityUtils.formatDate(data.getUpdatedAt()));
        Log.d(TAG, "setData: poster is "+data.getAVUser(POSTER));
        communityHolder.tvComment.setText(String.valueOf(data.getNumber(COMMENT_NUM)));
        communityHolder.tvLikes.setText(String.valueOf(data.getNumber(LIKES_NUM)));
        communityHolder.tvTitle.setText(data.getString(TITLE));
        communityHolder.tvContent.setText(data.getString(CONTENT));

        if (data.getAVFile(PIC_1)!= null) {

            loadPic(data.getAVFile(PIC_1).getUrl(), communityHolder.ivPic1);

        }
        if (data.getAVFile(PIC_2)!= null){
            loadPic(data.getAVFile(PIC_2).getUrl(),communityHolder.ivPic2);
            Log.d(TAG, "setData: position = "+communityHolder.getAdapterPosition()+
                    "url = "+data.getAVFile(PIC_2).getUrl());
        }
        if (data.getAVFile(PIC_3)!= null){
            loadPic(data.getAVFile(PIC_3).getUrl(),communityHolder.ivPic3);
        }

        communityHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null){
                    mOnItemClickListener.onItemClick(communityHolder.getAdapterPosition());
                }
            }
        });


    }
    private void loadPic(String url,ImageView imageView) {
        imageView.setTag(R.id.imageid,url);
        if(imageView.getTag(R.id.imageid)!=null && url==imageView.getTag(R.id.imageid)){
            if (!TextUtils.isEmpty(url)){
                Glide.with(mContext)
                        .load(url)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
            }
        }
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    class CommunityHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_person)
        ImageView ivPerson;
        @Bind(R.id.tv_user)
        TextView tvUser;
        @Bind(R.id.tv_updateAt)
        TextView tvUpdateAt;
        @Bind(R.id.tv_comment)
        TextView tvComment;
        @Bind(R.id.tv_likes)
        TextView tvLikes;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_content)
        TextView tvContent;
        @Bind(R.id.iv_pic1)
        ImageView ivPic1;
        @Bind(R.id.iv_pic2)
        ImageView ivPic2;
        @Bind(R.id.iv_pic3)
        ImageView ivPic3;
        public CommunityHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

}
