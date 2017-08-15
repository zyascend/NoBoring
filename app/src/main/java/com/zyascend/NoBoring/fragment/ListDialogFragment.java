package com.zyascend.NoBoring.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.adapter.ListDialogAdapter;
import com.zyascend.NoBoring.bean.ParcelableUser;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 功能：
 * 作者：zyascend on 2017/7/25 09:05
 * 邮箱：zyascend@qq.com
 */

public class ListDialogFragment extends DialogFragment {


    private static final String LIST_KEY = "list";
    private static final String TITLE_KEY = "title";

    @Bind(R.id.btn_close)
    ImageButton btnClose;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private ArrayList<ParcelableUser> mList;
    private String mTitle;

    private ListDialogAdapter adapter;

    public static ListDialogFragment getInstance(ArrayList<ParcelableUser> users,String title) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(LIST_KEY, users);
        bundle.putString(TITLE_KEY,title);
        ListDialogFragment fragment = new ListDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.translucentNoTitle);
        mList = getArguments().getParcelableArrayList(LIST_KEY);
        mTitle= getArguments().getString(TITLE_KEY);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        //DisplayMetrics dm = new DisplayMetrics();
        //getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        //getDialog().getWindow().setLayout(dm.widthPixels, dm.heightPixels);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_dialog, container, false);

        final Window window = getDialog().getWindow();
        if (window == null) return rootView;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);
        initView();
        ButterKnife.bind(this, rootView);
        return rootView;

    }

    private void initView() {
        tvTitle.setText(mTitle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ListDialogAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        adapter.addAll(mList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @OnClick(R.id.btn_close)
    public void onClick() {
        this.dismiss();
    }
}
