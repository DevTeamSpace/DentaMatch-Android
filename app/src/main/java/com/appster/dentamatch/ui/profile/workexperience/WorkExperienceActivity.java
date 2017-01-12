package com.appster.dentamatch.ui.profile.workexperience;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityWorkExperinceBinding;
import com.appster.dentamatch.interfaces.YearSelectionListener;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetPicker;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by virender on 04/01/17.
 */
public class WorkExperienceActivity extends BaseActivity implements View.OnClickListener, YearSelectionListener {
    //    private ActivityT mBinder;
    private ActivityWorkExperinceBinding mBinder;
    private String selectedJobtitle = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_work_experince);
        initViews();
    }

    private void initViews() {
        hideKeyboard();
        hideKeyboard(mBinder.etOfficeName);

        mBinder.toolbarWorkExp.tvToolbarGeneralLeft.setText(getString(R.string.header_work_exp));
        mBinder.progressBar.setProgress(45);
        mBinder.toolbarWorkExp.ivToolBarLeft.setOnClickListener(this);
        mBinder.btnNextWorkExp.setOnClickListener(this);
        mBinder.tvExperinceWorkExp.setOnClickListener(this);
        if (!TextUtils.isEmpty(PreferenceUtil.getProfileImagePath())) {
            Picasso.with(getApplicationContext()).load(PreferenceUtil.getProfileImagePath()).centerCrop().resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN).placeholder(R.drawable.profile_pic_placeholder).into(mBinder.createProfileIvProfileIcon);

        }
        setSpinnerData();
    }

    private void setSpinnerData() {
        String title[] = new String[PreferenceUtil.getJobTitleList().size()];

        for (int i = 0; i < PreferenceUtil.getJobTitleList().size(); i++) {
            title[i] = PreferenceUtil.getJobTitleList().get(i).getJobTitle();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(WorkExperienceActivity.this,
                android.R.layout.simple_dropdown_item_1line, title);
//        MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner)
//                findViewById(R.id.android_material_design_spinner);
        mBinder.spinnerJobTitleWorkExp.setPrompt(getString(R.string.lable_job_title));
        mBinder.spinnerJobTitleWorkExp.setAdapter(arrayAdapter);
        mBinder.spinnerJobTitleWorkExp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PreferenceUtil.setJobTitle(PreferenceUtil.getJobTitleList().get(i).getJobTitle());
                selectedJobtitle = PreferenceUtil.getJobTitleList().get(i).getJobTitle();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next_work_exp:
                if (checkValidation()) {
                    hideKeyboard();
                    startActivity(new Intent(this, WorkExperienceDetailActivity.class));
                }
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

    private boolean checkValidation() {
        if (TextUtils.isEmpty(selectedJobtitle)) {
            Utils.showToast(getApplicationContext(), getString(R.string.blank_job_title_alert));
            return false;
        }
        if (TextUtils.isEmpty(mBinder.tvExperinceWorkExp.getText().toString().trim())) {
            Utils.showToast(getApplicationContext(), getString(R.string.blank_year_alert));
            return false;
        }
        if (TextUtils.isEmpty(mBinder.etOfficeName.getText().toString().trim())) {
            Utils.showToast(getApplicationContext(), getString(R.string.blank_office_name_alert));
            return false;
        }
        if (mBinder.etOfficeName.getText().toString().trim().length() > Constants.DEFAULT_FIELD_LENGTH) {
            Utils.showToast(getApplicationContext(), getString(R.string.office_name_length_alert));
            return false;
        }

        return true;
    }

    @Override
    public void onExperienceSection(int year, int month) {
        mBinder.tvExperinceWorkExp.setText(year + " " + getString(R.string.year) + " " + month + " " + getString(R.string.month));
        PreferenceUtil.setMonth(month);
        PreferenceUtil.setYear(year);
    }
}
