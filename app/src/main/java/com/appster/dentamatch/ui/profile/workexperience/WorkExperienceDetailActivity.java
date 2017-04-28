package com.appster.dentamatch.ui.profile.workexperience;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityWorkExperienceDetailBinding;
import com.appster.dentamatch.interfaces.YearSelectionListener;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.workexp.WorkExpRequest;
import com.appster.dentamatch.network.response.workexp.WorkExpResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.profile.affiliation.AffiliationActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.util.WorkExpValidationUtil;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetPicker;

import retrofit2.Call;

/**
 * Created by virender on 05/01/17.
 */
public class WorkExperienceDetailActivity extends BaseActivity implements View.OnClickListener, YearSelectionListener {
    private ActivityWorkExperienceDetailBinding mBinder;
    private String selectedJobTitle = "";
    private int expMonth = 0, jobTitleId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_work_experience_detail);
        initViews();
    }

    private void initViews() {
        mBinder.toolbarWorkExpDetail.tvToolbarGeneralLeft.setText(getString(R.string.header_work_exp));
        mBinder.tvAddMoreReference.setOnClickListener(this);
        mBinder.tvAddMoreExperience.setOnClickListener(this);
        mBinder.btnNextDetailWorkExp.setOnClickListener(this);
        mBinder.includeLayoutWorkExp.tvExperienceWorkExp.setOnClickListener(this);
        mBinder.toolbarWorkExpDetail.ivToolBarLeft.setOnClickListener(this);
        mBinder.includeReference2.tvReferenceDelete.setOnClickListener(this);

        try {
            String yearLabel = "", monthLabel = "";

            if(PreferenceUtil.getYear() == 1){
                yearLabel = getString(R.string.txt_single_year);
            }else{
                yearLabel = getString(R.string.txt_multiple_years);
            }

            if(PreferenceUtil.getMonth() == 1){
                monthLabel = getString(R.string.txt_single_month);
            }else {
                monthLabel = getString(R.string.txt_multiple_months);
            }

            String workExp = String.valueOf(PreferenceUtil.getYear())
                    .concat(" ")
                    .concat(yearLabel)
                    .concat(" ")
                    .concat(String.valueOf(PreferenceUtil.getMonth()))
                    .concat(" ")
                    .concat(monthLabel);

            mBinder.includeLayoutWorkExp.tvExperienceWorkExp.setText(workExp);
            expMonth = PreferenceUtil.getYear() * 12 + PreferenceUtil.getMonth();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        mBinder.includeLayoutWorkExp.etOfficeName.setText(PreferenceUtil.getOfficeName());
        mBinder.includeLayoutWorkExp.etOfficeAddress.requestFocus();
    }


    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add_more_reference:

                if (TextUtils.isEmpty(Utils.getStringFromEditText(mBinder.includeReference1.etOfficeReferenceName))) {
                    Utils.showToast(getApplicationContext(), getString(R.string.complete_reference));
                } else {
                    mBinder.includeReference2.tvReferenceDelete.setVisibility(View.VISIBLE);
                    mBinder.tvAddMoreReference.setVisibility(View.GONE);
                    mBinder.includeReference2.tvReferenceCount.setText(getString(R.string.reference2));
                    mBinder.layoutReference2.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.iv_tool_bar_left:
                onBackPressed();
                break;

            case R.id.tv_reference_delete:
                mBinder.tvAddMoreReference.setVisibility(View.VISIBLE);
                mBinder.layoutReference2.setVisibility(View.GONE);
                break;

            case R.id.tv_add_more_experience:
                boolean isMoveForward = false;
//                isMoveForward = WorkExpValidationUtil.checkValidation(mBinder.layoutReference2.getVisibility(),
//                        selectedJobTitle,
//                        expMonth,
//                        Utils.getStringFromEditText(mBinder.includeLayoutWorkExp.etOfficeName),
//                        Utils.getStringFromEditText(mBinder.includeLayoutWorkExp.etOfficeAddress),
//                        Utils.getStringFromEditText(mBinder.includeLayoutWorkExp.etOfficeCity),
//                        Utils.getStringFromEditText(mBinder.includeReference1.etOfficeReferenceName),
//                        Utils.getStringFromEditText(mBinder.includeReference1.etOfficeReferenceEmail),
//                        Utils.getStringFromEditText(mBinder.includeRefrence2.etOfficeReferenceEmail),
//                        Utils.getStringFromEditText(mBinder.includeRefrence2.etOfficeReferenceName));

                if (isMoveForward) {
                    hideKeyboard();
                    callAddExpApi(WorkExpValidationUtil.prepareWorkExpRequest(mBinder.layoutReference2.getVisibility(),
                            Constants.APIS.ACTION_ADD,
                            jobTitleId,
                            expMonth,
                            Utils.getStringFromEditText(mBinder.includeLayoutWorkExp.etOfficeName),
                            Utils.getStringFromEditText(mBinder.includeLayoutWorkExp.etOfficeAddress),
                            Utils.getStringFromEditText(mBinder.includeLayoutWorkExp.etOfficeCity),
                            Utils.getStringFromEditText(mBinder.includeReference1.etOfficeReferenceName),
                            Utils.getStringFromEditText(mBinder.includeReference1.etOfficeReferenceMobile),
                            Utils.getStringFromEditText(mBinder.includeReference1.etOfficeReferenceEmail),
                            Utils.getStringFromEditText(mBinder.includeReference2.etOfficeReferenceEmail),
                            Utils.getStringFromEditText(mBinder.includeReference2.etOfficeReferenceName),
                            Utils.getStringFromEditText(mBinder.includeReference2.etOfficeReferenceMobile)));
                }

                break;

            case R.id.tv_experience_work_exp:
                int year = 0, month = 0;
                if (!TextUtils.isEmpty(mBinder.includeLayoutWorkExp.tvExperienceWorkExp.getText().toString())) {
                    String split[] = mBinder.includeLayoutWorkExp.tvExperienceWorkExp.getText().toString().split(" ");
                    year = Integer.parseInt(split[0]);
                    month = Integer.parseInt(split[2]);
                }

                new BottomSheetPicker(this, this, year, month);
                break;

            case R.id.btn_next_detail_work_exp:
                Utils.showToast(WorkExperienceDetailActivity.this, "New feature will introduce soon");
                startActivity(new Intent(this, AffiliationActivity.class));
                break;

            default:
                break;
        }

    }

    @Override
    public void onExperienceSection(int year, int month) {
        String yearLabel = "", monthLabel = "";

        if(year == 1){
            yearLabel = getString(R.string.txt_single_year);
        }else{
            yearLabel = getString(R.string.txt_multiple_years);
        }

        if(month == 1){
            monthLabel = getString(R.string.txt_single_month);
        }else {
            monthLabel = getString(R.string.txt_multiple_months);
        }

            String workExp =  String.valueOf(year)
                    .concat(" ")
                    .concat(yearLabel)
                    .concat(" ")
                    .concat(String.valueOf(month))
                    .concat(" ")
                    .concat(monthLabel);

        mBinder.includeLayoutWorkExp.tvExperienceWorkExp.setText(workExp);
        expMonth = year * 12 + month;
    }

    private void callAddExpApi(WorkExpRequest workExpRequest) {
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.addWorkExp(workExpRequest).enqueue(new BaseCallback<WorkExpResponse>(WorkExperienceDetailActivity.this) {
            @Override
            public void onSuccess(WorkExpResponse response) {
                if (response.getStatus() == 1) {
                    Utils.showToast(getApplicationContext(), response.getMessage());
                    startActivity(new Intent(getApplicationContext(), WorkExpListActivity.class));
                    finish();
                } else {
                    Utils.showToast(getApplicationContext(), response.getMessage());
                }
            }

            @Override
            public void onFail(Call<WorkExpResponse> call, BaseResponse baseResponse) {
                Utils.showToast(getApplicationContext(), baseResponse.getMessage());
            }
        });

    }


}
