package com.zyascend.NoBoring.utils.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.zyascend.NoBoring.R;

/**
 *
 * Created by Administrator on 2017/3/7.
 */

public class RegisterDialog extends Dialog {
    private static final String TAG = "RegisterDialog";
    private Context mContext;
    private AutoCompleteTextView userName;
    private EditText password;
    private Button dismiss,sure;

    public RegisterDialog(Context context) {
        this(context,0);
    }

    public RegisterDialog(Context context, int themeResId) {
        super(context, R.style.registerDialog);
        Log.d(TAG, "RegisterDialog: ");
        this.mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext,R.layout.dialog_register,null);
        setContentView(view);
        setCanceledOnTouchOutside(false);

        userName = (AutoCompleteTextView) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.password);
        dismiss = (Button) view.findViewById(R.id.btn_dismiss);
        sure = (Button) view.findViewById(R.id.btn_sure);

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRegister();
            }
        });
    }

    private void doRegister() {
        password.setError(null);
        userName.setError(null);

        final String user_name =  userName.getText().toString();
        final String pass_word =  password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(user_name)){
            userName.setError("用户名不可为空");
            focusView = userName;
            cancel = true;
        }

        if (!TextUtils.isEmpty(pass_word) && pass_word.length()<6) {
            password.setError(mContext.getString(R.string.error_invalid_password));
            focusView = password;
            cancel = true;
        }

        if (cancel){
            focusView.requestFocus();
        }else {
            if (mListener!= null){
                mListener.onRegister(user_name,pass_word);
                dismiss();
            }
        }
    }


    @Override
    public void show() {
        super.show();

        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //获取屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        //设置高宽
        lp.width = (int) (screenWidth * 0.75);
//        lp.height = (int) (lp.width * 0.70);
        dialogWindow.setAttributes(lp);

    }

    public void setDialogListener(onDialogRegistered mListener) {
        this.mListener = mListener;
    }

    private onDialogRegistered mListener;


    public interface onDialogRegistered{
        void onRegister(String username, String passWord);
    }
}
