package com.appster.dentamatch.ui.profile.workexperience;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityWorkExperinceDetailBinding;
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
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.util.socialhelper.WorkExpValidationUtil;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetPicker;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by virender on 05/01/17.
 */
public class WorkExperienceDetailActivity extends BaseActivity implements View.OnClickListener, YearSelectionListener {
    private ActivityWorkExperinceDetailBinding mBinder;
    private String selectedJobtitle = "";
    private ArrayList<WorkExpRequest> workExpRequestList = new ArrayList<>();
    private int expMonth = 0, jobTitleId;

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
        mBinder.includeRefrence2.tvRefrenceDelete.setOnClickListener(this);
        try {
            mBinder.includeLayoutWorkExp.tvExperinceWorkExp.setText(PreferenceUtil.getYear() + " " + getString(R.string.year) + " " + PreferenceUtil.getMonth() + " " + getString(R.string.month));
            expMonth = PreferenceUtil.getYear() * 12 + PreferenceUtil.getMonth();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        mBinder.includeRefrence1.etOfficeReferenceMobile.addTextChangedListener(new TextWatcher() {
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

                if (TextUtils.isEmpty(Utils.getStringFromEditText(mBinder.includeRefrence1.etOfficeReferenceName))) {
                    Utils.showToast(getApplicationContext(), getString(R.string.complete_reference));
                } else {
                    mBinder.includeRefrence2.tvRefrenceDelete.setVisibility(View.VISIBLE);
                    mBinder.tvAddMoreReference.setVisibility(View.GONE);
                    mBinder.includeRefrence2.tvRefrenceCount.setText(getString(R.string.reference2));
                    mBinder.layoutReference2.setVisibility(View.VISIBLE);
                }


                break;
            case R.id.iv_tool_bar_left:
                onBackPressed();
                break;
            case R.id.tv_refrence_delete:
                mBinder.tvAddMoreReference.setVisibility(View.VISIBLE);
                mBinder.layoutReference2.setVisibility(View.GONE);
                break;
            case R.id.tv_add_more_experience:
                boolean isMoveForward = false;
                isMoveForward = WorkExpValidationUtil.checkValidation(mBinder.layoutReference2.getVisibility(), selectedJobtitle, expMonth,
                        Utils.getStringFromEditText(mBinder.includeLayoutWorkExp.etOfficeName),
                        Utils.getStringFromEditText(mBinder.includeLayoutWorkExp.etOfficeAddress),
                        Utils.getStringFromEditText(mBinder.includeLayoutWorkExp.etOfficeCity),
                        Utils.getStringFromEditText(mBinder.includeRefrence1.etOfficeReferenceName)
                        , Utils.getStringFromEditText(mBinder.includeRefrence1.etOfficeReferenceEmail),
                        Utils.getStringFromEditText(mBinder.includeRefrence2.etOfficeReferenceEmail),
                        Utils.getStringFromEditText(mBinder.includeRefrence2.etOfficeReferenceName),
                        Utils.getStringFromEditText(mBinder.includeRefrence1.etOfficeReferenceMobile),
                        Utils.getStringFromEditText(mBinder.includeRefrence2.etOfficeReferenceMobile));
                if (isMoveForward) {
                    hideKeyboard();
                    callAddExpApi(WorkExpValidationUtil.prepareWorkExpRequest(mBinder.layoutReference2.getVisibility(), Constants.APIS.ACTION_ADD, jobTitleId, expMonth,
                            Utils.getStringFromEditText(mBinder.includeLayoutWorkExp.etOfficeName), Utils.getStringFromEditText(mBinder.includeLayoutWorkExp.etOfficeAddress),
                            Utils.getStringFromEditText(mBinder.includeLayoutWorkExp.etOfficeCity), Utils.getStringFromEditText(mBinder.includeRefrence1.etOfficeReferenceName)
                            , Utils.getStringFromEditText(mBinder.includeRefrence1.etOfficeReferenceMobile), Utils.getStringFromEditText(mBinder.includeRefrence1.etOfficeReferenceEmail),
                            Utils.getStringFromEditText(mBinder.includeRefrence2.etOfficeReferenceEmail), Utils.getStringFromEditText(mBinder.includeRefrence2.etOfficeReferenceName),
                            Utils.getStringFromEditText(mBinder.includeRefrence2.etOfficeReferenceMobile)));

//                    startActivity(new Intent(getApplicationContext(), WorkExpListActivity.class));
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
                startActivity(new Intent(this, AffiliationActivity.class));
                break;
        }

    }





    @Override
    public void onExperienceSection(int year, int month) {
        mBinder.includeLayoutWorkExp.tvExperinceWorkExp.setText(year + " " + getString(R.string.year) + " " + month + " " + getString(R.string.month));
//        PreferenceUtil.setMonth(month);
//        PreferenceUtil.setYear(year);
        expMonth = year * 12 + month;

    }

    private void callAddExpApi(WorkExpRequest workExpRequest) {
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.addWorkExp(workExpRequest).enqueue(new BaseCallback<WorkExpResponse>(WorkExperienceDetailActivity.this) {
            @Override
            public void onSuccess(WorkExpResponse response) {
                LogUtils.LOGD(TAG, "onSuccess");
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
                LogUtils.LOGD(TAG, "onFail");
                Utils.showToast(getApplicationContext(), baseResponse.getMessage());
            }
        });

    }


}
