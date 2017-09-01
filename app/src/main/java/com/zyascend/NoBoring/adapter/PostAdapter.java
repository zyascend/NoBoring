package com.zyascend.NoBoring.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.bean.PostResponse;
import com.zyascend.NoBoring.utils.glide.CircleTransform;
import com.zyascend.NoBoring.utils.view.ExpandTextViewUtils;

import java.util.HashMap;


public class PostAdapter extends RecyclerArrayAdapter<PostResponse> {

    public static final int OPERATE_INCRE = 100;
    public static final int OPERATE_DECRE = 200;
    public static final int OPERATE_NULL = 0;

    public static final int VIEW_BTN_LIKE = 1;
    public static final int VIEW_BTN_COMMENT = 2;
    public static final int VIEW_IV_AVATAR = 3;

    private final Context mContext;
    private static final String TAG = "TAG_FreshAdapter";
    private final CircleTransform cf;
    private SparseBooleanArray isLikedMap;

    public PostAdapter(Context context) {
        super(context);
        mContext = context;
        cf = new CircleTransform(context);
        isLikedMap = new SparseBooleanArray();
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post,parent,false);
        return new PostHolder(view);
    }

    public interface ItemChildViewListener {
        void onClick(int position,int which,int operate);
    }

    public void setItemChildViewListener(ItemChildViewListener itemChildViewListener) {
        this.itemChildViewListener = itemChildViewListener;
    }

    private ItemChildViewListener itemChildViewListener;



    public class PostHolder extends BaseViewHolder<PostResponse> implements View.OnClickListener,
            ViewSwitcher.ViewFactory {

        private TextView tvUserName;
        private TextView tvDate;
        private TextView tvContent;

        private ImageButton btnLike;
        private ImageButton btnComment;

        private ImageView ivPic;
        private ImageView ivAvatar;

        private TextView tvLikeCount;
        private TextView tvCommentCount;

        PostHolder(View view){
            super(view);
            tvUserName = $(R.id.tv_userName);
            tvDate = $(R.id.tv_date);
            tvContent = $(R.id.tv_content);

            btnComment = $(R.id.btn_comments);
            btnLike = $(R.id.btn_like);

            ivAvatar = $(R.id.iv_avatar);
            ivPic = $(R.id.iv_pic);

            tvCommentCount = $(R.id.tv_commentCount);
            tvLikeCount = $(R.id.tv_likeCount);

        }
        @Override
        public void setData(PostResponse data) {
            super.setData(data);
            if (data == null)return;
            tvContent.setText(data.getContent());
            tvUserName.setText(data.getPoster().getUsername());
            tvDate.setText(data.getUpdatedAt());

            //try {
                //todo 在eclipse上测试
                //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-DDTHH:MM:SS.MMMZ", Locale.CHINA);
                //Date date = dateFormat.parse();
                tvDate.setText(data.getUpdatedAt());
            //} catch (ParseException e) {
               // e.printStackTrace();
            //}`

            btnComment.setOnClickListener(this);
            btnLike.setOnClickListener(this);
            ivAvatar.setOnClickListener(this);

            Glide.with(mContext)
                    .load(data.getPoster().getAvatarUrl())
                    .error(R.drawable.ic_pic_holder)
                    .placeholder(R.drawable.ic_pic_holder)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .transform(cf)
                    .into(ivAvatar);

            Glide.with(mContext)
                    .load(data.getPicture().getUrl())
                    .error(R.drawable.ic_pic_holder)
                    .placeholder(R.drawable.ic_pic_holder)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(ivPic);

            tvLikeCount.setText(String.valueOf(data.getLikesCount()));
            tvCommentCount.setText(String.valueOf(data.getCommentsCount()));

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            switch (v.getId()){
                case R.id.iv_avatar:
                    if (itemChildViewListener != null){
                        itemChildViewListener.onClick(position,VIEW_IV_AVATAR,OPERATE_NULL);
                    }
                    break;
                case R.id.btn_comments:
                    if (itemChildViewListener != null){
                        itemChildViewListener.onClick(position,VIEW_BTN_COMMENT,OPERATE_NULL);
                    }
                    break;
                case R.id.btn_like:

                    if (isLikedMap.get(position)){
                        isLikedMap.put(position,false);
                        //点过赞
                        btnLike.setImageResource(R.drawable.ic_unlike_gray_24dp);
                        //减少赞数
                        decrement(tvLikeCount);
                    }else {
                        isLikedMap.put(position,true);
                        //点过赞
                        btnLike.setImageResource(R.drawable.ic_liked_red_24dp);
                        //减少赞数
                        increment(tvLikeCount);
                    }
                    break;
            }
            notifyItemChanged(position,null);
        }

        private void decrement(TextView view) {
            int currentNum = Integer.parseInt(view.getText().toString());
            view.setText(String.valueOf(currentNum-1 <= 0 ? 0 : currentNum-1));
            if (itemChildViewListener != null){
                itemChildViewListener.onClick(getAdapterPosition(),VIEW_BTN_LIKE,OPERATE_DECRE);
            }
        }

        private void increment(TextView view) {
            int currentNum = Integer.parseInt(view.getText().toString());
            view.setText(String.valueOf(currentNum+1));
            if (itemChildViewListener != null){
                itemChildViewListener.onClick(getAdapterPosition(),VIEW_BTN_LIKE,OPERATE_INCRE);
            }
        }

        @Override
        public View makeView() {
            TextView textView = new TextView(mContext);
            textView.setTextSize(15);
            textView.setGravity(Gravity.CENTER);
            return textView;
        }
    }
}
