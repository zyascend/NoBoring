package com.zyascend.NoBoring.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.base.BaseActivity;
import com.zyascend.NoBoring.utils.ActivityUtils;
import com.zyascend.NoBoring.utils.view.RegisterDialog;

import butterknife.Bind;
import butterknife.OnClick;

/**
 *
 * Created by Administrator on 2017/3/7.
 */

public class LoginActivity extends BaseActivity implements TextView.OnEditorActionListener {


    private static final String TAG = "LoginActivity";
    @Bind(R.id.username)
    AutoCompleteTextView ed_username;
    @Bind(R.id.password)
    EditText ed_password;
    @Bind(R.id.username_login_button)
    Button usernameLoginButton;
    @Bind(R.id.username_register_button)
    Button usernameRegisterButton;
    @Bind(R.id.email_login_form)
    LinearLayout emailLoginForm;
    @Bind(R.id.tv_jumpLogin)
    TextView tvJumpLogin;
    ProgressDialog progressDialog;


    @Override
    protected void initView() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loginng));

        if (AVUser.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            LoginActivity.this.finish();
        }

        ed_password.setOnEditorActionListener(this);
    }

    @OnClick({R.id.username_login_button, R.id.username_register_button, R.id.tv_jumpLogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.username_login_button:
                startLogin();
                break;
            case R.id.username_register_button:
                showRegisterDialog();
                break;
            case R.id.tv_jumpLogin:
                jumpLogin();
                break;
        }
    }

    private void jumpLogin() {
        ActivityUtils.jumpTo(this,MainActivity.class);
        this.finish();
    }

    private void showRegisterDialog() {
        Log.d(TAG, "showRegisterDialog: ");
        RegisterDialog dialog = new RegisterDialog(this);
        dialog.setDialogListener(new RegisterDialog.onDialogRegistered() {
            @Override
            public void onRegister(String username, String passWord) {
                doRegister(username,passWord);
            }
        });
        dialog.show();
    }

    private void doRegister(String username, String password) {
        showProgress(true);

        AVUser user = new AVUser();// 新建 AVUser 对象实例
        user.setUsername(username);// 设置用户名
        user.setPassword(password);// 设置密码
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    // 注册成功，把用户对象赋值给当前用户 AVUser.getCurrentUser()
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    LoginActivity.this.finish();
                } else {
                    // 失败的原因可能有多种，常见的是用户名已经存在。
                    showProgress(false);
                    showError(e.getCode());
                }
            }
        });
    }

    private void showError(int code) {
        String msg = null;
        switch (code){
            case 211:
                msg = "用户名不存在";
                break;
            case 210:
                msg = "用户名和密码不匹配";
                break;
            case 218:
                msg = "用户名和密码不可为空";
                break;
            case 202:
                msg = "用户名已被占用";
                break;
            default:
                msg = "出错啦，请检查网络或重试";
                break;
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == R.id.login || actionId == EditorInfo.IME_NULL) {
            startLogin();
            return true;
        }
        return false;
    }

    private void startLogin() {
        ed_password.setError(null);
        ed_username.setError(null);

        final String username = ed_password.getText().toString();
        final String password = ed_username.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(username)){
            ed_username.setError("用户名不可为空");
            focusView = ed_username;
            cancel = true;
        }

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            ed_password.setError(getString(R.string.error_invalid_password));
            focusView = ed_password;
            cancel = true;
        }

        if (cancel){
            focusView.requestFocus();
        }else {
            showProgress(true);

            AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {
                    if (e == null) {
                        LoginActivity.this.finish();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        showProgress(false);
                        showError(e.getCode());
                    }
                }
            });

        }
    }

    private void showProgress(boolean show) {
        if (progressDialog == null)return;
        if (show){
            progressDialog.show();
        }else {
            progressDialog.hide();
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    @Override
    protected void onPause() {
        super.onPause();
        AVAnalytics.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AVAnalytics.onResume(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }
}
