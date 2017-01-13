package com.appster.dentamatch.ui.profile.workexperience;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityWorkExpListBinding;
import com.appster.dentamatch.interfaces.YearSelectionListener;
import com.appster.dentamatch.network.request.auth.ReferenceRequest;
import com.appster.dentamatch.network.request.auth.WorkExpRequest;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetPicker;

import java.util.ArrayList;

/**
 * Created by virender on 05/01/17.
 */
public class WorkExpListActivity extends BaseActivity implements View.OnClickListener, YearSelectionListener {
    private ActivityWorkExpListBinding mBinder;
    private ArrayList<WorkExpRequest> workExpRequestList = new ArrayList<>();
    private String selectedJobtitle = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_work_exp_list);
        initViews();
        inflateExpList();
        hideKeyboard();
    }

    private void initViews() {
        mBinder.toolbarWorkExpList.ivToolBarLeft.setOnClickListener(this);
        mBinder.includeWorkExpList.tvExperinceWorkExp.setOnClickListener(this);
        mBinder.tvAddMoreExperience.setOnClickListener(this);
        mBinder.tvExperienceDelete.setOnClickListener(this);
        mBinder.tvAddMoreReference.setOnClickListener(this);
        mBinder.includeLayoutRefrence2.tvRefrenceDelete.setOnClickListener(this);

//        try {
//            mBinder.includeWorkExpList.tvExperinceWorkExp.setText(PreferenceUtil.getYear() + " " + getString(R.string.year) + " " + PreferenceUtil.getMonth() + " " + getString(R.string.month));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

        mBinder.toolbarWorkExpList.tvToolbarGeneralLeft.setText(getString(R.string.header_work_exp));
        setSpinnerData();
    }

    @Override
    public String getActivityName() {
        return null;
    }

    private void inflateExpList() {
        mBinder.layoutExpListInflater.removeAllViews();
        for (int i = 0; i < PreferenceUtil.getWorkExpList().size(); i++) {
            final View referenceView = getLayoutInflater().inflate(R.layout.layout_work_exp_header_item, mBinder.layoutExpListInflater, false);
            TextView tvJobTitle = (TextView) referenceView.findViewById(R.id.tv_title);
            TextView tvExp = (TextView) referenceView.findViewById(R.id.tv_exp);
            tvJobTitle.setText(PreferenceUtil.getWorkExpList().get(i).getJobTitle());
            tvExp.setText(PreferenceUtil.getWorkExpList().get(i).getExp());
            referenceView.setTag(i);
            referenceView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), ViewAndEditWorkExperienceActivity.class).putExtra(Constants.INTENT_KEY.POSITION, (Integer) referenceView.getTag()));
                }
            });
            mBinder.layoutExpListInflater.addView(referenceView);
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
            case R.id.tv_refrence_delete:
                mBinder.tvAddMoreReference.setVisibility(View.VISIBLE);

                mBinder.layoutReference2.setVisibility(View.GONE);
                mBinder.includeLayoutRefrence2.etOfficeReferenceEmail.setText("");
                mBinder.includeLayoutRefrence2.etOfficeReferenceMobile.setText("");
                mBinder.includeLayoutRefrence2.etOfficeReferenceName.setText("");
                break;
            case R.id.tv_add_more_reference:
                if (TextUtils.isEmpty(Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceName))) {
                    Utils.showToast(getApplicationContext(), getString(R.string.complete_reference));
                } else {
                    mBinder.includeLayoutRefrence2.tvRefrenceDelete.setVisibility(View.VISIBLE);
                    mBinder.tvAddMoreReference.setVisibility(View.GONE);
                    mBinder.includeLayoutRefrence2.tvRefrenceCount.setText(getString(R.string.reference2));

                    mBinder.layoutReference2.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_add_more_experience:
                if (checkValidation()) {
                    hideKeyboard();
                    PreferenceUtil.setWorkExpList(workExpRequestList);
                    clearAllExpField();
                    inflateExpList();
                }
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

    private boolean checkValidation() {
        workExpRequestList.clear();
        workExpRequestList = PreferenceUtil.getWorkExpList();
        WorkExpRequest workExpRequest = new WorkExpRequest();
        workExpRequest.setCity(Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeCity));
        workExpRequest.setOfficeName(Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeName));
        workExpRequest.setOfficeAddress(Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeAddress));
        workExpRequest.setExp(Utils.getStringFromEditText(mBinder.includeWorkExpList.tvExperinceWorkExp));
        workExpRequest.setJobTitle(selectedJobtitle);
        workExpRequest.setReference1Email(Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceEmail));
        workExpRequest.setReference1Mobile(Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceMobile));
        workExpRequest.setReference1Name(Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceName));
        workExpRequest.setReference2Email(Utils.getStringFromEditText(mBinder.includeLayoutRefrence2.etOfficeReferenceEmail));
        workExpRequest.setReference2Mobile(Utils.getStringFromEditText(mBinder.includeLayoutRefrence2.etOfficeReferenceMobile));
        workExpRequest.setReference2Name(Utils.getStringFromEditText(mBinder.includeLayoutRefrence2.etOfficeReferenceName));


        if (TextUtils.isEmpty(workExpRequest.getJobTitle())) {
            Utils.showToast(getApplicationContext(), getString(R.string.blank_job_title_alert));
            return false;
        }
        if (TextUtils.isEmpty(workExpRequest.getJobTitle())) {
            Utils.showToast(getApplicationContext(), getString(R.string.blank_year_alert));
            return false;
        }
        if (TextUtils.isEmpty(workExpRequest.getOfficeName())) {
            Utils.showToast(getApplicationContext(), getString(R.string.blank_office_name_alert));
            return false;
        }
        if (workExpRequest.getOfficeName().length() > Constants.DEFAULT_FIELD_LENGTH) {
            Utils.showToast(getApplicationContext(), getString(R.string.office_name_length_alert));
            return false;
        }

        if (TextUtils.isEmpty(workExpRequest.getOfficeAddress())) {
            Utils.showToast(getApplicationContext(), getString(R.string.blank_office_addrerss_alert));
            return false;
        }
        if (workExpRequest.getOfficeAddress().length() > Constants.DEFAULT_FIELD_LENGTH) {
            Utils.showToast(getApplicationContext(), getString(R.string.office_address_length_alert));
            return false;
        }
        if (TextUtils.isEmpty(workExpRequest.getCity())) {
            Utils.showToast(getApplicationContext(), getString(R.string.blank_city_alert));
            return false;
        }
        if (workExpRequest.getCity().length() > Constants.DEFAULT_FIELD_LENGTH) {
            Utils.showToast(getApplicationContext(), getString(R.string.city_length_alert));
            return false;
        }
        if (TextUtils.isEmpty(workExpRequest.getReference1Name())) {
            Utils.showToast(getApplicationContext(), getString(R.string.blank_refrence_name_alert));
            return false;
        }

        if (!TextUtils.isEmpty(workExpRequest.getReference1Email()) && !android.util.Patterns.EMAIL_ADDRESS.matcher(workExpRequest.getReference1Email()).matches()) {
            Utils.showToast(getApplicationContext(), getString(R.string.valid_email_alert));
            return false;
        }
        if (mBinder.layoutReference2.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(workExpRequest.getReference2Name())) {
                Utils.showToast(getApplicationContext(), getString(R.string.blank_refrence_name_alert));
                return false;
            }
            if (!TextUtils.isEmpty(workExpRequest.getReference1Email()) && !android.util.Patterns.EMAIL_ADDRESS.matcher(workExpRequest.getReference2Email()).matches()) {
                Utils.showToast(getApplicationContext(), getString(R.string.valid_email_alert));
                return false;
            }
        }


        workExpRequestList.add(workExpRequest);
        return true;
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
        mBinder.includeWorkExpList.spinnerJobTitleWorkExp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedJobtitle = PreferenceUtil.getJobTitleList().get(i).getJobTitle();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void clearAllExpField() {
        mBinder.includeWorkExpList.tvExperinceWorkExp.setText("");
        mBinder.includeWorkExpList.etOfficeAddress.setText("");
        mBinder.includeWorkExpList.etOfficeCity.setText("");
        mBinder.includeWorkExpList.etOfficeName.setText("");
        mBinder.includeLayoutRefrence1.etOfficeReferenceEmail.setText("");
        mBinder.includeLayoutRefrence1.etOfficeReferenceMobile.setText("");
        mBinder.includeLayoutRefrence1.etOfficeReferenceName.setText("");
        mBinder.includeLayoutRefrence2.etOfficeReferenceEmail.setText("");
        mBinder.includeLayoutRefrence2.etOfficeReferenceMobile.setText("");
        mBinder.includeLayoutRefrence2.etOfficeReferenceName.setText("");
        mBinder.layoutReference2.setVisibility(View.GONE);

    }

    @Override
    public void onExperienceSection(int year, int month) {
        mBinder.includeWorkExpList.tvExperinceWorkExp.setText(year + " " + getString(R.string.year) + " " + month + " " + getString(R.string.month));
//        PreferenceUtil.setMonth(month);
//        PreferenceUtil.setYear(year);
    }

    @Override
    protected void onResume() {
        super.onResume();
        clearAllExpField();
        inflateExpList();
    }
}
