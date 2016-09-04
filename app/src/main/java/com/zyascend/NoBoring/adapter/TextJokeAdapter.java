package com.zyascend.NoBoring.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zyascend.NoBoring.R;

import com.zyascend.NoBoring.base.BaseAdapter;
import com.zyascend.NoBoring.data.Database;
import com.zyascend.NoBoring.model.TextJokeResult;
import com.zyascend.NoBoring.utils.ActivityUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 * Created by zyascend on 2016/7/16.
 */
public class TextJokeAdapter extends BaseAdapter {

    private List<TextJokeResult.ShowapiResBodyBean.TextJoke> mList ;
    private Database mSqlite;
    public TextJokeAdapter(Context context) {
        mSqlite = Database.getInstance(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_textjoke, parent, false);

        return new TextJokeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final TextJokeViewHolder viewHolder = (TextJokeViewHolder) holder;

        mSqlite.addJokes(mList.get(position));

        final String text = mList.get(position).getText().replaceAll("<.*?>", "");
        viewHolder.tv_JokeContent.setText(text);

        String createAt  = mList.get(position).getCt();
        String[] time = createAt.split(" ");
        viewHolder.tv_createAt.setText(time[0]);

        if (mOnItemClickListener != null){
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(viewHolder.getAdapterPosition());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setList(List<TextJokeResult.ShowapiResBodyBean.TextJoke> list) {
        this.mList = list;
        notifyDataSetChanged();
    }


    class TextJokeViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_jokeContent)
        TextView tv_JokeContent;
        @Bind(R.id.tv_ct)
        TextView tv_createAt;

        public TextJokeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
