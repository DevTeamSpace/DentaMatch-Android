/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.ui.searchjob;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityProfileCompleteBinding;
import com.appster.dentamatch.base.BaseActivity;
import com.appster.dentamatch.ui.common.HomeActivity;
import com.appster.dentamatch.util.Constants;

/**
 * Created by abhaykant on 14/12/17.
 * User interface completion dialog .
 */

public class CompleteProfileDialogActivity extends BaseActivity implements View.OnClickListener {
    private ActivityProfileCompleteBinding mBinder;

    @Override
    public String getActivityName() {
        return CompleteProfileDialogActivity.class.getSimpleName();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_profile_complete);

        init();

    }

    private void init() {
        mBinder.ivClose.setOnClickListener(this);
        mBinder.btnCompleteProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                onBackPressed();
                break;

            case R.id.btn_complete_profile:
                startActivity(new Intent(CompleteProfileDialogActivity.this, HomeActivity.class)
                        .putExtra(Constants.EXTRA_FROM_JOB_DETAIL, true)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
        }
    }
}
