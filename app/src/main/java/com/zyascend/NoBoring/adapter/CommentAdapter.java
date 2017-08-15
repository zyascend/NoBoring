package com.zyascend.NoBoring.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.base.BaseAdapter;
import com.zyascend.NoBoring.bean.CommentResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：
 * 作者：zyascend on 2017/8/15 23:07
 * 邮箱：zyascend@qq.com
 */

public class CommentAdapter extends RecyclerView.Adapter {


    public void addAll(List<CommentResponse> mList) {
        this.mList.addAll(mList);
        notifyDataSetChanged();
    }

    public void addOne(CommentResponse one){
        //在第一行添加数据
        mList.add(0,one);
        notifyItemInserted(0);
    }

    private List<CommentResponse> mList;

    public CommentAdapter() {
        mList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comments,parent,false);
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
