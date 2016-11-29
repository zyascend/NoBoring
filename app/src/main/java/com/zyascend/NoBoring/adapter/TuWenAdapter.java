package com.zyascend.NoBoring.adapter;

import android.content.Context;
import android.text.TextUtils;
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
import com.zyascend.NoBoring.dao.TuWenJoke;
import com.zyascend.NoBoring.utils.ActivityUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zyascend on 2016/7/16.
 */
public class TuWenAdapter extends RecyclerArrayAdapter<TuWenJoke> {

    private static final String TAG = "TAG_DongTaiAdapter";


    public TuWenAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tuwen, parent, false);
        return new DongViewHolder(view);
    }


//    private List<DongTaiJokeResult.ShowapiResBodyBean.DongTaiJoke> mList;
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        final DongViewHolder viewHolder = (DongViewHolder) holder;
//        final String title = mList.get(position).getTitle();
//        viewHolder.tvTitle.setText(title);
//        final String url = mList.get(position).getImg();
//
//        if (url.isEmpty()){
//            viewHolder.ivJokeImage.setImageResource(R.drawable.ic_error);
//        }else {
//            viewHolder.ivPlayGif.setVisibility(View.VISIBLE);
//            Bitmap bitmap = LruCacheUtils.getInstance().getBitmapFromMemCache(url);
//            if (bitmap != null){
//                viewHolder.ivJokeImage.setImageBitmap(bitmap);
//            }else {
//                LruCacheUtils.displayImageTarget(viewHolder.ivJokeImage,url,LruCacheUtils.getTarget(viewHolder.ivJokeImage,url,position));
//            }
//        }
//
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(v.getContext(),PhotoActivity.class);
//                intent.putExtra(PhotoActivity.EXTRA_TITLE,title);
//                intent.putExtra(PhotoActivity.EXTRA_URL,url);
//                intent.putExtra(PhotoActivity.EXTRA_IS_GIF,true);
//                v.getContext().startActivity(intent);
//
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return mList == null ? 0 : mList.size();
//    }
//
//    public void setList(List<DongTaiJokeResult.ShowapiResBodyBean.DongTaiJoke> mList) {
//        this.mList = mList;
//        notifyDataSetChanged();
//    }

    class DongViewHolder extends BaseViewHolder<TuWenJoke> {

        @Bind(R.id.image)
        ImageView image;
        @Bind(R.id.gif)
        ImageView gif;
        @Bind(R.id.tv_longImage)
        TextView tvLongImage;
        @Bind(R.id.title)
        TextView title;

        public DongViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        @Override
        public void setData(TuWenJoke data) {
            super.setData(data);


            if (data == null) {
                return;
            }

            if (TextUtils.equals(data.getTime(),"gif")){
                gif.setVisibility(View.VISIBLE);
            }else {
                gif.setVisibility(View.GONE);
            }

            if (data.getPicHeight() > 300){
                tvLongImage.setVisibility(View.VISIBLE);
            }else {
                tvLongImage.setVisibility(View.GONE);
            }

            Glide.with(getContext())
                    .load(data.getThumbUrl())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_pic_holder)
                    .into(image);
            title.setText(data.getTitle());

        }
    }
}
