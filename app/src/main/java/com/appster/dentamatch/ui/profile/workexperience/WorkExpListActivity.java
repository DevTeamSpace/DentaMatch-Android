package com.appster.dentamatch.ui.profile.workexperience;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityWorkExpListBinding;
import com.appster.dentamatch.interfaces.JobTitleSelectionListener;
import com.appster.dentamatch.interfaces.YearSelectionListener;
import com.appster.dentamatch.EventBus.ProfileUpdatedEvent;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.workexp.WorkExpListRequest;
import com.appster.dentamatch.network.request.workexp.WorkExpRequest;
import com.appster.dentamatch.network.response.workexp.WorkExpResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
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

import retrofit2.Call;

/**
 * Created by virender on 05/01/17.
 */
public class WorkExpListActivity extends BaseActivity implements View.OnClickListener, YearSelectionListener, JobTitleSelectionListener {
    private ActivityWorkExpListBinding mBinder;
    private boolean isFromProfile;
    private String mSelectedJobTitle = "";
    private int mJobTitleId, mExpMonth, jobTitlePosition;
    private ArrayList<WorkExpRequest> workExpList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_work_exp_list);
        if (getIntent() != null) {
            isFromProfile = getIntent().getBooleanExtra(Constants.INTENT_KEY.FROM_WHERE, false);
        }
        initViews();
        hideKeyboard();
        callGetExpListApi(prepareListRequest());
        workExpList = new ArrayList<>();
    }


    private void initViews() {
        mBinder.toolbarWorkExpList.ivToolBarLeft.setOnClickListener(this);
        mBinder.includeWorkExpList.tvExperinceWorkExp.setOnClickListener(this);
        mBinder.includeWorkExpList.etJobTitle.setOnClickListener(this);
        mBinder.tvAddMoreExperience.setOnClickListener(this);
        mBinder.tvExperienceDelete.setOnClickListener(this);
        mBinder.tvAddMoreReference.setOnClickListener(this);
        mBinder.btnNextWorkExpLsit.setOnClickListener(this);
        if (isFromProfile) {
            mBinder.tvTitleScreen.setVisibility(View.VISIBLE);
            mBinder.toolbarWorkExpList.tvToolbarGeneralLeft.setText(getString(R.string.header_edit_profile));
        } else {
            mBinder.toolbarWorkExpList.tvToolbarGeneralLeft.setText(getString(R.string.header_work_exp));

        }
        mBinder.includeLayoutRefrence2.tvRefrenceDelete.setOnClickListener(this);
        UsPhoneNumberFormat addLineNumberFormatter = new UsPhoneNumberFormat(
                new WeakReference<EditText>(mBinder.includeLayoutRefrence1.etOfficeReferenceMobile));
        mBinder.includeLayoutRefrence1.etOfficeReferenceMobile.addTextChangedListener(addLineNumberFormatter);
        UsPhoneNumberFormat addLineNumberFormatter2 = new UsPhoneNumberFormat(
                new WeakReference<EditText>(mBinder.includeLayoutRefrence2.etOfficeReferenceMobile));
        mBinder.includeLayoutRefrence2.etOfficeReferenceMobile.addTextChangedListener(addLineNumberFormatter2);
        if (isFromProfile) {
            mBinder.btnNextWorkExpLsit.setText(getString(R.string.save_label));
            mBinder.toolbarWorkExpList.tvToolbarGeneralLeft.setText(getString(R.string.header_edit_profile).toUpperCase());
        }
        mSelectedJobTitle = PreferenceUtil.getJobTitle();
        if (PreferenceUtil.getJobTitle() != null) {
            mJobTitleId = PreferenceUtil.getJobTitleId();
        }

        if (!TextUtils.isEmpty(mSelectedJobTitle)) {
            mBinder.includeWorkExpList.etJobTitle.setText(mSelectedJobTitle);
        }

        if (!TextUtils.isEmpty(PreferenceUtil.getOfficeName())) {
            mBinder.includeWorkExpList.etOfficeName.setText(PreferenceUtil.getOfficeName());
        }
        try {
            mBinder.includeWorkExpList.tvExperinceWorkExp.setText(PreferenceUtil.getYear() + " " + getString(R.string.year) + " " + PreferenceUtil.getMonth() + " " + getString(R.string.month));
            if (!TextUtils.isEmpty(mBinder.includeWorkExpList.tvExperinceWorkExp.getText().toString())) {
                String split[] = mBinder.includeWorkExpList.tvExperinceWorkExp.getText().toString().split(" ");
                mExpMonth = Integer.parseInt(split[0]) * 12 + Integer.parseInt(split[2]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private WorkExpListRequest prepareListRequest() {
        processToShowDialog("", getString(R.string.please_wait), null);

        WorkExpListRequest workExpListRequest = new WorkExpListRequest();
        workExpListRequest.setLimit(Constants.WORK_EXP_LIST_LIMIT);
        workExpListRequest.setStart(0);
        return workExpListRequest;
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
                hideKeyboard();
                clearAllExpField();
                break;
            case R.id.et_job_title:
                hideKeyboard();
                new BottomSheetJobTitle(WorkExpListActivity.this, this, 0);
                break;
            case R.id.btn_next_work_exp_lsit:
                hideKeyboard();
                if (workExpList.size() == 0) {
                    prepareRequestForAdd(true);
                } else {
                    launchNextActivity();
                }
                break;
            case R.id.tv_refrence_delete:
                mBinder.tvAddMoreReference.setVisibility(View.VISIBLE);

                mBinder.layoutReference2.setVisibility(View.GONE);
                mBinder.includeLayoutRefrence2.etOfficeReferenceEmail.setText("");
                mBinder.includeLayoutRefrence2.etOfficeReferenceMobile.setText("");
                mBinder.includeLayoutRefrence2.etOfficeReferenceName.setText("");
                break;
            case R.id.tv_add_more_reference:
                if (TextUtils.isEmpty(Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceName)) && TextUtils.isEmpty(Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceEmail)) && TextUtils.isEmpty(Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceMobile))) {
                    Utils.showToast(getApplicationContext(), getString(R.string.complete_reference));
                } else {
                    mBinder.includeLayoutRefrence2.tvRefrenceDelete.setVisibility(View.VISIBLE);
                    mBinder.tvAddMoreReference.setVisibility(View.GONE);
                    mBinder.includeLayoutRefrence2.tvRefrenceCount.setText(getString(R.string.reference2));

                    mBinder.layoutReference2.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_add_more_experience:
                prepareRequestForAdd(false);

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

    private void prepareRequestForAdd(boolean isNext) {
        boolean isMoveForward = false;
        isMoveForward = WorkExpValidationUtil.checkValidation(mBinder.layoutReference2.getVisibility(), mSelectedJobTitle, mExpMonth,
                Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeName),
                Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeAddress),
                Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeCity),
                Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceName)
                , Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceEmail),
                Utils.getStringFromEditText(mBinder.includeLayoutRefrence2.etOfficeReferenceEmail),
                Utils.getStringFromEditText(mBinder.includeLayoutRefrence2.etOfficeReferenceName),
                Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceMobile),
                Utils.getStringFromEditText(mBinder.includeLayoutRefrence2.etOfficeReferenceMobile));
        if (isMoveForward) {
            hideKeyboard();
            WorkExpRequest request = WorkExpValidationUtil.prepareWorkExpRequest(mBinder.layoutReference2.getVisibility(), Constants.APIS.ACTION_ADD, mJobTitleId, mExpMonth,
                    Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeName), Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeAddress),
                    Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeCity), Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceName)
                    , Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceMobile), Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceEmail),
                    Utils.getStringFromEditText(mBinder.includeLayoutRefrence2.etOfficeReferenceEmail), Utils.getStringFromEditText(mBinder.includeLayoutRefrence2.etOfficeReferenceName),
                    Utils.getStringFromEditText(mBinder.includeLayoutRefrence2.etOfficeReferenceMobile));
            callAddExpApi(request, isNext);
        }
    }

    private void clearAllExpField() {
        mBinder.includeWorkExpList.tvExperinceWorkExp.setText("");
        mBinder.includeWorkExpList.etJobTitle.setText("");
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

        mExpMonth = 0;
        mSelectedJobTitle = "";
    }

    private void callGetExpListApi(WorkExpListRequest workExpListRequest) {
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.workExpList(workExpListRequest).enqueue(new BaseCallback<WorkExpResponse>(WorkExpListActivity.this) {
            @Override
            public void onSuccess(WorkExpResponse response) {
                LogUtils.LOGD(TAG, "onSuccess");
                if (response.getStatus() == 1) {
                    if (workExpList != null) {
                        workExpList.clear();
                    }
                    workExpList = response.getWorkExpResponseData().getSaveList();
                    if (workExpList.size() > 0) {
                        inflateExpList(response.getWorkExpResponseData().getSaveList());
                    } else {
                        jobTitlePosition = 0;
                    }

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

    private void callAddExpApi(WorkExpRequest workExpRequest, final boolean isMoveNext) {
        processToShowDialog("", getString(R.string.please_wait), null);

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.addWorkExp(workExpRequest).enqueue(new BaseCallback<WorkExpResponse>(WorkExpListActivity.this) {
            @Override
            public void onSuccess(WorkExpResponse response) {
                LogUtils.LOGD(TAG, "onSuccess");
                if (response.getStatus() == 1) {
                    if (workExpList != null) {
                        response.getWorkExpResponseData().getSaveList().get(0).setJobTitleName(mSelectedJobTitle);
                        clearAllExpField();
                        workExpList.add(response.getWorkExpResponseData().getSaveList().get(0));
                        inflateExpList(workExpList);

                    }
                    if (isMoveNext) {
                        launchNextActivity();
                    } else {
                        inflateExpList(workExpList);
                    }
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

    private void inflateExpList(ArrayList<WorkExpRequest> expList) {
        hideKeyboard();
        mBinder.layoutExpListInflater.removeAllViews();

        for (int i = 0; i < expList.size(); i++) {
            final View referenceView = getLayoutInflater().inflate(R.layout.layout_work_exp_header_item, mBinder.layoutExpListInflater, false);
            TextView tvJobTitle = (TextView) referenceView.findViewById(R.id.tv_title);
            TextView tvExp = (TextView) referenceView.findViewById(R.id.tv_exp);
            tvJobTitle.setText(expList.get(i).getJobTitleName());

            tvExp.setText(Utils.getExpYears(expList.get(i).getMonthsOfExpereince()));
            referenceView.setTag(i);
            referenceView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(WorkExpListActivity.this, ViewAndEditWorkExperienceActivity.class);
                    intent.putExtra(Constants.INTENT_KEY.POSITION, (Integer) referenceView.getTag());
                    intent.putExtra(Constants.INTENT_KEY.DATA, workExpList);
                    intent.putExtra(Constants.INTENT_KEY.FROM_WHERE, isFromProfile);
                    startActivityForResult(intent, Constants.REQUEST_CODE.REQUEST_CODE_PASS_INTENT);
                }
            });
            mBinder.layoutExpListInflater.addView(referenceView);
        }
    }

    @Override
    public void onExperienceSection(int year, int month) {
        mBinder.includeWorkExpList.tvExperinceWorkExp.setText(year + " " + getString(R.string.year) + " " + month + " " + getString(R.string.month));
        mExpMonth = year * 12 + month;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE.REQUEST_CODE_PASS_INTENT) {
            if (data != null) {
                workExpList.clear();
                workExpList = data.getParcelableArrayListExtra(Constants.INTENT_KEY.DATA);
                clearAllExpField();
                inflateExpList(workExpList);
            }
        }
    }

    @Override
    public void onJobTitleSelection(String title, int titleId, int position) {
        mSelectedJobTitle = title;
        mJobTitleId = titleId;
        jobTitlePosition = position;
        mBinder.includeWorkExpList.etJobTitle.setText(title);
    }

    private void launchNextActivity() {
        if (TextUtils.isEmpty(mSelectedJobTitle) && mExpMonth == 0 && Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeName).equalsIgnoreCase("")
                && Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeAddress).equalsIgnoreCase("")
                && Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeCity).equalsIgnoreCase("")) {
            if (isFromProfile) {
                EventBus.getDefault().post(new ProfileUpdatedEvent(true));
                finish();
            } else {
                startActivity(new Intent(WorkExpListActivity.this, SchoolingActivity.class));
            }
        } else {
            if ((mSelectedJobTitle.equalsIgnoreCase("") || mExpMonth == 0 &&
                    Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeName).equalsIgnoreCase("") ||
                    Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeAddress).equalsIgnoreCase("") ||
                    Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeCity).equalsIgnoreCase("")) ||
                    (!TextUtils.isEmpty(mSelectedJobTitle) && mExpMonth != 0 &&
                            !TextUtils.isEmpty(Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeName)) &&
                            !TextUtils.isEmpty(Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeAddress)) &&
                            !TextUtils.isEmpty(Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeCity)))) {


                Alert.createYesNoAlert(WorkExpListActivity.this, getString(R.string.ok), getString(R.string.cancel), "", getString(R.string.alert_discard_exp), new Alert.OnAlertClickListener() {
                            @Override
                            public void onPositive(DialogInterface dialog) {
                                if (isFromProfile) {
                                    EventBus.getDefault().post(new ProfileUpdatedEvent(true));
                                    finish();
                                } else {
                                    startActivity(new Intent(WorkExpListActivity.this, SchoolingActivity.class));
                                }
                            }

                            @Override
                            public void onNegative(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        }

                );
            } else {
                startActivity(new Intent(WorkExpListActivity.this, SchoolingActivity.class));

            }
        }
    }
}
