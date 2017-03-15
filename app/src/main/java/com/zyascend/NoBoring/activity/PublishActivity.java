package com.zyascend.NoBoring.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.base.BaseActivity;
import com.zyascend.NoBoring.utils.AVObjectKeysInterface;
import com.zyascend.NoBoring.utils.ActivityUtils;

import java.io.IOException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 *
 * Created by Administrator on 2017/3/8.
 */

public class PublishActivity extends BaseActivity implements AVObjectKeysInterface {

    private static final int PIC_CODE_01 = 111;
    private static final int PIC_CODE_02 = 222;
    private static final int PIC_CODE_03 = 333;
    @Bind(R.id.ed_title)
    EditText edTitle;
    @Bind(R.id.ed_content)
    EditText edContent;
    @Bind(R.id.pic1)
    ImageView pic1;
    @Bind(R.id.pic2)
    ImageView pic2;
    @Bind(R.id.pic3)
    ImageView pic3;
    @Bind(R.id.iv_addPic)
    ImageView ivAddPic;
    @Bind(R.id.tv_delete_pic)
    TextView tvDeletePic;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private byte[] mImageBytes_01 = null;
    private byte[] mImageBytes_02 = null;
    private byte[] mImageBytes_03 = null;

    private ProgressDialog progressDialog;

    @Override
    protected void initView() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.publishing));

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish;
    }


    private void publish() {
        String title = edTitle.getText().toString();
        String content = edContent.getText().toString();
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            showToast(getString(R.string.cant_null));
        } else if (title.length() > 20) {
            showToast(getString(R.string.noMore20));
        }else {
            doPublish(title,content);
        }

    }

    private void doPublish(String title, String content) {
        progressDialog.show();
        AVObject circle = new AVObject(CIRCLES);
        circle.put(POSTER, AVUser.getCurrentUser());
        circle.put(TITLE,title);
        circle.put(CONTENT,content);
        circle.put(COMMENT_NUM,0);
        circle.put(LIKES_NUM,0);
        if (mImageBytes_01 != null){
            circle.put(PIC_1,new AVFile(PIC_1_FILE,mImageBytes_01));
        }
        if (mImageBytes_02 != null){
            circle.put(PIC_2,new AVFile(PIC_2_FILE,mImageBytes_02));
        }
        if (mImageBytes_03 != null){
            circle.put(PIC_3,new AVFile(PIC_3_FILE,mImageBytes_03));
        }
        circle.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    progressDialog.dismiss();
                    PublishActivity.this.finish();
                } else {
                    progressDialog.dismiss();
                    // TODO: 2017/3/9 处理上传错误的逻辑
                    Toast.makeText(PublishActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publish, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_send:
                publish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick({R.id.pic1, R.id.pic2, R.id.pic3,R.id.tv_delete_pic,R.id.iv_addPic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pic1:
                requestImageBytes(PIC_CODE_01);
                break;
            case R.id.pic2:
                requestImageBytes(PIC_CODE_02);
                break;
            case R.id.pic3:
                requestImageBytes(PIC_CODE_03);
                break;
            case R.id.tv_delete_pic:
                deletePic();
                break;
            case R.id.iv_addPic:
                break;
        }
    }

    private void deletePic() {
        if (mImageBytes_03 != null){
            mImageBytes_03 = null;
            pic3.setImageResource(R.drawable.ic_add);
        }else if (mImageBytes_02 != null){
            mImageBytes_02 = null;
            pic2.setImageResource(R.drawable.ic_add);
        }else {
            mImageBytes_01 = null;
            pic1.setImageResource(R.drawable.ic_add);
        }
    }

    private void requestImageBytes(int picCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, picCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            setImageBitmap(requestCode,data);
        }
    }

    private void setImageBitmap(int requestCode,Intent data) {
        try {
            switch (requestCode){
                case PIC_CODE_01:
                    pic1.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData()));
                    mImageBytes_01 = ActivityUtils.getBytes(getContentResolver().openInputStream(data.getData()));
                    break;
                case PIC_CODE_02:
                    pic2.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData()));
                    mImageBytes_02 = ActivityUtils.getBytes(getContentResolver().openInputStream(data.getData()));
                    break;
                case PIC_CODE_03:
                    pic3.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData()));
                    mImageBytes_03 = ActivityUtils.getBytes(getContentResolver().openInputStream(data.getData()));
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放字节数组
        mImageBytes_03 = null;
        mImageBytes_02 = null;
        mImageBytes_01 = null;
    }
}
