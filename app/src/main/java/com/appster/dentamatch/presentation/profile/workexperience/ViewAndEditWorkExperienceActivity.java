/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.profile.workexperience;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.base.BaseLoadingActivity;
import com.appster.dentamatch.databinding.ActivityViewAndWditWorkExperienceBinding;
import com.appster.dentamatch.eventbus.ProfileUpdatedEvent;
import com.appster.dentamatch.interfaces.JobTitleSelectionListener;
import com.appster.dentamatch.interfaces.YearSelectionListener;
import com.appster.dentamatch.base.BaseResponse;
import com.appster.dentamatch.network.request.workexp.WorkExpRequest;
import com.appster.dentamatch.network.response.profile.StateList;
import com.appster.dentamatch.network.response.workexp.WorkExpResponse;
import com.appster.dentamatch.network.response.workexp.WorkExpResponseData;
import com.appster.dentamatch.presentation.common.HomeActivity;
import com.appster.dentamatch.presentation.common.SearchStateActivity;
import com.appster.dentamatch.util.Alert;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.UsPhoneNumberFormat;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.util.WorkExpValidationUtil;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetJobTitle;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetPicker;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by virender on 04/01/17.
 * To inject activity reference.
 */
public class ViewAndEditWorkExperienceActivity extends BaseLoadingActivity<ViewAndEditWorkExperienceViewModel>
        implements View.OnClickListener, YearSelectionListener, JobTitleSelectionListener {

    private ActivityViewAndWditWorkExperienceBinding mBinder;
    private int position;
    private String selectedJobTitle;
    private ArrayList<WorkExpRequest> workExpList;
    private int expMonth, jobTitleId;
    private int mPrevSelStatePos;
    private ArrayList<StateList> mStateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_view_and_wdit_work_experience);
        initViews();
        viewModel.getAddWorkExperience().observe(this, this::onSuccessAddWorkExperience);
        viewModel.getDeleteWorkExperience().observe(this, this::onSuccessDeleteWorkExperience);
    }

    private void onSuccessDeleteWorkExperience(@Nullable BaseResponse response) {
        if (response != null) {
            workExpList.remove(position);
            Intent intent = new Intent();
            intent.putExtra(Constants.INTENT_KEY.DATA, workExpList);
            setResult(RESULT_OK, intent);
            EventBus.getDefault().post(new ProfileUpdatedEvent(true));
            finish();
        }
    }

    private void onSuccessAddWorkExperience(@Nullable WorkExpResponse response) {
        if (response != null) {
            WorkExpResponseData data = response.getWorkExpResponseData();
            workExpList.remove(position);
            data.getSaveList().get(0).setJobTitleName(selectedJobTitle);
            workExpList.add(position, data.getSaveList().get(0));
            Intent intent = new Intent();
            intent.putExtra(Constants.INTENT_KEY.DATA, workExpList);
            setResult(Constants.REQUEST_CODE.REQUEST_CODE_PASS_INTENT, intent);
            EventBus.getDefault().post(new ProfileUpdatedEvent(true));
            finish();
        }
    }

    private void initViews() {
        mBinder.toolbarWorkExpView.ivToolBarLeft.setOnClickListener(this);
        mBinder.btnUpdate.setOnClickListener(this);
        mBinder.tvExperienceDelete.setOnClickListener(this);
        mBinder.tvAddMoreReference.setOnClickListener(this);
        mBinder.includeLayoutReference2.tvReferenceDelete.setOnClickListener(this);
        mBinder.layoutWorkExpViewEdit.tvExperienceWorkExp.setOnClickListener(this);
        mBinder.layoutWorkExpViewEdit.etJobTitle.setOnClickListener(this);
        mBinder.toolbarWorkExpView.tvToolbarGeneralLeft.setText(getString(R.string.header_edit_profile));
        mBinder.relSave.setOnClickListener(this);

        if (getIntent() != null) {
            position = getIntent().getIntExtra(Constants.INTENT_KEY.POSITION, 0);
            workExpList = getIntent().getParcelableArrayListExtra(Constants.INTENT_KEY.DATA);
        }

        setViewData();
        mBinder.tvExperienceDelete.setVisibility(View.VISIBLE);
        hideKeyboard();

        UsPhoneNumberFormat addLineNumberFormatter = new UsPhoneNumberFormat(
                new WeakReference<>(mBinder.includeLayoutReference1.etOfficeReferenceMobile));
        mBinder.includeLayoutReference1.etOfficeReferenceMobile.addTextChangedListener(addLineNumberFormatter);
        UsPhoneNumberFormat addLineNumberFormatter2 = new UsPhoneNumberFormat(
                new WeakReference<>(mBinder.includeLayoutReference2.etOfficeReferenceMobile));
        mBinder.includeLayoutReference2.etOfficeReferenceMobile.addTextChangedListener(addLineNumberFormatter2);

        mBinder.layoutWorkExpViewEdit.etOfficeState.setFocusableInTouchMode(false);
        mBinder.layoutWorkExpViewEdit.etOfficeState.setCursorVisible(false);
        mBinder.layoutWorkExpViewEdit.etOfficeState.setOnClickListener(this);
    }

    private void setViewData() {
        selectedJobTitle = workExpList.get(position).getJobTitleName();
        jobTitleId = workExpList.get(position).getJobTitleId();
        expMonth = workExpList.get(position).getMonthsOfExpereince();
        mBinder.layoutWorkExpViewEdit.etOfficeCity.setText(workExpList.get(position).getCity());
        mBinder.layoutWorkExpViewEdit.etOfficeState.setText(workExpList.get(position).getState());
        mBinder.layoutWorkExpViewEdit.etOfficeName.setText(workExpList.get(position).getOfficeName());
        mBinder.layoutWorkExpViewEdit.etJobTitle.setText(workExpList.get(position).getJobTitleName());
        mBinder.layoutWorkExpViewEdit.tvExperienceWorkExp.setText(Utils.getExpYears(workExpList.get(position).getMonthsOfExpereince()));
        mBinder.layoutWorkExpViewEdit.etOfficeAddress.setText(workExpList.get(position).getOfficeAddress());
        mBinder.includeLayoutReference1.etOfficeReferenceMobile.setText(workExpList.get(position).getReference1Mobile());
        mBinder.includeLayoutReference1.etOfficeReferenceName.setText(workExpList.get(position).getReference1Name());
        mBinder.includeLayoutReference1.etOfficeReferenceEmail.setText(workExpList.get(position).getReference1Email());

        if (!TextUtils.isEmpty(workExpList.get(position).getReference2Name()) ||
                !TextUtils.isEmpty(workExpList.get(position).getReference2Email()) ||
                !TextUtils.isEmpty(workExpList.get(position).getReference2Mobile())) {
            mBinder.layoutReference2.setVisibility(View.VISIBLE);
            mBinder.tvAddMoreReference.setVisibility(View.GONE);
            mBinder.includeLayoutReference2.tvReferenceCount.setText(getString(R.string.reference2));
            mBinder.includeLayoutReference2.tvReferenceDelete.setVisibility(View.VISIBLE);
            mBinder.includeLayoutReference2.etOfficeReferenceEmail.setText(workExpList.get(position).getReference2Email());
            mBinder.includeLayoutReference2.etOfficeReferenceMobile.setText(workExpList.get(position).getReference2Mobile());
            mBinder.includeLayoutReference2.etOfficeReferenceName.setText(workExpList.get(position).getReference2Name());

        } else {
            mBinder.tvAddMoreReference.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_tool_bar_left:
                hideKeyboard();
                //onBackPressed();

                startActivity(new Intent(ViewAndEditWorkExperienceActivity.this, HomeActivity.class)
                        .putExtra(Constants.EXTRA_FROM_JOB_DETAIL, true)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

            case R.id.tv_experience_delete:
                Alert.createYesNoAlert(ViewAndEditWorkExperienceActivity.this,
                        getString(R.string.txt_ok),
                        getString(R.string.txt_cancel),
                        "", getString(R.string.msg_delete_exp_warning), new Alert.OnAlertClickListener() {
                            @Override
                            public void onPositive(DialogInterface dialog) {
                                callDeleteApi();
                            }

                            @Override
                            public void onNegative(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                break;
            case R.id.et_job_title:
                hideKeyboard();
                new BottomSheetJobTitle(ViewAndEditWorkExperienceActivity.this, this, 0);
                break;
            case R.id.btn_update:
                updateExp();

                break;
            case R.id.tv_experience_work_exp:
                int year = 0, month = 0;
                if (!TextUtils.isEmpty(mBinder.layoutWorkExpViewEdit.tvExperienceWorkExp.getText().toString())) {
                    String split[] = mBinder.layoutWorkExpViewEdit.tvExperienceWorkExp.getText().toString().split(" ");
                    if (split.length == 4) {
                        year = Integer.parseInt(split[0]);
                        month = Integer.parseInt(split[2]);
                    } else {
                        if (split.length == 2 && (split[1].equals(getString(R.string.txt_single_month)) || split[1].equals(getString(R.string.txt_multiple_months)))) {
                            month = Integer.parseInt(split[0]);
                        } else {
                            year = Integer.parseInt(split[0]);
                        }
                    }

                }
                new BottomSheetPicker(this, this, year, month);
                break;
            case R.id.tv_reference_delete:
                mBinder.tvAddMoreReference.setVisibility(View.VISIBLE);
                mBinder.layoutReference2.setVisibility(View.GONE);
                mBinder.includeLayoutReference2.etOfficeReferenceEmail.setText("");
                mBinder.includeLayoutReference2.etOfficeReferenceMobile.setText("");
                mBinder.includeLayoutReference2.etOfficeReferenceName.setText("");
                break;
            case R.id.tv_add_more_reference:
                if (TextUtils.isEmpty(Utils.getStringFromEditText(mBinder.includeLayoutReference1.etOfficeReferenceName)) &&
                        TextUtils.isEmpty(Utils.getStringFromEditText(mBinder.includeLayoutReference1.etOfficeReferenceEmail)) &&
                        TextUtils.isEmpty(Utils.getStringFromEditText(mBinder.includeLayoutReference1.etOfficeReferenceMobile))) {

                    Utils.showToast(this, getString(R.string.complete_reference));
                } else {
                    mBinder.includeLayoutReference2.tvReferenceDelete.setVisibility(View.VISIBLE);
                    mBinder.tvAddMoreReference.setVisibility(View.GONE);
                    mBinder.includeLayoutReference2.tvReferenceCount.setText(getString(R.string.reference2));

                    mBinder.layoutReference2.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rel_save:
                updateExp();
                break;
            case R.id.et_office_state:
                Intent intent = new Intent(this, SearchStateActivity.class);
                if (mStateList != null && !mStateList.isEmpty()) {
                    intent.putExtra(Constants.BundleKey.PREV_SEL_STATE, mPrevSelStatePos);
                    intent.putParcelableArrayListExtra(Constants.BundleKey.STATE, mStateList);
                }
                startActivityForResult(intent, Constants.REQUEST_CODE.REQUEST_CODE_STATE);
                break;
            default:
                break;
        }
    }

    private void updateExp() {
        final HashMap<Boolean, String> result = WorkExpValidationUtil.checkValidation(mBinder.layoutReference2.getVisibility(), selectedJobTitle, expMonth,
                Utils.getStringFromEditText(mBinder.layoutWorkExpViewEdit.etOfficeName),
                Utils.getStringFromEditText(mBinder.layoutWorkExpViewEdit.etOfficeAddress),
                Utils.getStringFromEditText(mBinder.layoutWorkExpViewEdit.etOfficeCity),
                Utils.getStringFromEditText(mBinder.layoutWorkExpViewEdit.etOfficeState),
                Utils.getStringFromEditText(mBinder.includeLayoutReference1.etOfficeReferenceEmail),
                Utils.getStringFromEditText(mBinder.includeLayoutReference2.etOfficeReferenceEmail),
                Utils.getStringFromEditText(mBinder.includeLayoutReference1.etOfficeReferenceMobile),
                Utils.getStringFromEditText(mBinder.includeLayoutReference2.etOfficeReferenceMobile),
                Utils.getStringFromEditText(mBinder.includeLayoutReference1.etOfficeReferenceName),
                Utils.getStringFromEditText(mBinder.includeLayoutReference2.etOfficeReferenceName));
        hideKeyboard();
        if (result.containsKey(false)) {
            showToast(result.get(false));
        } else {
            if (TextUtils.isEmpty(result.get(true))) {
                callUpdateExpApi(createWorkExperienceRequest());
            } else {
                showToast(result.get(true));
            }
        }
    }

    @NotNull
    private WorkExpRequest createWorkExperienceRequest() {
        WorkExpRequest request = WorkExpValidationUtil.prepareWorkExpRequest(mBinder.layoutReference2.getVisibility(),
                Constants.APIS.ACTION_EDIT,
                jobTitleId,
                expMonth,
                Utils.getStringFromEditText(mBinder.layoutWorkExpViewEdit.etOfficeName),
                Utils.getStringFromEditText(mBinder.layoutWorkExpViewEdit.etOfficeAddress),
                Utils.getStringFromEditText(mBinder.layoutWorkExpViewEdit.etOfficeCity),
                Utils.getStringFromEditText(mBinder.layoutWorkExpViewEdit.etOfficeState),
                Utils.getStringFromEditText(mBinder.includeLayoutReference1.etOfficeReferenceName),
                Utils.getStringFromEditText(mBinder.includeLayoutReference1.etOfficeReferenceMobile),
                Utils.getStringFromEditText(mBinder.includeLayoutReference1.etOfficeReferenceEmail),
                Utils.getStringFromEditText(mBinder.includeLayoutReference2.etOfficeReferenceEmail),
                Utils.getStringFromEditText(mBinder.includeLayoutReference2.etOfficeReferenceName),
                Utils.getStringFromEditText(mBinder.includeLayoutReference2.etOfficeReferenceMobile));
        request.setId(workExpList.get(position).getId());
        return request;
    }


    @Override
    public void onExperienceSection(int year, int month) {
        String yearLabel, monthLabel;

        if (year == 1) {
            yearLabel = getString(R.string.txt_single_year);
        } else {
            yearLabel = getString(R.string.txt_multiple_years);
        }

        if (month == 1) {
            monthLabel = getString(R.string.txt_single_month);
        } else {
            monthLabel = getString(R.string.txt_multiple_months);
        }

        mBinder.layoutWorkExpViewEdit.tvExperienceWorkExp
                .setText(year + " " + yearLabel + " " + month + " " + monthLabel);

        expMonth = year * 12 + month;
    }

    private void callDeleteApi() {
        viewModel.deleteWorkExperience(Integer.parseInt(workExpList.get(position).getId()));
    }

    private void callUpdateExpApi(final WorkExpRequest workExpRequest) {
        viewModel.addWorkExp(workExpRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.REQUEST_CODE.REQUEST_CODE_STATE) {
                mStateList = data.getParcelableArrayListExtra(Constants.BundleKey.STATE);
                mPrevSelStatePos = data.getIntExtra(Constants.BundleKey.PREV_SEL_STATE, -1);
                mBinder.layoutWorkExpViewEdit.etOfficeState.setText(data.getStringExtra(Constants.BundleKey.SEL_STATE));
            }
        }
    }

    @Override
    public void onJobTitleSelection(String title, int titleId, int postion, int isLicenseRequired) {
        selectedJobTitle = title;
        jobTitleId = titleId;
        mBinder.layoutWorkExpViewEdit.etJobTitle.setText(title);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideKeyboard();
        startActivity(new Intent(ViewAndEditWorkExperienceActivity.this, HomeActivity.class)
                .putExtra(Constants.EXTRA_FROM_JOB_DETAIL, true)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
