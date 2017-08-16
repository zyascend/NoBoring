package com.zyascend.NoBoring.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
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
import com.zyascend.NoBoring.R;
import com.zyascend.NoBoring.base.BaseActivity;
import com.zyascend.NoBoring.bean.LoginResponse;
import com.zyascend.NoBoring.bean.RegisterResponse;
import com.zyascend.NoBoring.http.LeanCloudService;
import com.zyascend.NoBoring.http.RequestHelper;
import com.zyascend.NoBoring.utils.ActivityUtils;
import com.zyascend.NoBoring.utils.Constants;
import com.zyascend.NoBoring.utils.LogUtils;
import com.zyascend.NoBoring.utils.rx.RxTransformer;
import com.zyascend.NoBoring.utils.SPUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.zyascend.NoBoring.http.ZhihuService.CACHE_INTERCEPTOR;
import static com.zyascend.NoBoring.http.ZhihuService.LOG_INTERCEPTOR;

/**
 *
 * Created by Administrator on 2017/3/7.
 */

public class LoginActivity extends BaseActivity implements TextView.OnEditorActionListener {

    private static final String TAG = "LoginActivity";
    public static final String FROM_EXIT_LOGIN = "from_exit";
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
    @Bind(R.id.email)
    EditText ed_email;
    @Bind(R.id.layout_email)
    TextInputLayout layoutEmail;


    @Override
    protected void initView() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loginng));
        boolean fromMain = getIntent().getBooleanExtra(FROM_EXIT_LOGIN,false);
        if (!TextUtils.isEmpty(SPUtils.getString(SPUtils.SESSION_TOKEN,null))
                && !fromMain){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            LoginActivity.this.finish();
        }
        ed_password.setOnEditorActionListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @OnClick({R.id.username_login_button, R.id.username_register_button, R.id.tv_jumpLogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.username_login_button:
                startLogin();
                break;
            case R.id.username_register_button:
                if (layoutEmail.getVisibility()!=View.VISIBLE){
                    layoutEmail.setVisibility(View.VISIBLE);
                }else{
                    doRegister();
                }
                break;
            case R.id.tv_jumpLogin:
                jumpLogin();
                break;
        }
    }


    private void doRegister() {
        ed_password.setError(null);
        ed_username.setError(null);
        ed_email.setError(null);

        final String user_name =  ed_username.getText().toString();
        final String pass_word =  ed_password.getText().toString();
        final String email =  ed_email.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email) || !email.contains("@")){
            ed_email.setError("邮箱地址有误");
            focusView = ed_email;
            cancel = true;
        }

        if(TextUtils.isEmpty(user_name)){
            ed_username.setError("用户名不可为空");
            focusView = ed_username;
            cancel = true;
        }

        if (!TextUtils.isEmpty(pass_word) && pass_word.length()<6) {
            ed_password.setError(this.getString(R.string.error_invalid_password));
            focusView = ed_password;
            cancel = true;
        }

        if (cancel){
            focusView.requestFocus();
        }else {
            doRegister(user_name,pass_word,email);
        }
    }


    private void jumpLogin() {
        ActivityUtils.jumpTo(this, MainActivity.class);
        this.finish();
    }


    private void doRegister(final String username, String password, String email) {
        showProgress(true);
        LeanCloudService.getInstance().getAPI()
                .register(RequestHelper.getRegisterBody(username,password,email))
                .compose(RxTransformer.INSTANCE.<RegisterResponse>transform(lifeCycleSubject))
                .map(new Func1<RegisterResponse, String>() {
                    @Override
                    public String call(RegisterResponse registerResponse) {
                        if (registerResponse == null){
                            return "注册失败";
                        }
                        SPUtils.putUserInfo(registerResponse,username);
                        return "注册成功";
                    }
                }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e(e.getMessage());
            }

            @Override
            public void onNext(String s) {
                if ("注册成功".equals(s)) {
                    // 注册成功，把用户对象赋值给当前用户 AVUser.getCurrentUser()
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    LoginActivity.this.finish();
                } else {
                    // 失败的原因可能有多种，常见的是用户名已经存在。
                    showProgress(false);
                    Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showError(int code) {
        String msg = null;
        switch (code) {
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

        if (TextUtils.isEmpty(username)) {
            ed_username.setError("用户名不可为空");
            focusView = ed_username;
            cancel = true;
        }

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            ed_password.setError(getString(R.string.error_invalid_password));
            focusView = ed_password;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {
                    if (e == null){
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        LoginActivity.this.finish();
                    }else {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
//            LeanCloudService.getInstance().getAPI()
//                    .login(RequestHelper.getLoginBody(username,password))
//                    .compose(RxTransformer.INSTANCE.<LoginResponse>transform(lifeCycleSubject))
//                    .map(new Func1<LoginResponse, String>() {
//                        @Override
//                        public String call(LoginResponse loginResponse) {
//                            if (loginResponse == null){
//                                return "登录失败";
//                            }
//                            SPUtils.putUserInfo(loginResponse);
//                            return "登录成功";
//                        }
//                    }).subscribe(new Subscriber<String>() {
//                @Override
//                public void onCompleted() {
//
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    LogUtils.e(e.getMessage());
//                    progressDialog.dismiss();
//                }
//
//                @Override
//                public void onNext(String s) {
//                    if ("登录成功".equals(s)) {
//                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                        LoginActivity.this.finish();
//                    } else {
//                        // 失败的原因可能有多种，常见的是用户名已经存在。
//                        showProgress(false);
//                        Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });

        }
    }

    private void showProgress(boolean show) {
        if (progressDialog == null) return;
        if (show) {
            progressDialog.show();
        } else {
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
