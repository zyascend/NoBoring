package com.zyascend.NoBoring.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
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
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.utils.AVObjectKeysInterface;
import com.zyascend.NoBoring.utils.ActivityUtils;
import com.zyascend.NoBoring.utils.CircleTransform;




public class CommunityAdapter extends RecyclerArrayAdapter<AVObject> implements AVObjectKeysInterface {
    private final CircleTransform cf;
    private static final String TAG = "CommunityAdapter";
    public CommunityAdapter(Context context) {
        super(context);
        cf = new CircleTransform(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommunityHolder(parent);
    }

    private class CommunityHolder extends BaseViewHolder<AVObject> {


        private ImageView ivPerson;
        private TextView tvUser;
        private TextView tvUpdateAt;
        private TextView tvComment;
        private TextView tvLikes;
        private TextView tvTitle;
        private TextView tvContent;
        private ImageView ivPic1;
        private ImageView ivPic2;
        private ImageView ivPic3;

        public CommunityHolder(ViewGroup parent) {
            super(parent, R.layout.item_community);
            ivPerson = $(R.id.iv_person);
            tvUser = $(R.id.tv_user);
            tvUpdateAt = $(R.id.tv_updateAt);
            tvComment = $(R.id.tv_comment);
            tvLikes = $(R.id.tv_likes);
            tvTitle = $(R.id.tv_title);
            tvContent = $(R.id.tv_content);
            ivPic1 = $(R.id.iv_pic1);
            ivPic2 = $(R.id.iv_pic2);
            ivPic3 = $(R.id.iv_pic3);
        }

        @Override
        public void setData(AVObject data) {
            super.setData(data);
            if (data == null)return;
            Log.d(TAG, "setData: excute ---------");
            AVObject avObject = AVObject.createWithoutData(CIRCLES, data.getObjectId());
            avObject.fetchInBackground(POSTER, new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    AVUser user = avObject.getAVUser(POSTER);
                    if (user!=null){
                        tvUser.setText(user.getUsername());
                        AVFile head = user.getAVFile(HEAD_PIC);
                        if (head != null){
                            String headUrl = head.getUrl();
                            Log.d(TAG, "setData: headUrl = "+headUrl+"name = "+user.getUsername());
                            if (!TextUtils.isEmpty(headUrl)){
                                Glide.with(getContext())
                                        .load(headUrl)
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .transform(cf)
                                        .into(ivPerson);
                            }
                        }
                    }
                }
            });

            tvUpdateAt.setText(ActivityUtils.formatDate(data.getUpdatedAt()));
            Log.d(TAG, "setData: poster is "+data.getAVUser(POSTER));
            tvComment.setText(String.valueOf(data.getNumber(COMMENT_NUM)));
            tvLikes.setText(String.valueOf(data.getNumber(LIKES_NUM)));
            tvTitle.setText(data.getString(TITLE));
            tvContent.setText(data.getString(CONTENT));

            if (data.getAVFile(PIC_1)!= null){
                loadPic(data.getAVFile(PIC_1).getUrl(),ivPic1);
            }else {
                ivPic1.setVisibility(View.GONE);
            }
            if (data.getAVFile(PIC_2)!= null){
                loadPic(data.getAVFile(PIC_2).getUrl(),ivPic2);
            }else {
                ivPic1.setVisibility(View.GONE);
            }
            if (data.getAVFile(PIC_3)!= null){
                loadPic(data.getAVFile(PIC_3).getUrl(),ivPic3);
            }else {
                ivPic1.setVisibility(View.GONE);
            }


        }

        private void loadPic(String url,ImageView imageView) {
            imageView.setTag(R.id.imageid,url);
            if(imageView.getTag(R.id.imageid)!=null&&url==imageView.getTag(R.id.imageid)){
                if (!TextUtils.isEmpty(url)){
                    Glide.with(getContext())
                            .load(url)
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView);
                }
            }
        }
    }
}
