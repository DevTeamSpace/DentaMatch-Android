package com.appster.dentamatch.ui.profile;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityWorkExperinceBinding;
import com.appster.dentamatch.ui.common.BaseActivity;

/**
 * Created by virender on 04/01/17.
 */
public class WorkExperienceActivity extends BaseActivity {
    //    private ActivityT mBinder;
    private ActivityWorkExperinceBinding mBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_work_experince);

    }

    @Override
    public String getActivityName() {
        return null;
    }
}
