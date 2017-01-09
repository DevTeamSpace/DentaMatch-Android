package com.appster.dentamatch.ui.profile.workexperience;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityViewAndWditWorkExperienceBinding;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;

/**
 * Created by virender on 04/01/17.
 */
public class ViewAndEditWorkExperienceActivity extends BaseActivity {
    private ActivityViewAndWditWorkExperienceBinding mBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_view_and_wdit_work_experience);
        initViews();
    }

    private void initViews() {
        mBinder.toolbarWorkExpView.tvToolbarGeneralLeft.setText(getString(R.string.header_work_exp));
    }

    @Override
    public String getActivityName() {
        return null;
    }
}
