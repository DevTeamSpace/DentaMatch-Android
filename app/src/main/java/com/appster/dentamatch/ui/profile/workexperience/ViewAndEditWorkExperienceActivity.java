package com.appster.dentamatch.ui.profile.workexperience;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityViewAndWditWorkExperienceBinding;
import com.appster.dentamatch.interfaces.YearSelectionListener;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;

/**
 * Created by virender on 04/01/17.
 */
public class ViewAndEditWorkExperienceActivity extends BaseActivity implements View.OnClickListener, YearSelectionListener {
    private ActivityViewAndWditWorkExperienceBinding mBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_view_and_wdit_work_experience);
        initViews();
    }

    private void initViews() {
        mBinder.toolbarWorkExpView.ivToolBarLeft.setOnClickListener(this);
        mBinder.toolbarWorkExpView.tvToolbarGeneralLeft.setText(getString(R.string.header_work_exp));
        mBinder.layoutWorkExpViewEdit.tvExperinceWorkExp.setText(PreferenceUtil.getYear() + " " + getString(R.string.year) + " " + PreferenceUtil.getMonth() + " " + getString(R.string.month));

        setSpinnerData();
    }

    private void setSpinnerData() {
        String title[] = new String[PreferenceUtil.getJobTitleList().size()];

        for (int i = 0; i < PreferenceUtil.getJobTitleList().size(); i++) {
            title[i] = PreferenceUtil.getJobTitleList().get(i).getJobTitle();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ViewAndEditWorkExperienceActivity.this,
                android.R.layout.simple_dropdown_item_1line, title);
//        MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner)
//                findViewById(R.id.android_material_design_spinner);
        mBinder.layoutWorkExpViewEdit.spinnerJobTitleWorkExp.setPrompt(getString(R.string.lable_job_title));
        mBinder.layoutWorkExpViewEdit.spinnerJobTitleWorkExp.setAdapter(arrayAdapter);
    }

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_tool_bar_left:
                hideKeyboard();
                onBackPressed();
                break;
        }
    }

    @Override
    public void onExperienceSection(int year, int month) {
        mBinder.layoutWorkExpViewEdit.tvExperinceWorkExp.setText(year + " " + getString(R.string.year) + " " + month + " " + getString(R.string.month));
        PreferenceUtil.setMonth(month);
        PreferenceUtil.setYear(year);
    }
}
