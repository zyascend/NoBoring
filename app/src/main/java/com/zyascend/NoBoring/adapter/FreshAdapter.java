package com.zyascend.NoBoring.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.dao.Fresh;
import com.zyascend.NoBoring.utils.SPUtils;

/**
 *
 * Created by zyascend on 2016/7/15.
 */
public class FreshAdapter extends RecyclerArrayAdapter<Fresh> {
    private final Context mContext;
    private static final String TAG = "TAG_FreshAdapter";
    public FreshAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fresh,parent,false);

        return new FreshViewHolder(view);
    }


    private class FreshViewHolder extends BaseViewHolder<Fresh> {


        private TextView tvTitle;
        private TextView tvAuthor;
        private TextView tvTag;
        private ImageView ivThumb;

        FreshViewHolder(View view){
            super(view);
//            tvAuthor = $(R.id.tv_author);
//            tvTag = $(R.id.tv_tag);
            tvTitle = $(R.id.tv_title);
            ivThumb = $(R.id.iv_preview);

        }

        @Override
        public void setData(Fresh data) {
            super.setData(data);

            if (data == null){
                return;
            }
            Log.d(TAG, "setData: "+data.getTitle());
            tvTitle.setText(data.getTitle());
//            tvAuthor.setText(data.getAuthor());
//            tvTag.setText(data.getTag());
            if (!SPUtils.getBoolean(SPUtils.SP_KEY_NOPIC,false,mContext)){
                Glide.with(mContext)
                        .load(data.getThumbUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ic_pic_holder)
                        .into(ivThumb);
            }else {
                ivThumb.setVisibility(View.GONE);
            }
        }
    }


//    private static final String TAG = "TAG_FreshAdapter";
//    private final Database mSqlite;
//    private List<Fresh> mList;
//    private Context mContext;
//
//    public FreshAdapter(Context context) {
//        mContext = context;
//        mSqlite = Database.getInstance(context);
//    }
//
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fresh, parent, false);
//        return new ReadListViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
//        ReadListViewHolder viewHolder = (ReadListViewHolder) holder;
//        final Fresh item = mList.get(position);
//        viewHolder.tvTitle.setText(item.getTitle());
//        viewHolder.tvAuthor.setText(item.getAuthor());
//        viewHolder.tvTag.setText(item.getTag());
//
//        if (!SPUtils.getBoolean(SPUtils.SP_KEY_NOPIC,false,mContext)){
//            viewHolder.ivPreview.setVisibility(View.VISIBLE);
//            Glide.with(viewHolder.itemView.getContext())
//                    .load(item.getUrl())
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .into(viewHolder.ivPreview);
//            Log.d(TAG, "onBindViewHolder: picUrl = " + item.getUrl());
//
//        }else {
//            viewHolder.ivPreview.setVisibility(View.GONE);
//        }
//
//        mSqlite.addNews(item);
//
//        if (mOnItemClickListener != null) {
//            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mOnItemClickListener.onItemClick(holder.getAdapterPosition());
//                }
//            });
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return mList == null ? 0 : mList.size();
//    }
//
//    public void setList(List<Fresh> mList) {
//        this.mList = mList;
//        notifyDataSetChanged();
//    }
//
//    class ReadListViewHolder extends RecyclerView.ViewHolder {
//        @Bind(R.id.tv_title)
//        TextView tvTitle;
//        @Bind(R.id.tv_author)
//        TextView tvAuthor;
//        @Bind(R.id.tv_tag)
//        TextView tvTag;
//        @Bind(R.id.iv_preview)
//        ImageView ivPreview;
//
//        public ReadListViewHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//        }
//    }
}
