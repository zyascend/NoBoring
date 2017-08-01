package com.zyascend.NoBoring.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.adapter.UserPostAdapter;
import com.zyascend.NoBoring.base.BaseActivity;
import com.zyascend.NoBoring.bean.FollowResponse;
import com.zyascend.NoBoring.bean.ListResponse;
import com.zyascend.NoBoring.bean.ParcelableUser;
import com.zyascend.NoBoring.bean.PostResponse;
import com.zyascend.NoBoring.bean.UserResponse;
import com.zyascend.NoBoring.fragment.ListDialogFragment;
import com.zyascend.NoBoring.http.LeanCloudService;
import com.zyascend.NoBoring.http.RequestHelper;
import com.zyascend.NoBoring.utils.ActivityUtils;
import com.zyascend.NoBoring.utils.LogUtils;
import com.zyascend.NoBoring.utils.glide.CircleTransform;
import com.zyascend.NoBoring.utils.Constants;
import com.zyascend.NoBoring.utils.rx.RxTransformer;
import com.zyascend.NoBoring.utils.SPUtils;
import com.zyascend.NoBoring.utils.view.CircleDrawale;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 *
 * Created by Administrator on 2017/3/10.
 */

public class UserActivity extends BaseActivity{

    private static final int REQUEST_CODE = 111;
    public static final String USER_ID = "user";
    @Bind(R.id.iv_headPic)
    ImageView ivHeadPic;
    @Bind(R.id.tv_postCount)
    TextView tvPostCount;
    @Bind(R.id.tv_followerCount)
    TextView tvFollowerCount;
    @Bind(R.id.tv_followeeCount)
    TextView tvFolloweeCount;
    @Bind(R.id.btn_operate)
    TextView tvOperate;
    @Bind(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.btn_grid)
    ImageView btnGrid;
    @Bind(R.id.btn_List)
    ImageView btnList;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.view_empty)
    RelativeLayout viewEmpty;

    private boolean logined;
    private String sessionToken;
    private String userId;
    private ProgressDialog progressDialog;
    private boolean isGridMode;
    private UserPostAdapter adapter;
    private RecyclerView.LayoutManager gridLayoutManager;
    private RecyclerView.LayoutManager linerLayoutManager;

    @Override
    protected void initView() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("数据加载中");
        progressDialog.show();

        gridLayoutManager = new GridLayoutManager(this,3);
        linerLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new UserPostAdapter(this);
        recyclerView.setAdapter(adapter);

        sessionToken = SPUtils.getString(SPUtils.SESSION_TOKEN,null);
        userId = getIntent().getStringExtra(USER_ID);
        if (TextUtils.isEmpty(userId)){
            //打开个人页
            if (TextUtils.isEmpty(sessionToken)){
                //未登录
                LogUtils.d("未登录");
                logined = false;
                tvOperate.setText("点击登录");
            }else {
                //已登录
                LogUtils.d("token = "+sessionToken);
                logined = true;
                tvOperate.setText("编辑资料");
                loadLoginedData();
            }
        }else {
            //打开其他用户页
            LogUtils.d("userId = "+userId);
            tvOperate.setText("关注");
            loadUserData();
        }
    }

    private void loadLoginedData() {
        LeanCloudService.getInstance().getAPI()
                .getLoginedUser(sessionToken)
                .compose(RxTransformer.INSTANCE.<UserResponse>transform(lifeCycleSubject))
                .subscribe(new Subscriber<UserResponse>() {
                    @Override
                    public void onCompleted() {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        Toast.makeText(UserActivity.this, "加载出错...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(UserResponse userResponse) {
                        bindUserData(userResponse);
                    }
                });
    }

    private void bindUserData(UserResponse data) {
        if (data == null)return;

        tvPostCount.setText(data.getPostCount());
        tvFolloweeCount.setText(data.getFolloweeCount());
        tvFollowerCount.setText(data.getFollowerCount());

        Glide.with(this)
                .load(data.getAvatarUrl())
                .transform(new CircleTransform(this))
                .placeholder(R.drawable.ic_pic_holder)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(ivHeadPic);

        //获取当前User的所有Post数据
        loadPostData(data.getObjectId());
    }

    private void loadPostData(String userId) {

        Map<String,String> requsetMap = new HashMap<>();
        // TODO: 2017/7/23 urlencode ?
        //https://api.leancloud.cn/1.1/classes/Post?include=poster,picture
        requsetMap.put("order","createdAt");//or -createdAt
        requsetMap.put("include","picture");
        requsetMap.put("where","{\"posterId\":\""+userId+"\"}");
        LeanCloudService.getInstance().getAPI()
                .getUserPost(requsetMap)
                .compose(RxTransformer.INSTANCE.<ListResponse<PostResponse>>transform(lifeCycleSubject))
                .subscribe(new Subscriber<ListResponse<PostResponse>>() {
                    @Override
                    public void onCompleted() {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        Toast.makeText(UserActivity.this, "加载出错...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ListResponse<PostResponse> data) {
                        if(data == null)return;
                        bindPostData(data.getResults());
                    }
                });
    }

    private void bindPostData(List<PostResponse> results) {
        if (isGridMode){
            //grid布局
            recyclerView.setLayoutManager(gridLayoutManager);
            adapter.setGrid(true,false);
            adapter.addData(results);
        }else {
            //list布局
            recyclerView.setLayoutManager(linerLayoutManager);
            adapter.setGrid(false,false);
            adapter.addData(results);
        }
    }

    /**
     * 初始化数据
     */
    private void loadUserData() {
        LeanCloudService.getInstance().getAPI()
                .getUserById(userId)
                .compose(RxTransformer.INSTANCE.<UserResponse>transform(lifeCycleSubject))
                .subscribe(new Subscriber<UserResponse>() {
                    @Override
                    public void onCompleted() {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        Toast.makeText(UserActivity.this, "加载出错...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(UserResponse userResponse) {
                        bindUserData(userResponse);
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_pro;
    }

    private void showFans() {
        LeanCloudService.getInstance().getAPI()
                .getFollower(userId)
                .compose(RxTransformer.INSTANCE.<ListResponse<FollowResponse>>transform(lifeCycleSubject))
                .subscribe(new Subscriber<ListResponse<FollowResponse>>() {
                    @Override
                    public void onCompleted() {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        Toast.makeText(UserActivity.this, "加载出错...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ListResponse<FollowResponse> userResponse) {
                        if (userResponse == null)return;
                        showListDialog(userResponse.getResults(),true);
                    }
                });
    }

    private void showFollowing() {
        LeanCloudService.getInstance().getAPI()
                .getFollower(userId)
                .compose(RxTransformer.INSTANCE.<ListResponse<FollowResponse>>transform(lifeCycleSubject))
                .subscribe(new Subscriber<ListResponse<FollowResponse>>() {
                    @Override
                    public void onCompleted() {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        Toast.makeText(UserActivity.this, "加载出错...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ListResponse<FollowResponse> userResponse) {
                        if (userResponse == null)return;
                        showListDialog(userResponse.getResults(),false);
                    }
                });
    }

    private void showListDialog(List<FollowResponse> list,boolean showFollower) {
        if (list == null || list.isEmpty())return;
        ArrayList<ParcelableUser> users = new ArrayList<>();
        for (FollowResponse response : list){

            users.add(new ParcelableUser(response.getFollower().getUsername()
                    ,response.getFollower().getObjectId(),
                    response.getFollower().getAvatarUrl()));
        }
        String title = showFollower?"关注者":"正在关注";
        ListDialogFragment.getInstance(users,title).show(getSupportFragmentManager(),"listFragment");
    }


//    private void follow(String followedId){
//
//        Observable<String> follow = LeanCloudService.getInstance().getAPI()
//                .follow(userId,followedId)
//                .compose(RxTransformer.INSTANCE.<String>transform(lifeCycleSubject));
//
//        Observable<String> increaseFollowee = LeanCloudService.getInstance().getAPI()
//                .increment(userId, RequestHelper.getIncrementBody("followeeCount", Constants.OP_INCREMENT))
//                .compose(RxTransformer.INSTANCE.<String>transform(lifeCycleSubject));
//
//        Observable<String> increaseFollower = LeanCloudService.getInstance().getAPI()
//                .increment(userId, RequestHelper.getIncrementBody("followerCount", Constants.OP_INCREMENT))
//                .compose(RxTransformer.INSTANCE.<String>transform(lifeCycleSubject));
//
//        Observable.concat(follow,increaseFollowee,increaseFollower)
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String s) {
//                        tvOperate.setText("已关注");
//                    }
//                });
//    }

//    private void cancelFollow(String followedId){
//
//        Observable<String> cancelFollow = LeanCloudService.getInstance().getAPI()
//                .follow(userId,followedId)
//                .compose(RxTransformer.INSTANCE.<String>transform(lifeCycleSubject));
//
//        Observable<String> decreateFollowee = LeanCloudService.getInstance().getAPI()
//                .increment(userId, RequestHelper.getIncrementBody("followeeCount", Constants.OP_DECREMENT))
//                .compose(RxTransformer.INSTANCE.<String>transform(lifeCycleSubject));
//
//        // 被关注方的fans数也要减少
//        Observable<String> decreateFollower = LeanCloudService.getInstance().getAPI()
//                .increment(followedId, RequestHelper.getIncrementBody("followerCount", Constants.OP_DECREMENT))
//                .compose(RxTransformer.INSTANCE.<String>transform(lifeCycleSubject));
//
//        Observable.concat(cancelFollow,decreateFollowee,decreateFollower)
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String s) {
//                        tvOperate.setText("关注");
//                    }
//                });
//    }

    private void changeHeadPic() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setImageBitmap(requestCode, data);
        }
    }

    private void setImageBitmap(int requestCode, Intent data) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            if (bitmap == null) {
                Toast.makeText(this, "设置失败", Toast.LENGTH_SHORT).show();
                return;
            }
            ivHeadPic.setImageDrawable(new CircleDrawale(this, bitmap));
            byte[] imageBytes = ActivityUtils.getBytes(getContentResolver().openInputStream(data.getData()));
            if (imageBytes != null) {
                upLoadHeadPic(imageBytes);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void upLoadHeadPic(byte[] imageBytes) {

    }

    private void showToast(String msg) {
        Toast.makeText(UserActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void loadHeadPic() {

    }

}
