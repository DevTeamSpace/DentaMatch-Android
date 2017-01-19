package com.appster.dentamatch.ui.profile.workexperience;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityViewAndWditWorkExperienceBinding;
import com.appster.dentamatch.interfaces.JobTitleSelectionListener;
import com.appster.dentamatch.interfaces.YearSelectionListener;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.workexp.WorkExpRequest;
import com.appster.dentamatch.network.response.workexp.WorkExpResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
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
 * Created by virender on 04/01/17.
 */
public class ViewAndEditWorkExperienceActivity extends BaseActivity implements View.OnClickListener, YearSelectionListener, JobTitleSelectionListener {
    private ActivityViewAndWditWorkExperienceBinding mBinder;
    private int position;
    private int count = 0;
    private String selectedJobtitle;
    private ArrayList<WorkExpRequest> workExpList;
    private int expMonth, jobTitleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_view_and_wdit_work_experience);
        if (getIntent() != null) {
            position = getIntent().getIntExtra(Constants.INTENT_KEY.POSITION, 0);
            workExpList = getIntent().getParcelableArrayListExtra(Constants.INTENT_KEY.DATA);
        }
        initViews();
    }

    private void initViews() {
        mBinder.toolbarWorkExpView.ivToolBarLeft.setOnClickListener(this);
        mBinder.btnUpdate.setOnClickListener(this);
        mBinder.tvExperienceDelete.setOnClickListener(this);
        mBinder.tvAddMoreReference.setOnClickListener(this);
        mBinder.includeLayoutRefrence2.tvRefrenceDelete.setOnClickListener(this);
        mBinder.layoutWorkExpViewEdit.tvExperinceWorkExp.setOnClickListener(this);
        mBinder.layoutWorkExpViewEdit.etJobTitle.setOnClickListener(this);
        mBinder.toolbarWorkExpView.tvToolbarGeneralLeft.setText(getString(R.string.header_work_exp));


        setViewData();
        if (position == 0) {
            mBinder.tvExperienceDelete.setVisibility(View.GONE);
        } else {
            mBinder.tvExperienceDelete.setVisibility(View.VISIBLE);

        }
        hideKeyboard();
        UsPhoneNumberFormat addLineNumberFormatter = new UsPhoneNumberFormat(
                new WeakReference<EditText>(mBinder.includeLayoutRefrence1.etOfficeReferenceMobile));
        mBinder.includeLayoutRefrence1.etOfficeReferenceMobile.addTextChangedListener(addLineNumberFormatter);
        UsPhoneNumberFormat addLineNumberFormatter2 = new UsPhoneNumberFormat(
                new WeakReference<EditText>(mBinder.includeLayoutRefrence2.etOfficeReferenceMobile));
        mBinder.includeLayoutRefrence2.etOfficeReferenceMobile.addTextChangedListener(addLineNumberFormatter2);
    }

    private void setViewData() {
        selectedJobtitle = workExpList.get(position).getJobtitleName();
        jobTitleId = workExpList.get(position).getJobTitleId();
        expMonth = workExpList.get(position).getMonthsOfExpereince();
        mBinder.layoutWorkExpViewEdit.etOfficeCity.setText(workExpList.get(position).getCity());
        mBinder.layoutWorkExpViewEdit.etOfficeName.setText(workExpList.get(position).getOfficeName());
        mBinder.layoutWorkExpViewEdit.etJobTitle.setText(workExpList.get(position).getJobtitleName());
        mBinder.layoutWorkExpViewEdit.tvExperinceWorkExp.setText(Utils.getExpYears(workExpList.get(position).getMonthsOfExpereince()));
        mBinder.layoutWorkExpViewEdit.etOfficeAddress.setText(workExpList.get(position).getOfficeAddress());
        mBinder.includeLayoutRefrence1.etOfficeReferenceMobile.setText(workExpList.get(position).getReference1Mobile());
        mBinder.includeLayoutRefrence1.etOfficeReferenceName.setText(workExpList.get(position).getReference1Name());
        mBinder.includeLayoutRefrence1.etOfficeReferenceEmail.setText(workExpList.get(position).getReference1Email());
        if (!TextUtils.isEmpty(workExpList.get(position).getReference2Name())) {
            mBinder.layoutRefrence2.setVisibility(View.VISIBLE);
            mBinder.includeLayoutRefrence2.tvRefrenceCount.setText(getString(R.string.reference2));
            mBinder.includeLayoutRefrence2.tvRefrenceDelete.setVisibility(View.VISIBLE);
            mBinder.includeLayoutRefrence2.etOfficeReferenceEmail.setText(workExpList.get(position).getReference2Email());
            mBinder.includeLayoutRefrence2.etOfficeReferenceMobile.setText(workExpList.get(position).getReference2Mobile());
            mBinder.includeLayoutRefrence2.etOfficeReferenceName.setText(workExpList.get(position).getReference2Name());

        }


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
                    callDeleteApi();

                }
                break;
            case R.id.et_job_title:
                hideKeyboard();
                new BottomSheetJobTitle(ViewAndEditWorkExperienceActivity.this, this, 0);
                break;
            case R.id.btn_update:
                boolean isMoveForward = false;
                isMoveForward = WorkExpValidationUtil.checkValidation(mBinder.layoutRefrence2.getVisibility(), selectedJobtitle, expMonth,
                        Utils.getStringFromEditText(mBinder.layoutWorkExpViewEdit.etOfficeName),
                        Utils.getStringFromEditText(mBinder.layoutWorkExpViewEdit.etOfficeAddress),
                        Utils.getStringFromEditText(mBinder.layoutWorkExpViewEdit.etOfficeCity),
                        Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceName)
                        , Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceEmail),
                        Utils.getStringFromEditText(mBinder.includeLayoutRefrence2.etOfficeReferenceEmail),
                        Utils.getStringFromEditText(mBinder.includeLayoutRefrence2.etOfficeReferenceName));
                if (isMoveForward) {
                    hideKeyboard();
                    WorkExpRequest request = WorkExpValidationUtil.prepareWorkExpRequest(mBinder.layoutRefrence2.getVisibility(), Constants.APIS.ACTION_EDIT, jobTitleId, expMonth,
                            Utils.getStringFromEditText(mBinder.layoutWorkExpViewEdit.etOfficeName), Utils.getStringFromEditText(mBinder.layoutWorkExpViewEdit.etOfficeAddress),
                            Utils.getStringFromEditText(mBinder.layoutWorkExpViewEdit.etOfficeCity), Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceName)
                            , Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceMobile), Utils.getStringFromEditText(mBinder.includeLayoutRefrence1.etOfficeReferenceEmail),
                            Utils.getStringFromEditText(mBinder.includeLayoutRefrence2.etOfficeReferenceEmail), Utils.getStringFromEditText(mBinder.includeLayoutRefrence2.etOfficeReferenceName),
                            Utils.getStringFromEditText(mBinder.includeLayoutRefrence2.etOfficeReferenceMobile));
                    request.setId(workExpList.get(position).getId());
                    callUpdateExpApi(request);

                }
                break;
            case R.id.tv_experince_work_exp:
                int year = 0, month = 0;
                if (!TextUtils.isEmpty(mBinder.layoutWorkExpViewEdit.tvExperinceWorkExp.getText().toString())) {
                    String split[] = mBinder.layoutWorkExpViewEdit.tvExperinceWorkExp.getText().toString().split(" ");
                    year = Integer.parseInt(split[0]);
                    month = Integer.parseInt(split[2]);
                }
                new BottomSheetPicker(this, this, year, month);
                break;
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
        expMonth = year * 12 + month;

    }


    private void callDeleteApi() {
        processToShowDialog("", getString(R.string.please_wait), null);

//        DeleteRequest deleteRequest=new DeleteRequest();
//        deleteRequest.setId();
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.workExpDelete(Integer.parseInt(workExpList.get(position).getId())).enqueue(new BaseCallback<BaseResponse>(ViewAndEditWorkExperienceActivity.this) {
            @Override
            public void onSuccess(BaseResponse response) {
                Utils.showToast(getApplicationContext(), response.getMessage());
                LogUtils.LOGD(TAG, "onSuccess");
                if (response.getStatus() == 1) {
                    workExpList.remove(position);
                    Intent intent = new Intent();
                    intent.putExtra(Constants.INTENT_KEY.DATA, workExpList);
                    setResult(Constants.REQUEST_CODE.REQUEST_CODE_PASS_INTENT, intent);
                    finish();
                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGD(TAG, "onFail");
                Utils.showToast(getApplicationContext(), baseResponse.getMessage());
            }
        });
    }

    private void callUpdateExpApi(final WorkExpRequest workExpRequest) {
        processToShowDialog("", getString(R.string.please_wait), null);

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.addWorkExp(workExpRequest).enqueue(new BaseCallback<WorkExpResponse>(ViewAndEditWorkExperienceActivity.this) {
            @Override
            public void onSuccess(WorkExpResponse response) {
                LogUtils.LOGD(TAG, "onSuccess");
                Utils.showToast(getApplicationContext(), response.getMessage());
                if (response.getStatus() == 1) {
                    workExpList.remove(position);
                    response.getWorkExpResponseData().getSaveList().get(0).setJobtitleName(selectedJobtitle);
                    workExpList.add(position, response.getWorkExpResponseData().getSaveList().get(0));
                    Intent intent = new Intent();
                    intent.putExtra(Constants.INTENT_KEY.DATA, workExpList);
                    setResult(Constants.REQUEST_CODE.REQUEST_CODE_PASS_INTENT, intent);
                    finish();
                }
            }

            @Override
            public void onFail(Call<WorkExpResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGD(TAG, "onFail");
                Utils.showToast(getApplicationContext(), baseResponse.getMessage());
            }
        });

    }

    @Override
    public void onJobTitleSelection(String title, int titleId, int postion) {
        selectedJobtitle = title;
        jobTitleId = titleId;


    }
}
