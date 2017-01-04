package com.appster.dentamatch.ui.auth;

import android.os.Bundle;

import com.appster.dentamatch.R;
import com.appster.dentamatch.ui.common.BaseActivity;

/**
 * Created by virender on 30/12/16.
 */
public class ForgotPasswordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_forgot_password);
    }

    @Override
    public String getActivityName() {
        return null;
    }
}
