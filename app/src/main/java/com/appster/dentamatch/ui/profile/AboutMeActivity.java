package com.appster.dentamatch.ui.profile;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityAboutMeBinding;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.NetworkMonitor;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.squareup.picasso.Picasso;

/**
 * Created by virender on 10/01/17.
 */
public class AboutMeActivity extends BaseActivity implements View.OnClickListener {
    private ActivityAboutMeBinding mBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_about_me);
        initViews();


    }

    private void initViews() {
        mBinder.toolbarAboutMe.tvToolbarGeneralLeft.setText(getString(R.string.header_about_me));
        mBinder.layoutIncludeProfileHeader.tvTitle.setText(getString(R.string.about_me_title));

        if (!TextUtils.isEmpty(PreferenceUtil.getProfileImagePath())) {
            Picasso.with(getApplicationContext()).load(PreferenceUtil.getProfileImagePath()).centerCrop().resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN).placeholder(R.drawable.profile_pic_placeholder).into(mBinder.layoutIncludeProfileHeader.ivProfileIcon);

        }
        mBinder.layoutIncludeProfileHeader.progressBar.setProgress(100);
        mBinder.toolbarAboutMe.ivToolBarLeft.setOnClickListener(this);
    }

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_tool_bar_left:
                onBackPressed();
                break;
        }

    }
}
