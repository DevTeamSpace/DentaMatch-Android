package com.appster.dentamatch.ui.profile.workexperience;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityViewAndWditWorkExperienceBinding;
import com.appster.dentamatch.interfaces.YearSelectionListener;
import com.appster.dentamatch.network.request.auth.WorkExpRequest;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;

import java.util.ArrayList;

/**
 * Created by virender on 04/01/17.
 */
public class ViewAndEditWorkExperienceActivity extends BaseActivity implements View.OnClickListener, YearSelectionListener {
    private ActivityViewAndWditWorkExperienceBinding mBinder;
    private int position;
    private int count = 0;
    private String selectedJobtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_view_and_wdit_work_experience);
        if (getIntent() != null) {
            position = getIntent().getIntExtra(Constants.INTENT_KEY.POSITION, 0);
        }
        initViews();
    }

    private void initViews() {
        mBinder.toolbarWorkExpView.ivToolBarLeft.setOnClickListener(this);
        mBinder.btnUpdate.setOnClickListener(this);
        mBinder.tvExperienceDelete.setOnClickListener(this);
        mBinder.tvAddMoreReference.setOnClickListener(this);
        mBinder.includeLayoutRefrence2.tvRefrenceDelete.setOnClickListener(this);
        mBinder.toolbarWorkExpView.tvToolbarGeneralLeft.setText(getString(R.string.header_work_exp));
        try {
            mBinder.layoutWorkExpViewEdit.tvExperinceWorkExp.setText(PreferenceUtil.getYear() + " " + getString(R.string.year) + " " + PreferenceUtil.getMonth() + " " + getString(R.string.month));

        } catch (Exception e) {
            e.printStackTrace();
        }

        setSpinnerData();
        setViewData();
        if (position == 0) {
            mBinder.tvExperienceDelete.setVisibility(View.GONE);
        } else {
            mBinder.tvExperienceDelete.setVisibility(View.VISIBLE);

        }
    }

    private void setViewData() {
        mBinder.layoutWorkExpViewEdit.etOfficeCity.setText(PreferenceUtil.getWorkExpList().get(position).getCity());
        mBinder.layoutWorkExpViewEdit.etOfficeName.setText(PreferenceUtil.getWorkExpList().get(position).getOfficeName());
        mBinder.layoutWorkExpViewEdit.tvExperinceWorkExp.setText(PreferenceUtil.getWorkExpList().get(position).getExp());
        mBinder.layoutWorkExpViewEdit.etOfficeAddress.setText(PreferenceUtil.getWorkExpList().get(position).getOfficeAddress());
        mBinder.includeLayoutRefrence1.etOfficeReferenceMobile.setText(PreferenceUtil.getWorkExpList().get(position).getReference1Mobile());
        mBinder.includeLayoutRefrence1.etOfficeReferenceName.setText(PreferenceUtil.getWorkExpList().get(position).getReference1Name());
        mBinder.includeLayoutRefrence1.etOfficeReferenceEmail.setText(PreferenceUtil.getWorkExpList().get(position).getReference1Email());
        if (!TextUtils.isEmpty(PreferenceUtil.getWorkExpList().get(position).getReference2Name())) {
            mBinder.layoutRefrence2.setVisibility(View.VISIBLE);
            mBinder.includeLayoutRefrence2.tvRefrenceCount.setText(getString(R.string.reference2));
            mBinder.includeLayoutRefrence2.etOfficeReferenceEmail.setText(PreferenceUtil.getWorkExpList().get(position).getReference2Email());
            mBinder.includeLayoutRefrence2.etOfficeReferenceMobile.setText(PreferenceUtil.getWorkExpList().get(position).getReference2Mobile());
            mBinder.includeLayoutRefrence2.etOfficeReferenceName.setText(PreferenceUtil.getWorkExpList().get(position).getReference2Name());

        }


    }

    private void setSpinnerData() {
        String title[] = new String[PreferenceUtil.getJobTitleList().size()];

        for (int i = 0; i < PreferenceUtil.getJobTitleList().size(); i++) {
            title[i] = PreferenceUtil.getJobTitleList().get(i).getJobTitle();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ViewAndEditWorkExperienceActivity.this,
                android.R.layout.simple_dropdown_item_1line, title);
        mBinder.layoutWorkExpViewEdit.spinnerJobTitleWorkExp.setAdapter(arrayAdapter);
        mBinder.layoutWorkExpViewEdit.spinnerJobTitleWorkExp.setPrompt(getString(R.string.lable_job_title));
        mBinder.layoutWorkExpViewEdit.spinnerJobTitleWorkExp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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
            case R.id.iv_tool_bar_left:
                hideKeyboard();
                onBackPressed();
                break;
            case R.id.tv_experience_delete:
                if (position != 0) {
                    ArrayList<WorkExpRequest> list = PreferenceUtil.getWorkExpList();
                    list.remove(position);
                    PreferenceUtil.setWorkExpList(list);
                    Utils.showToast(getApplicationContext(), "size is--" + PreferenceUtil.getWorkExpList());
                    finish();

                }
                break;
            case R.id.btn_update:
                if (checkValidation()) {
                    finish();
                }
                break;
//            case R.id.tv_experince_work_exp:
//                if (checkValidation()) {
//                    finish();
//                }
//                break;
            case R.id.tv_refrence_delete:
                mBinder.tvAddMoreReference.setVisibility(View.VISIBLE);

                mBinder.layoutRefrence2.setVisibility(View.GONE);
                mBinder.includeLayoutRefrence2.etOfficeReferenceEmail.setText("");
                mBinder.includeLayoutRefrence2.etOfficeReferenceMobile.setText("");
                mBinder.includeLayoutRefrence2.etOfficeReferenceName.setText("");
                break;
            case R.id.tv_add_more_reference:
                if (TextUtils.isEmpty(Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceName))) {
                    Utils.showToast(getApplicationContext(), getString(R.string.complete_reference));
                } else {

//                    count++;
//                    inflateRefrence();
                    mBinder.includeLayoutRefrence2.tvRefrenceDelete.setVisibility(View.VISIBLE);
                    mBinder.tvAddMoreReference.setVisibility(View.GONE);
                    mBinder.includeLayoutRefrence2.tvRefrenceCount.setText(getString(R.string.reference2));

                    mBinder.layoutRefrence2.setVisibility(View.VISIBLE);
                }
                break;
        }
    }


    @Override
    public void onExperienceSection(int year, int month) {
        mBinder.layoutWorkExpViewEdit.tvExperinceWorkExp.setText(year + " " + getString(R.string.year) + " " + month + " " + getString(R.string.month));
//        PreferenceUtil.setMonth(month);
//        PreferenceUtil.setYear(year);
    }

    private boolean checkValidation() {
        WorkExpRequest workExpRequest = PreferenceUtil.getWorkExpList().get(position);
        workExpRequest.setCity(Utils.getStringFromEditText(mBinder.layoutWorkExpViewEdit.etOfficeCity));
        workExpRequest.setOfficeName(Utils.getStringFromEditText(mBinder.layoutWorkExpViewEdit.etOfficeName));
        workExpRequest.setOfficeAddress(Utils.getStringFromEditText(mBinder.layoutWorkExpViewEdit.etOfficeAddress));
        workExpRequest.setExp(Utils.getStringFromEditText(mBinder.layoutWorkExpViewEdit.tvExperinceWorkExp));
        workExpRequest.setJobTitle(selectedJobtitle);
        workExpRequest.setReference1Email(Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceEmail));
        workExpRequest.setReference1Mobile(Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceMobile));
        workExpRequest.setReference1Name(Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceName));
        workExpRequest.setReference2Mobile(Utils.getStringFromEditText(mBinder.includeLayoutRefrence2.etOfficeReferenceMobile));
        workExpRequest.setReference2Name(Utils.getStringFromEditText(mBinder.includeLayoutRefrence2.etOfficeReferenceName));
        workExpRequest.setReference2Email(Utils.getStringFromEditText(mBinder.includeLayoutRefrence2.etOfficeReferenceEmail));
//        workExpRequest.setReferenceAerialist(refrenceRequestArrayList);


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
        if (mBinder.layoutRefrence2.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(workExpRequest.getReference2Name())) {
                Utils.showToast(getApplicationContext(), getString(R.string.blank_refrence_name_alert));
                return false;
            }
            if (!TextUtils.isEmpty(workExpRequest.getReference1Email()) && !android.util.Patterns.EMAIL_ADDRESS.matcher(workExpRequest.getReference2Email()).matches()) {
                Utils.showToast(getApplicationContext(), getString(R.string.valid_email_alert));
                return false;
            }
        }

        ArrayList<WorkExpRequest> list = PreferenceUtil.getWorkExpList();
        list.remove(position);
        list.add(position, workExpRequest);
        PreferenceUtil.setWorkExpList(list);
//        PreferenceUtil.getWorkExpList().add(workExpRequest);

        return true;
    }
}
