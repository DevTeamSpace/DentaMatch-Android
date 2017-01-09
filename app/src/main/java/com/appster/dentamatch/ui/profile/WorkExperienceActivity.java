package com.appster.dentamatch.ui.profile;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityWorkExperinceBinding;
import com.appster.dentamatch.interfaces.YearSelectionListener;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.BottomSheetPicker;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by virender on 04/01/17.
 */
public class WorkExperienceActivity extends BaseActivity implements View.OnClickListener, YearSelectionListener {
    //    private ActivityT mBinder;
    private ActivityWorkExperinceBinding mBinder;
    private String profileImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_work_experince);
        if (getIntent() != null) {
            profileImagePath = getIntent().getStringExtra(Constants.INTENT_KEY.IMAGE_PATH);
//            fName = getIntent().getStringExtra(Constants.INTENT_KEY.F_NAME);
//            jobTitle = getIntent().getStringExtra(Constants.INTENT_KEY.JOB_TITLE);
        }
        initViews();
    }

    private void initViews() {
        hideKeyboard();
        hideKeyboard(mBinder.etOfficeName);

        mBinder.toolbarWorkExp.tvToolbarGeneralLeft.setText(getString(R.string.header_work_exp));

        mBinder.btnNextWorkExp.setOnClickListener(this);
        mBinder.tvExperinceWorkExp.setOnClickListener(this);
        if (!TextUtils.isEmpty(profileImagePath)) {
            Picasso.with(getApplicationContext()).load(new File(profileImagePath)).centerCrop().resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN).placeholder(R.drawable.profile_pic_placeholder).into(mBinder.createProfileIvProfileIcon);

        }
    }

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next_work_exp:
                startActivity(new Intent(this, WorkExperienceDetailActivity.class));
                break;
            case R.id.iv_tool_bar_left:
                onBackPressed();
                break;
            case R.id.tv_experince_work_exp:
                hideKeyboard();
                new BottomSheetPicker(this, this, 0, 0);
                break;
        }
    }

    @Override
    public void onExperienceSection(int year, int month) {
        mBinder.tvExperinceWorkExp.setText(year + " " + getString(R.string.year) + " " + month + " " + getString(R.string.month));
    }
}
