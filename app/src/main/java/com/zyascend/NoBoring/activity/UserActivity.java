package com.zyascend.NoBoring.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.base.BaseActivity;
import com.zyascend.NoBoring.utils.AVObjectKeysInterface;
import com.zyascend.NoBoring.utils.ActivityUtils;
import com.zyascend.NoBoring.utils.CircleTransform;
import com.zyascend.NoBoring.utils.view.CircleDrawale;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/10.
 */

public class UserActivity extends BaseActivity implements AVObjectKeysInterface {

    private static final int REQUEST_CODE = 111;
    @Bind(R.id.iv_headPic)
    ImageView ivHeadPic;
    @Bind(R.id.tv_userName)
    TextView tvUserName;
    @Bind(R.id.tv_follow)
    TextView tvFollow;
    @Bind(R.id.tv_follow_num)
    TextView tvFollowNum;
    @Bind(R.id.tv_fans)
    TextView tvFans;
    @Bind(R.id.tv_fans_num)
    TextView tvFansNum;
    @Bind(R.id.ll_userInfo)
    LinearLayout llUserInfo;
    @Bind(R.id.tv_circles_num)
    TextView tvCirclesNum;
    @Bind(R.id.ll_circles)
    LinearLayout llCircles;
    @Bind(R.id.tv_likes_num)
    TextView tvLikesNum;
    @Bind(R.id.ll_likes)
    LinearLayout llLikes;
    @Bind(R.id.tv_info)
    TextView tvInfo;
    @Bind(R.id.tv_circle)
    TextView tvCircle;
    @Bind(R.id.tv_likes)
    TextView tvLikes;
    private boolean isLogin;
    private AVUser user;

    @Override
    protected void initView() {
        user = AVUser.getCurrentUser();
        if (user == null){
            isLogin = true;
        }else {
            loadUserData();
        }
    }

    /**
     * 初始化数据
     */
    private void loadUserData() {

        loadHeadPic();

        String followNum = String.valueOf(user.getInt(FOLLOW_NUM));
        String fansNum = String.valueOf(user.getInt(FANS_NUM));
        tvFansNum.setText(fansNum);
        tvFollowNum.setText(followNum);

        tvUserName.setText(user.getUsername());

        String circlesNum = String.valueOf(user.getInt(CIRCLE_NUM));
        String LikesNum = String.valueOf(user.getInt(MY_LIKES_NUM));
        tvCirclesNum.setText(circlesNum);
        tvLikesNum.setText(LikesNum);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_user;
    }



    @OnClick({R.id.iv_headPic, R.id.tv_userName, R.id.tv_follow, R.id.tv_fans, R.id.ll_userInfo, R.id.ll_circles, R.id.ll_likes})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_headPic:
                changeHeadPic();
                break;
            case R.id.tv_userName:
                //doNothing
                break;
            case R.id.tv_follow:
                showFollowing();
                break;
            case R.id.tv_fans:
                showFans();
                break;
            case R.id.ll_userInfo:
                editInfo();
                break;
            case R.id.ll_circles:
                showCircle();
                break;
            case R.id.ll_likes:
                showLikes();
                break;
        }
    }

    private void showLikes() {

    }

    private void showCircle() {

    }

    private void editInfo() {

    }

    private void showFans() {
        try {
            AVQuery<AVUser> followerQuery = user.followerQuery(AVUser.class);
            // 也可以使用这个静态方法来获取非登录用户的好友关系
            // AVQuery<AVUser> followerQuery = AVUser.followerQuery(userA.getObjectId(),AVUser.class);
            followerQuery.findInBackground(new FindCallback<AVUser>() {
                @Override
                public void done(List<AVUser> avObjects, AVException avException) {
                    // avObjects 包含了 userA 的粉丝列表
                    if (avObjects!=null){
                        tvFollowNum.setText(String.valueOf(avObjects.size()-1));
                    }
                }
            });
        } catch (AVException e) {
            e.printStackTrace();
        }
    }

    private void showFollowing() {
        //查询关注者
        AVQuery<AVUser> followeeQuery = AVUser.followeeQuery(user.getObjectId(), AVUser.class);
        //AVQuery<AVUser> followeeQuery = userB.followeeQuery(AVUser.class);
        followeeQuery.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> avObjects, AVException avException) {
                //avObjects 就是用户的关注用户列表

            }
        });


    }

    private void changeHeadPic() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            setImageBitmap(requestCode,data);
        }
    }

    private void setImageBitmap(int requestCode, Intent data) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            if (bitmap == null){
                Toast.makeText(this, "设置失败", Toast.LENGTH_SHORT).show();
                return;
            }
            ivHeadPic.setImageDrawable(new CircleDrawale(this,bitmap));
            byte[] imageBytes = ActivityUtils.getBytes(getContentResolver().openInputStream(data.getData()));
            if (imageBytes != null){
                upLoadHeadPic(imageBytes);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void upLoadHeadPic(byte[] imageBytes) {
        assert user!=null;
        user.put(HEAD_PIC,new AVFile(HEAD_PIC_FILE,imageBytes));
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e ==null){
                    showToast("设置成功");
                    loadHeadPic();
                }else {
                    e.printStackTrace();
                    showToast("设置失败，请检查网络");
                }
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(UserActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void loadHeadPic() {

        String picUrl = user.getAVFile(HEAD_PIC) == null ? null : user.getAVFile(HEAD_PIC).getUrl();


        if (!TextUtils.isEmpty(picUrl)){
            Glide.with(this)
                    .load(picUrl)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .transform(new CircleTransform(this))
                    .into(ivHeadPic);
        }else {
            Glide.with(this)
                    .load(R.mipmap.launcher)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .transform(new CircleTransform(this))
                    .into(ivHeadPic);
        }
    }
}
