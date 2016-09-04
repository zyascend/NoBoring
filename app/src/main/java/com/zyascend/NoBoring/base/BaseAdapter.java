package com.zyascend.NoBoring.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zyascend.NoBoring.model.Item;

/**
 * Created by Administrator on 2016/7/17.
 */
public abstract class BaseAdapter extends RecyclerView.Adapter {

    protected BaseAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
