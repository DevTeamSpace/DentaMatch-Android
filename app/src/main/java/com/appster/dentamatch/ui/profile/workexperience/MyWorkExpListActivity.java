/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.ui.profile.workexperience;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityWorkExpListBinding;
import com.appster.dentamatch.eventbus.ProfileUpdatedEvent;
import com.appster.dentamatch.interfaces.JobTitleSelectionListener;
import com.appster.dentamatch.interfaces.YearSelectionListener;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.workexp.WorkExpListRequest;
import com.appster.dentamatch.network.request.workexp.WorkExpRequest;
import com.appster.dentamatch.network.response.profile.StateList;
import com.appster.dentamatch.network.response.workexp.WorkExpResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.common.SearchStateActivity;
import com.appster.dentamatch.util.Alert;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.UsPhoneNumberFormat;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.util.WorkExpValidationUtil;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetJobTitle;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetPicker;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

/**
 * Created by bawenderyandra on 06/04/17.
 * To inject activity reference.
 */

public class MyWorkExpListActivity extends BaseActivity implements View.OnClickListener, JobTitleSelectionListener, YearSelectionListener {
    private static final String TAG = LogUtils.makeLogTag(MyWorkExpListActivity.class);
    private ActivityWorkExpListBinding mBinder;
    private boolean isFromProfile;
    private String mSelectedJobTitle = "";
    private int mJobTitleId, mExpMonth, jobTitlePosition;
    private ArrayList<WorkExpRequest> workExpList;
    private int mPrevSelStatePos;
    private ArrayList<StateList> mStateList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_work_exp_list);
        initViews();
        hideKeyboard();
        callGetExpListApi(prepareListRequest());

    }

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_tool_bar_left:
                hideKeyboard();
                onBackPressed();
                break;

            case R.id.tv_experience_delete:
                hideKeyboard();
                clearAllExpField();
                break;

            case R.id.et_job_title:
                hideKeyboard();
                new BottomSheetJobTitle(MyWorkExpListActivity.this, this, 0);
                break;

            case R.id.tv_reference_delete:
                mBinder.tvAddMoreReference.setVisibility(View.VISIBLE);
                mBinder.layoutReference2.setVisibility(View.GONE);
                mBinder.includeLayoutReference2.etOfficeReferenceEmail.setText("");
                mBinder.includeLayoutReference2.etOfficeReferenceMobile.setText("");
                mBinder.includeLayoutReference2.etOfficeReferenceName.setText("");
                break;

            case R.id.tv_add_more_reference:
                if (TextUtils.isEmpty(Utils.getStringFromEditText(mBinder.includeLayoutReference1.etOfficeReferenceName))) {
                    Utils.showToast(getApplicationContext(), getString(R.string.complete_reference));
                } else {
                    mBinder.includeLayoutReference2.tvReferenceDelete.setVisibility(View.VISIBLE);
                    mBinder.tvAddMoreReference.setVisibility(View.GONE);
                    mBinder.includeLayoutReference2.tvReferenceCount.setText(getString(R.string.reference2));
                    mBinder.layoutReference2.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.tv_experience_work_exp:
                int year = 0, month = 0;

                if (!TextUtils.isEmpty(mBinder.includeWorkExpList.tvExperienceWorkExp.getText().toString())) {
                    String split[] = mBinder.includeWorkExpList.tvExperienceWorkExp.getText().toString().split(" ");
                    year = Integer.parseInt(split[0]);
                    month = Integer.parseInt(split[2]);
                }

                new BottomSheetPicker(MyWorkExpListActivity.this, MyWorkExpListActivity.this, year, month);
                break;

            case R.id.tv_add_more_experience:
                hideKeyboard();
                addExperience();
                break;

            case R.id.btn_next_work_exp_list:
                hideKeyboard();
                //validateDataAndProceed();
                addExperience();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.REQUEST_CODE.REQUEST_CODE_STATE) {
                mStateList = data.getParcelableArrayListExtra(Constants.BundleKey.STATE);
                mPrevSelStatePos = data.getIntExtra(Constants.BundleKey.PREV_SEL_STATE, -1);
                mBinder.includeWorkExpList.etOfficeState.setText(data.getStringExtra(Constants.BundleKey.SEL_STATE));
                return;
            }
            if (requestCode == Constants.REQUEST_CODE.REQUEST_CODE_PASS_INTENT) {
                if (data != null) {
                    workExpList.clear();
                    workExpList = data.getParcelableArrayListExtra(Constants.INTENT_KEY.DATA);
                    clearAllExpField();
                    inflateExpList(workExpList);
                }
            }
        }
    }

    private void validateDataAndProceed() {
        /*
         In case all the fields are empty then we proceed directly to the next screen.
         */
        if (TextUtils.isEmpty(mSelectedJobTitle) &&
                TextUtils.isEmpty(mBinder.includeWorkExpList.etOfficeName.getText()) &&
                TextUtils.isEmpty(mBinder.includeWorkExpList.etOfficeAddress.getText()) &&
                TextUtils.isEmpty(mBinder.includeWorkExpList.etOfficeCity.getText()) &&
                TextUtils.isEmpty(mBinder.includeLayoutReference1.etOfficeReferenceEmail.getText()) &&
                TextUtils.isEmpty(mBinder.includeLayoutReference2.etOfficeReferenceEmail.getText()) &&
                TextUtils.isEmpty(mBinder.includeLayoutReference1.etOfficeReferenceMobile.getText()) &&
                TextUtils.isEmpty(mBinder.includeLayoutReference2.etOfficeReferenceMobile.getText()) &&

                TextUtils.isEmpty(mBinder.includeLayoutReference1.etOfficeReferenceName.getText()) &&
                TextUtils.isEmpty(mBinder.includeLayoutReference2.etOfficeReferenceName.getText())) {

            if (isFromProfile) {
                EventBus.getDefault().post(new ProfileUpdatedEvent(true));
                MyWorkExpListActivity.this.finish();
            } else {
                startActivity(new Intent(MyWorkExpListActivity.this, SchoolingActivity.class));
            }

        } else {
            final HashMap<Boolean, String> result = WorkExpValidationUtil.checkValidation(mBinder.layoutReference2.getVisibility(),
                    mSelectedJobTitle,
                    mExpMonth,
                    Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeName),
                    Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeAddress),
                    Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeCity),
                    Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeState),
                    Utils.getStringFromEditText(mBinder.includeLayoutReference1.etOfficeReferenceEmail),
                    Utils.getStringFromEditText(mBinder.includeLayoutReference2.etOfficeReferenceEmail),
                    Utils.getStringFromEditText(mBinder.includeLayoutReference1.etOfficeReferenceMobile),
                    Utils.getStringFromEditText(mBinder.includeLayoutReference2.etOfficeReferenceMobile),
                    Utils.getStringFromEditText(mBinder.includeLayoutReference1.etOfficeReferenceName),
                    Utils.getStringFromEditText(mBinder.includeLayoutReference2.etOfficeReferenceName));
            /*
             In case the page is partially filled we get 'false' and we proceed accordingly.
             */
            if (result.containsKey(false)) {

                Alert.createYesNoAlert(this, getString(R.string.save_label),
                        getString(R.string.txt_discard), getString(R.string.header_work_exp),
                        getString(R.string.msg_discard_partial_experience),
                        new Alert.OnAlertClickListener() {
                            @Override
                            public void onPositive(DialogInterface dialog) {
                                dialog.dismiss();
                                showToast(result.get(false));
                            }

                            @Override
                            public void onNegative(DialogInterface dialog) {
                                dialog.dismiss();
                                //startActivity(new Intent(MyWorkExpListActivity.this, SchoolingActivity.class));
                                finish();

                            }
                        });

            } else {
                /*
                  In case the result contains 'true' value means that the field are filled but the
                  there is a formatting exception like phone number format exception or email not valid exception.
                 */
                if (!TextUtils.isEmpty(result.get(true))) {
                    showToast(result.get(true));
                } else {
                    /*
                     Prepare Request for the add experience API.
                    */
                    WorkExpRequest request = WorkExpValidationUtil.prepareWorkExpRequest(mBinder.layoutReference2.getVisibility(),
                            Constants.APIS.ACTION_ADD,
                            mJobTitleId,
                            mExpMonth,
                            Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeName),
                            Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeAddress),
                            Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeCity),
                            Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeState),
                            Utils.getStringFromEditText(mBinder.includeLayoutReference1.etOfficeReferenceName),
                            Utils.getStringFromEditText(mBinder.includeLayoutReference1.etOfficeReferenceMobile),
                            Utils.getStringFromEditText(mBinder.includeLayoutReference1.etOfficeReferenceEmail),
                            Utils.getStringFromEditText(mBinder.includeLayoutReference2.etOfficeReferenceEmail),
                            Utils.getStringFromEditText(mBinder.includeLayoutReference2.etOfficeReferenceName),
                            Utils.getStringFromEditText(mBinder.includeLayoutReference2.etOfficeReferenceMobile));

                    callAddExpApi(request, false);
                }
            }

        }
    }


    private WorkExpListRequest prepareListRequest() {
        processToShowDialog();

        WorkExpListRequest workExpListRequest = new WorkExpListRequest();
        workExpListRequest.setLimit(Constants.WORK_EXP_LIST_LIMIT);
        workExpListRequest.setStart(0);
        return workExpListRequest;
    }

    private void inflateExpList(ArrayList<WorkExpRequest> expList) {
        hideKeyboard();
        mBinder.layoutExpListInflater.removeAllViews();

        for (int i = 0; i < expList.size(); i++) {
            final View referenceView = getLayoutInflater().inflate(R.layout.layout_work_exp_header_item, mBinder.layoutExpListInflater, false);
            TextView tvJobTitle = referenceView.findViewById(R.id.tv_title);
            TextView tvExp = referenceView.findViewById(R.id.tv_exp);
            tvJobTitle.setText(expList.get(i).getJobTitleName());

            tvExp.setText(Utils.getExpYears(expList.get(i).getMonthsOfExpereince()));
            referenceView.setTag(i);

            referenceView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MyWorkExpListActivity.this, ViewAndEditWorkExperienceActivity.class);
                    intent.putExtra(Constants.INTENT_KEY.POSITION, (Integer) referenceView.getTag());
                    intent.putExtra(Constants.INTENT_KEY.DATA, workExpList);
                    intent.putExtra(Constants.INTENT_KEY.FROM_WHERE, isFromProfile);
                    startActivityForResult(intent, Constants.REQUEST_CODE.REQUEST_CODE_PASS_INTENT);
                }
            });

            mBinder.layoutExpListInflater.addView(referenceView);
        }
    }

    /**
     * Checks for errors and validations for a particular experience and call Add Experience API.
     */
    private void addExperience() {
        final HashMap<Boolean, String> result = WorkExpValidationUtil.checkValidation(mBinder.layoutReference2.getVisibility(),
                mSelectedJobTitle,
                mExpMonth,
                Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeName),
                Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeAddress),
                Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeCity),
                Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeState),
                Utils.getStringFromEditText(mBinder.includeLayoutReference1.etOfficeReferenceEmail),
                Utils.getStringFromEditText(mBinder.includeLayoutReference2.etOfficeReferenceEmail),
                Utils.getStringFromEditText(mBinder.includeLayoutReference1.etOfficeReferenceMobile),
                Utils.getStringFromEditText(mBinder.includeLayoutReference2.etOfficeReferenceMobile),
                Utils.getStringFromEditText(mBinder.includeLayoutReference1.etOfficeReferenceName),
                Utils.getStringFromEditText(mBinder.includeLayoutReference2.etOfficeReferenceName));

        if (result.containsKey(false)) {
            showToast(result.get(false));
        } else {
            /*
              Prepare Request for the add experience API.
             */
            WorkExpRequest request = WorkExpValidationUtil.prepareWorkExpRequest(mBinder.layoutReference2.getVisibility(),
                    Constants.APIS.ACTION_ADD,
                    mJobTitleId,
                    mExpMonth,
                    Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeName),
                    Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeAddress),
                    Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeCity),
                    Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeState),
                    Utils.getStringFromEditText(mBinder.includeLayoutReference1.etOfficeReferenceName),
                    Utils.getStringFromEditText(mBinder.includeLayoutReference1.etOfficeReferenceMobile),
                    Utils.getStringFromEditText(mBinder.includeLayoutReference1.etOfficeReferenceEmail),
                    Utils.getStringFromEditText(mBinder.includeLayoutReference2.etOfficeReferenceEmail),
                    Utils.getStringFromEditText(mBinder.includeLayoutReference2.etOfficeReferenceName),
                    Utils.getStringFromEditText(mBinder.includeLayoutReference2.etOfficeReferenceMobile));

            callAddExpApi(request, true);
        }
    }


    private void callAddExpApi(WorkExpRequest workExpRequest, final boolean isAddExperience) {
        processToShowDialog();

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.addWorkExp(workExpRequest).enqueue(new BaseCallback<WorkExpResponse>(MyWorkExpListActivity.this) {
            @Override
            public void onSuccess(WorkExpResponse response) {

                if (response.getStatus() == 1) {
                    Utils.showToast(MyWorkExpListActivity.this, response.getMessage());
                    if (workExpList != null) {
                        response.getWorkExpResponseData().getSaveList().get(0).setJobTitleName(mSelectedJobTitle);
                        clearAllExpField();
                        workExpList.add(response.getWorkExpResponseData().getSaveList().get(0));
                        inflateExpList(workExpList);
                    }

                    if (isAddExperience) {
                        clearAllExpField();

                        if (isFromProfile) {
                            EventBus.getDefault().post(new ProfileUpdatedEvent(true));
                        }

                    } else {
                        if (isFromProfile) {
                            EventBus.getDefault().post(new ProfileUpdatedEvent(true));
                            MyWorkExpListActivity.this.finish();
                        } else {
                            startActivity(new Intent(MyWorkExpListActivity.this, SchoolingActivity.class));
                        }
                    }

                    if (workExpList != null && workExpList.size() > 0) {
                        mBinder.tvExperienceDelete.setVisibility(View.VISIBLE);
                    } else {
                        mBinder.tvExperienceDelete.setVisibility(View.GONE);
                    }

                } else {
                    Utils.showToast(getApplicationContext(), response.getMessage());
                }
            }

            @Override
            public void onFail(Call<WorkExpResponse> call, BaseResponse baseResponse) {
            }
        });

    }

    private void callGetExpListApi(WorkExpListRequest workExpListRequest) {
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.workExpList(workExpListRequest).enqueue(new BaseCallback<WorkExpResponse>(MyWorkExpListActivity.this) {
            @Override
            public void onSuccess(WorkExpResponse response) {
                if (response.getStatus() == 1) {
                    if (workExpList != null) {
                        workExpList.clear();
                    }

                    workExpList = response.getWorkExpResponseData().getSaveList();

                    if (workExpList.size() > 0) {
                        inflateExpList(response.getWorkExpResponseData().getSaveList());
                        mBinder.tvExperienceDelete.setVisibility(View.VISIBLE);
                    } else {
                        jobTitlePosition = 0;
                        mBinder.tvExperienceDelete.setVisibility(View.GONE);
                    }

                } else {
                    Utils.showToast(getApplicationContext(), response.getMessage());
                    mBinder.tvExperienceDelete.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFail(Call<WorkExpResponse> call, BaseResponse baseResponse) {
                mBinder.tvExperienceDelete.setVisibility(View.GONE);
            }
        });

    }

    private void initViews() {
        workExpList = new ArrayList<>();

        if (getIntent() != null) {
            isFromProfile = getIntent().getBooleanExtra(Constants.INTENT_KEY.FROM_WHERE, false);
        }

        if (isFromProfile) {
            mBinder.btnNextWorkExpList.setText(getString(R.string.save_label));
            mBinder.toolbarWorkExpList.tvToolbarGeneralLeft.setText(getString(R.string.header_edit_profile).toUpperCase());
        }

        if (isFromProfile) {
            mBinder.tvTitleScreen.setVisibility(View.VISIBLE);
            mBinder.toolbarWorkExpList.tvToolbarGeneralLeft.setText(getString(R.string.header_edit_profile));
            mBinder.toolbarWorkExpList.tvToolbarGeneralLeft.setText(getString(R.string.header_work_exp));

        } else {
            mBinder.toolbarWorkExpList.tvToolbarGeneralLeft.setText(getString(R.string.header_work_exp));
        }

        UsPhoneNumberFormat addLineNumberFormatter = new UsPhoneNumberFormat(
                new WeakReference<>(mBinder.includeLayoutReference1.etOfficeReferenceMobile));

        mBinder.includeLayoutReference1.etOfficeReferenceMobile.addTextChangedListener(addLineNumberFormatter);

        UsPhoneNumberFormat addLineNumberFormatter2 = new UsPhoneNumberFormat(
                new WeakReference<>(mBinder.includeLayoutReference2.etOfficeReferenceMobile));

        mBinder.includeLayoutReference2.etOfficeReferenceMobile.addTextChangedListener(addLineNumberFormatter2);

        /*
         Loads the previously selected data of the user from the preference of the user selected in the
         previous screen.
         */
        setDataFromPreference();

        mBinder.toolbarWorkExpList.ivToolBarLeft.setOnClickListener(this);
        mBinder.includeWorkExpList.tvExperienceWorkExp.setOnClickListener(this);
        mBinder.includeWorkExpList.etJobTitle.setOnClickListener(this);
        mBinder.tvAddMoreExperience.setOnClickListener(this);
        mBinder.tvExperienceDelete.setOnClickListener(this);
        mBinder.tvAddMoreReference.setOnClickListener(this);
        mBinder.btnNextWorkExpList.setOnClickListener(this);
        mBinder.includeLayoutReference2.tvReferenceDelete.setOnClickListener(this);

        mBinder.includeWorkExpList.etOfficeState.setFocusableInTouchMode(false);
        mBinder.includeWorkExpList.etOfficeState.setCursorVisible(false);
        mBinder.includeWorkExpList.etOfficeState.setOnClickListener(this);

    }

    private void setDataFromPreference() {
        //mSelectedJobTitle = PreferenceUtil.getJobTitle();

        if (PreferenceUtil.getJobTitle() != null) {
            mJobTitleId = PreferenceUtil.getJobTitleId();
        }

        /*if (!TextUtils.isEmpty(mSelectedJobTitle)) {
            mBinder.includeWorkExpList.etJobTitle.setText(mSelectedJobTitle);
        }*/

        if (!TextUtils.isEmpty(PreferenceUtil.getOfficeName())) {
            mBinder.includeWorkExpList.etOfficeName.setText(PreferenceUtil.getOfficeName());
        }

        try {
            String yearLabel, monthLabel;

            if (PreferenceUtil.getYear() == 1) {
                yearLabel = getString(R.string.txt_single_year);
            } else {
                yearLabel = getString(R.string.txt_multiple_years);
            }

            if (PreferenceUtil.getMonth() == 1) {
                monthLabel = getString(R.string.txt_single_month);
            } else {
                monthLabel = getString(R.string.txt_multiple_months);
            }

            String workExp = String.valueOf(PreferenceUtil.getYear())
                    .concat(" ")
                    .concat(yearLabel)
                    .concat(" ")
                    .concat(String.valueOf(PreferenceUtil.getMonth()))
                    .concat(" ")
                    .concat(monthLabel);
            mBinder.includeWorkExpList.tvExperienceWorkExp.setText(workExp);

            if (!TextUtils.isEmpty(mBinder.includeWorkExpList.tvExperienceWorkExp.getText().toString())) {
                String split[] = mBinder.includeWorkExpList.tvExperienceWorkExp.getText().toString().split(" ");
                mExpMonth = Integer.parseInt(split[0]) * 12 + Integer.parseInt(split[2]);
            }

        } catch (Exception e) {
            LogUtils.LOGE(TAG, e.getMessage());
        }
    }

    private void clearAllExpField() {
        mBinder.includeWorkExpList.tvExperienceWorkExp.setText("");
        mBinder.includeWorkExpList.etJobTitle.setText("");
        mBinder.includeWorkExpList.etOfficeAddress.setText("");
        mBinder.includeWorkExpList.etOfficeCity.setText("");
        mBinder.includeWorkExpList.etOfficeState.setText("");
        mBinder.includeWorkExpList.etOfficeName.setText("");
        mBinder.includeLayoutReference1.etOfficeReferenceEmail.setText("");
        mBinder.includeLayoutReference1.etOfficeReferenceMobile.setText("");
        mBinder.includeLayoutReference1.etOfficeReferenceName.setText("");
        mBinder.includeLayoutReference2.etOfficeReferenceEmail.setText("");
        mBinder.includeLayoutReference2.etOfficeReferenceMobile.setText("");
        mBinder.includeLayoutReference2.etOfficeReferenceName.setText("");
        mBinder.layoutReference2.setVisibility(View.GONE);

        mExpMonth = 0;
        mSelectedJobTitle = "";
    }

    @Override
    public void onJobTitleSelection(String title, int titleId, int position, int isLicenseRequired) {
        mSelectedJobTitle = title;
        mJobTitleId = titleId;
        jobTitlePosition = position;
        mBinder.includeWorkExpList.etJobTitle.setText(title);
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

        String workExp = String.valueOf(year)
                .concat(" ")
                .concat(yearLabel)
                .concat(" ")
                .concat(String.valueOf(month))
                .concat(" ")
                .concat(monthLabel);

        mBinder.includeWorkExpList.tvExperienceWorkExp.setText(workExp);
        mExpMonth = year * 12 + month;
    }
}
