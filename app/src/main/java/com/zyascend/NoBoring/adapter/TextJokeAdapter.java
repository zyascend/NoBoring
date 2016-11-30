package com.zyascend.NoBoring.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.dao.TextJoke;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 * Created by zyascend on 2016/7/16.
 */
public class TextJokeAdapter extends RecyclerArrayAdapter<TextJoke> {

//    private Typeface typeFace;

    public TextJokeAdapter(Context context) {
        super(context);
//        typeFace = Typeface.createFromAsset(context.getAssets(),"NotoSansHans-Light.ttf");

    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_textjoke, parent, false);
        return new TextJokeHolder(view);
    }

    class TextJokeHolder extends BaseViewHolder<TextJoke> {

        @Bind(R.id.tv_content)
        TextView tvContent;

        TextJokeHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }

        @Override
        public void setData(TextJoke data) {
            super.setData(data);
            if (data == null){
                return;
            }
            tvContent.setText(data.getContent());
        }
    }
}
