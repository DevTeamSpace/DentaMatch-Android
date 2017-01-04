package com.appster.dentamatch.ui.auth;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.ui.profile.CreateProfileActivity1;

/**
 * Created by virender on 13/12/16.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private ImageView ivRegisterPeg, ivLoginPeg;
    private LinearLayout layoutRegisterSelector, layoutLoginSelector, layoutOnlyRegister, layoutOnlyLogin;
    private TextView tvLogin, tvRegister, tvForgotPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initViews() {

        tvRegister = (TextView) findViewById(R.id.register_tv_register);
        tvLogin = (TextView) findViewById(R.id.login_tv_login);
        tvForgotPassword = (TextView) findViewById(R.id.login_tv_forgot_password);
        btnRegister = (Button) findViewById(R.id.login_btn_register);
        ivRegisterPeg = (ImageView) findViewById(R.id.register_iv_peg);
        ivLoginPeg = (ImageView) findViewById(R.id.login_iv_peg);
        layoutRegisterSelector = (LinearLayout) findViewById(R.id.login_view_register);
        layoutLoginSelector = (LinearLayout) findViewById(R.id.login_view_login);
        layoutOnlyLogin = (LinearLayout) findViewById(R.id.login_layout_login_view);
        layoutOnlyRegister = (LinearLayout) findViewById(R.id.login_layout_register_view);
        layoutLoginSelector.setOnClickListener(this);
        layoutRegisterSelector.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_view_login:
                showSelectedView(true);
                break;
            case R.id.login_view_register:
                showSelectedView(false);
                break;

            case R.id.login_btn_register:
                startActivity(new Intent(this, CreateProfileActivity1.class));
                break;
            case R.id.login_tv_forgot_password:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));

                break;
        }
    }

    private void showSelectedView(boolean isLogin) {
        if (isLogin) {
            layoutOnlyRegister.setVisibility(View.GONE);
            ivRegisterPeg.setVisibility(View.INVISIBLE);
            ivLoginPeg.setVisibility(View.VISIBLE);
            layoutOnlyLogin.setVisibility(View.VISIBLE);
        } else {
            layoutOnlyRegister.setVisibility(View.VISIBLE);
            ivRegisterPeg.setVisibility(View.VISIBLE);
            ivLoginPeg.setVisibility(View.INVISIBLE);
            layoutOnlyLogin.setVisibility(View.GONE);
        }
    }
}
