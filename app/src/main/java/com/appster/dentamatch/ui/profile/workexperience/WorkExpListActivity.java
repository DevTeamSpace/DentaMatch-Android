package com.appster.dentamatch.ui.profile.workexperience;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityWorkExpListBinding;
import com.appster.dentamatch.interfaces.JobTitleSelectionListener;
import com.appster.dentamatch.interfaces.YearSelectionListener;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.auth.WorkExpListRequest;
import com.appster.dentamatch.network.request.auth.WorkExpRequest;
import com.appster.dentamatch.network.response.auth.WorkExpResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.profile.affiliation.AffiliationActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.UsPhoneNumberFormat;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.util.socialhelper.WorkExpValidationUtil;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetJobTitle;
import com.appster.dentamatch.widget.bottomsheet.BottomSheetPicker;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by virender on 05/01/17.
 */
public class WorkExpListActivity extends BaseActivity implements View.OnClickListener, YearSelectionListener, JobTitleSelectionListener {
    private ActivityWorkExpListBinding mBinder;
    private String selectedJobtitle = "";
    private int jobTitleId, expMonth, jobTitlePosition;
    private ArrayList<WorkExpRequest> workExpList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_work_exp_list);
        initViews();
        hideKeyboard();
        callGetExpListApi(prepareListRequest());
    }


    private void initViews() {
        mBinder.toolbarWorkExpList.ivToolBarLeft.setOnClickListener(this);
        mBinder.includeWorkExpList.tvExperinceWorkExp.setOnClickListener(this);
        mBinder.includeWorkExpList.etJobTitle.setOnClickListener(this);
        mBinder.tvAddMoreExperience.setOnClickListener(this);
        mBinder.tvExperienceDelete.setOnClickListener(this);
        mBinder.tvAddMoreReference.setOnClickListener(this);
        mBinder.btnNextWorkExpLsit.setOnClickListener(this);
        mBinder.includeLayoutRefrence2.tvRefrenceDelete.setOnClickListener(this);
        mBinder.toolbarWorkExpList.tvToolbarGeneralLeft.setText(getString(R.string.header_work_exp));
        UsPhoneNumberFormat addLineNumberFormatter = new UsPhoneNumberFormat(
                new WeakReference<EditText>(mBinder.includeLayoutRefrence1.etOfficeReferenceMobile));
        mBinder.includeLayoutRefrence1.etOfficeReferenceMobile.addTextChangedListener(addLineNumberFormatter);
        UsPhoneNumberFormat addLineNumberFormatter2 = new UsPhoneNumberFormat(
                new WeakReference<EditText>(mBinder.includeLayoutRefrence2.etOfficeReferenceMobile));
        mBinder.includeLayoutRefrence2.etOfficeReferenceMobile.addTextChangedListener(addLineNumberFormatter2);
        selectedJobtitle = PreferenceUtil.getJobTitle();
        if (!TextUtils.isEmpty(selectedJobtitle)) {
            mBinder.includeWorkExpList.etJobTitle.setText(selectedJobtitle);
        }
        if (!TextUtils.isEmpty(PreferenceUtil.getOfficeName())) {
            mBinder.includeWorkExpList.etOfficeName.setText(PreferenceUtil.getOfficeName());
        }
        try {
            mBinder.includeWorkExpList.tvExperinceWorkExp.setText(PreferenceUtil.getYear() + " " + getString(R.string.year) + " " + PreferenceUtil.getMonth() + " " + getString(R.string.month));
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
                startActivity(new Intent(this, AffiliationActivity.class));
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
                boolean isMoveForward = false;
                isMoveForward = WorkExpValidationUtil.checkValidation(mBinder.layoutReference2.getVisibility(), selectedJobtitle, expMonth,
                        Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeName),
                        Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeAddress),
                        Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeCity),
                        Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceName)
                        , Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceEmail),
                        Utils.getStringFromEditText(mBinder.includeLayoutRefrence2.etOfficeReferenceEmail),
                        Utils.getStringFromEditText(mBinder.includeLayoutRefrence2.etOfficeReferenceName));
                if (isMoveForward) {
                    hideKeyboard();
                    callAddExpApi(WorkExpValidationUtil.prepareWorkExpRequest(mBinder.layoutReference2.getVisibility(), Constants.APIS.ACTION_ADD, jobTitleId, expMonth,
                            Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeName), Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeAddress),
                            Utils.getStringFromEditText(mBinder.includeWorkExpList.etOfficeCity), Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceName)
                            , Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceMobile), Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceEmail),
                            Utils.getStringFromEditText(mBinder.includeLayoutRefrence2.etOfficeReferenceEmail), Utils.getStringFromEditText(mBinder.includeLayoutRefrence2.etOfficeReferenceName),
                            Utils.getStringFromEditText(mBinder.includeLayoutRefrence2.etOfficeReferenceMobile)));

//                    startActivity(new Intent(getApplicationContext(), WorkExpListActivity.class));
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

    //


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

    private void callAddExpApi(WorkExpRequest workExpRequest) {
        processToShowDialog("", getString(R.string.please_wait), null);

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.addWorkExp(workExpRequest).enqueue(new BaseCallback<WorkExpResponse>(WorkExpListActivity.this) {
            @Override
            public void onSuccess(WorkExpResponse response) {
                LogUtils.LOGD(TAG, "onSuccess");
                if (response.getStatus() == 1) {
                    clearAllExpField();
                    if (workExpList != null) {
                        response.getWorkExpResponseData().getSaveList().get(0).setJobtitleName(selectedJobtitle);
                        workExpList.add(response.getWorkExpResponseData().getSaveList().get(0));
                    }
                    inflateExpList(response.getWorkExpResponseData().getSaveList());
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
            tvJobTitle.setText(expList.get(i).getJobtitleName());

            tvExp.setText(Utils.getExpYears(expList.get(i).getMonthsOfExpereince()));
            referenceView.setTag(i);
            referenceView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(WorkExpListActivity.this, ViewAndEditWorkExperienceActivity.class);
                    intent.putExtra(Constants.INTENT_KEY.POSITION, (Integer) referenceView.getTag());
                    intent.putExtra(Constants.INTENT_KEY.DATA, workExpList);
                    startActivityForResult(intent, Constants.REQUEST_CODE.REQUEST_CODE_PASS_INTENT);
                }
            });
            mBinder.layoutExpListInflater.addView(referenceView);
        }
    }

    @Override
    public void onExperienceSection(int year, int month) {
        mBinder.includeWorkExpList.tvExperinceWorkExp.setText(year + " " + getString(R.string.year) + " " + month + " " + getString(R.string.month));
//        PreferenceUtil.setMonth(month);
//        PreferenceUtil.setYear(year);
        expMonth = PreferenceUtil.getYear() * 12 + PreferenceUtil.getMonth();

    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        clearAllExpField();
////        inflateExpList();
//    }

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
        selectedJobtitle = title;
        jobTitleId = titleId;
        jobTitlePosition = position;
        mBinder.includeWorkExpList.etJobTitle.setText(title);

    }
}
