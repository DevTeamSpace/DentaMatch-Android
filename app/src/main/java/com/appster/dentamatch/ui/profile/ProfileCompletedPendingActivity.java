package com.appster.dentamatch.ui.profile;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityProfileCompletedPendingBinding;
import com.appster.dentamatch.ui.common.BaseActivity;

/**
 * Created by zishan on 28/11/17.
 */

public class ProfileCompletedPendingActivity extends BaseActivity {
    private String TAG = "ProfileCompletedPendingActivity";
    private ActivityProfileCompletedPendingBinding activityProfileCompletedPendingBinding;
    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        activityProfileCompletedPendingBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile_completed_pending);
    }
}
