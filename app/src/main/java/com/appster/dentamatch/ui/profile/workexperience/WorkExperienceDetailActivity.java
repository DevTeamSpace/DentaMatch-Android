package com.appster.dentamatch.ui.profile.workexperience;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityWorkExperinceDetailBinding;
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
public class WorkExperienceDetailActivity extends BaseActivity implements View.OnClickListener, YearSelectionListener {
    private ActivityWorkExperinceDetailBinding mBinder;
    //AtzTextBinding mBinder;
    private int count = 0;
    private String selectedJobtitle = "";
    private ArrayList<ReferenceRequest> refrenceRequestArrayList = new ArrayList<>();
    private ArrayList<WorkExpRequest> workExpRequestList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_work_experince_detail);
        initViews();
    }

    private void initViews() {
        mBinder.toolbarWorkExpDetail.tvToolbarGeneralLeft.setText(getString(R.string.header_work_exp));
        mBinder.tvAddMoreReference.setOnClickListener(this);
        mBinder.tvAddMoreExperience.setOnClickListener(this);
        mBinder.btnNextDetailWorkExp.setOnClickListener(this);
        mBinder.includeLayoutWorkExp.tvExperinceWorkExp.setOnClickListener(this);
        mBinder.toolbarWorkExpDetail.ivToolBarLeft.setOnClickListener(this);
        try {
            mBinder.includeLayoutWorkExp.tvExperinceWorkExp.setText(PreferenceUtil.getYear() + " " + getString(R.string.year) + " " + PreferenceUtil.getMonth() + " " + getString(R.string.month));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        mBinder.includeRefrence.etOfficeReferenceMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        setSpinnerData();
    }

    private void inflateRefrence() {
        mBinder.layoutRefrenceInfalter.removeAllViews();
        for (int i = 0; i < count; i++) {
            final View refrenceView = getLayoutInflater().inflate(R.layout.layout_reference, mBinder.layoutRefrenceInfalter, false);
            TextView tvRefrenceCount = (TextView) refrenceView.findViewById(R.id.tv_refrence_count);
            TextView tvRefrenceDlt = (TextView) refrenceView.findViewById(R.id.tv_refrence_delete);
            tvRefrenceCount.setText(getString(R.string.reference) + " " + (i + 1));
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

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.tv_add_more_reference:
            case R.id.tv_add_more_reference:

                if (TextUtils.isEmpty(Utils.getStringFromEditText(mBinder.includeRefrence.etOfficeReferenceEmail)) || TextUtils.isEmpty(Utils.getStringFromEditText(mBinder.includeRefrence.etOfficeReferenceMobile)) || TextUtils.isEmpty(Utils.getStringFromEditText(mBinder.includeRefrence.etOfficeReferenceName))) {
                    Utils.showToast(getApplicationContext(), getString(R.string.complete_reference));
                } else {

                    count++;
                    inflateRefrence();
                }


                break;
            case R.id.iv_tool_bar_left:
                onBackPressed();
                break;
            case R.id.tv_add_more_experience:

                if (checkValidation()) {
                    hideKeyboard();
                    PreferenceUtil.setWorkExpList(workExpRequestList);
                    startActivity(new Intent(getApplicationContext(), WorkExpListActivity.class));
                }

                break;
            case R.id.tv_experince_work_exp:
                int year = 0, month = 0;
                if (!TextUtils.isEmpty(mBinder.includeLayoutWorkExp.tvExperinceWorkExp.getText().toString())) {
                    String split[] = mBinder.includeLayoutWorkExp.tvExperinceWorkExp.getText().toString().split(" ");
                    year = Integer.parseInt(split[0]);
                    month = Integer.parseInt(split[2]);
                }
                new BottomSheetPicker(this, this, year, month);

                break;
            case R.id.btn_next_detail_work_exp:
                Utils.showToast(WorkExperienceDetailActivity.this, "New feature will introduce soon");
                break;
        }

    }

    private boolean checkValidation() {
        refrenceRequestArrayList.clear();
        WorkExpRequest workExpRequest = new WorkExpRequest();
        workExpRequest.setCity(Utils.getStringFromEditText(mBinder.includeLayoutWorkExp.etOfficeCity));
        workExpRequest.setOfficeName(Utils.getStringFromEditText(mBinder.includeLayoutWorkExp.etOfficeName));
        workExpRequest.setOfficeAddress(Utils.getStringFromEditText(mBinder.includeLayoutWorkExp.etOfficeAddress));
        workExpRequest.setExp(Utils.getStringFromEditText(mBinder.includeLayoutWorkExp.tvExperinceWorkExp));
        workExpRequest.setJobTitle(selectedJobtitle);
        ReferenceRequest refrenceRequest = new ReferenceRequest();
        refrenceRequest.setEmail(Utils.getStringFromEditText(mBinder.includeRefrence.etOfficeReferenceEmail));
        refrenceRequest.setPhoneNumber(Utils.getStringFromEditText(mBinder.includeRefrence.etOfficeReferenceMobile));
        refrenceRequest.setRefrenceName(Utils.getStringFromEditText(mBinder.includeRefrence.etOfficeReferenceName));
        refrenceRequestArrayList.add(refrenceRequest);
        workExpRequest.setReferenceAerialist(refrenceRequestArrayList);


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


        for (int i = 0; i < workExpRequest.getReferenceAerialist().size(); i++) {
            if (TextUtils.isEmpty(workExpRequest.getReferenceAerialist().get(i).getRefrenceName())) {
                Utils.showToast(getApplicationContext(), getString(R.string.blank_refrence_name_alert));
                return false;
            }
            if (workExpRequest.getReferenceAerialist().get(i).getRefrenceName().length() > Constants.DEFAULT_FIELD_LENGTH) {
                Utils.showToast(getApplicationContext(), getString(R.string.refrence_length_alert));
                return false;
            }
            if (TextUtils.isEmpty(workExpRequest.getReferenceAerialist().get(i).getPhoneNumber())) {
                Utils.showToast(getApplicationContext(), getString(R.string.blank_phone_number_alert));
                return false;
            }
            if (TextUtils.isEmpty(workExpRequest.getReferenceAerialist().get(i).getEmail())) {
                Utils.showToast(getApplicationContext(), getString(R.string.blank_email_alert));
                return false;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(workExpRequest.getReferenceAerialist().get(i).getEmail()).matches()) {
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
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(WorkExperienceDetailActivity.this,
                android.R.layout.simple_dropdown_item_1line, title);
//        MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner)
//                findViewById(R.id.android_material_design_spinner);
        mBinder.includeLayoutWorkExp.spinnerJobTitleWorkExp.setPrompt(getString(R.string.lable_job_title));
        mBinder.includeLayoutWorkExp.spinnerJobTitleWorkExp.setAdapter(arrayAdapter);
        mBinder.includeLayoutWorkExp.spinnerJobTitleWorkExp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
    public void onExperienceSection(int year, int month) {
        mBinder.includeLayoutWorkExp.tvExperinceWorkExp.setText(year + " " + getString(R.string.year) + " " + month + " " + getString(R.string.month));
//        PreferenceUtil.setMonth(month);
//        PreferenceUtil.setYear(year);
    }
}
