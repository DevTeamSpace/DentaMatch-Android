package com.appster.dentamatch.ui.profile.workexperience;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityWorkExpListBinding;
import com.appster.dentamatch.interfaces.YearSelectionListener;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetPicker;

/**
 * Created by virender on 05/01/17.
 */
public class WorkExpListActivity extends BaseActivity implements View.OnClickListener, YearSelectionListener {
    private ActivityWorkExpListBinding mBinder;
    private int count=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_work_exp_list);
        initViews();
        inflateExpList();
    }

    private void initViews() {
        mBinder.toolbarWorkExpList.ivToolBarLeft.setOnClickListener(this);
        mBinder.includeWorkExpList.tvExperinceWorkExp.setOnClickListener(this);
        mBinder.tvExperienceDelete.setOnClickListener(this);
        mBinder.tvAddMoreReference.setOnClickListener(this);

        try {
            mBinder.includeWorkExpList.tvExperinceWorkExp.setText(PreferenceUtil.getYear() + " " + getString(R.string.year) + " " + PreferenceUtil.getMonth() + " " + getString(R.string.month));
        }catch (Exception ex) {
            ex.printStackTrace();
        }

        mBinder.toolbarWorkExpList.tvToolbarGeneralLeft.setText(getString(R.string.header_work_exp));
        setSpinnerData();
    }

    @Override
    public String getActivityName() {
        return null;
    }

    private void inflateExpList() {
        mBinder.layoutExpListInflater.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < 3; i++) {
            final View refrenceView = getLayoutInflater().inflate(R.layout.layout_work_exp_header_item, mBinder.layoutExpListInflater, false);
            refrenceView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), ViewAndEditWorkExperienceActivity.class));
                }
            });
            mBinder.layoutExpListInflater.addView(refrenceView);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_tool_bar_left:
                hideKeyboard();
                onBackPressed();
                break;
            case R.id.tv_experience_delete:
                hideKeyboard();
                clearAllExpField();
                break;
            case R.id.tv_add_more_reference:
                count++;
                inflateRefrence();
                break;
            case R.id.tv_experince_work_exp:
                int year = 0, month = 0;
                if (!TextUtils.isEmpty(mBinder.includeWorkExpList.tvExperinceWorkExp.getText().toString())) {
                    String split[] = mBinder.includeWorkExpList.tvExperinceWorkExp.getText().toString().split(" ");
                    year = Integer.parseInt(split[0]);
                    month = Integer.parseInt(split[2]);
                }
                new BottomSheetPicker(this, this, year, month);

                break;
        }
    }

    private void setSpinnerData() {
        String title[] = new String[PreferenceUtil.getJobTitleList().size()];

        for (int i = 0; i < PreferenceUtil.getJobTitleList().size(); i++) {
            title[i] = PreferenceUtil.getJobTitleList().get(i).getJobTitle();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(WorkExpListActivity.this,
                android.R.layout.simple_dropdown_item_1line, title);
//        MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner)
//                findViewById(R.id.android_material_design_spinner);
        mBinder.includeWorkExpList.spinnerJobTitleWorkExp.setPrompt(getString(R.string.lable_job_title));
        mBinder.includeWorkExpList.spinnerJobTitleWorkExp.setAdapter(arrayAdapter);
    }
    private void inflateRefrence() {
        mBinder.layoutRefrenceInfalter.removeAllViews();
        for (int i = 0; i < count; i++) {
            final View refrenceView = getLayoutInflater().inflate(R.layout.layout_reference, mBinder.layoutRefrenceInfalter, false);
            TextView tvRefrenceCount = (TextView) refrenceView.findViewById(R.id.tv_refrence_count);
            TextView tvRefrenceDlt = (TextView) refrenceView.findViewById(R.id.tv_refrence_delete);
            tvRefrenceCount.setText(getString(R.string.reference) + " " + (i+1));
            tvRefrenceCount.setVisibility(View.VISIBLE);
            tvRefrenceDlt.setVisibility(View.VISIBLE);
            tvRefrenceDlt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    count--;
                    inflateRefrence();

                }
            });
// number.setTag(i);
//            number.setText(Integer.toString(i));
            mBinder.layoutRefrenceInfalter.addView(refrenceView);
        }
    }
    private void clearAllExpField() {
        mBinder.includeWorkExpList.tvExperinceWorkExp.setText("");
        mBinder.includeWorkExpList.etOfficeAddress.setText("");
        mBinder.includeWorkExpList.etOfficeCity.setText("");
        mBinder.includeWorkExpList.etOfficeName.setText("");
        mBinder.includeLayoutRefrence.etOfficeReferenceEmail.setText("");
        mBinder.includeLayoutRefrence.etOfficeReferenceMobile.setText("");
        mBinder.includeLayoutRefrence.etOfficeReferenceMobile.setText("");
//        mBinder.re.etOfficeName.setText("");
        mBinder.layoutRefrenceInfalter.removeAllViews();
        count=0;

    }

    @Override
    public void onExperienceSection(int year, int month) {
        mBinder.includeWorkExpList.tvExperinceWorkExp.setText(year + " " + getString(R.string.year) + " " + month + " " + getString(R.string.month));
        PreferenceUtil.setMonth(month);
        PreferenceUtil.setYear(year);
    }
}
