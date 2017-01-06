package com.appster.dentamatch.ui.auth;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActvityForgotPasswordBinding;
import com.appster.dentamatch.ui.common.BaseActivity;

/**
 * Created by virender on 30/12/16.
 */
public class ForgotPasswordActivity extends BaseActivity {
    private ActvityForgotPasswordBinding mBinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.actvity_forgot_password);
        initViews();
    }

    private void initViews() {
        mBinder.toolbarForgotPassword.tvToolbarGeneralLeft.setText(getString(R.string.header_forgot_password));
    }

    @Override
    public String getActivityName() {
        return null;
    }
}
