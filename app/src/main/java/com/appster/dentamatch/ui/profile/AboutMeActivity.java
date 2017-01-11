package com.appster.dentamatch.ui.profile;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityAboutMeBinding;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.NetworkMonitor;
import com.appster.dentamatch.util.Utils;

/**
 * Created by virender on 10/01/17.
 */
public class AboutMeActivity extends BaseActivity {
    private ActivityAboutMeBinding mBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_about_me);
        initViews();


    }

    private void initViews() {
        mBinder.layoutIncludeProfileHeader.tvTitle.setText(getString(R.string.about_me_title));
        mBinder.layoutIncludeProfileHeader.progressBar.setProgress(100);
    }

    @Override
    public String getActivityName() {
        return null;
    }
}
