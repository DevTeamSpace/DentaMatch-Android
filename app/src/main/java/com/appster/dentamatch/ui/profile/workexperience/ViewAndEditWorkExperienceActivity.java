package com.appster.dentamatch.ui.profile.workexperience;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityViewAndWditWorkExperienceBinding;
import com.appster.dentamatch.eventbus.ProfileUpdatedEvent;
import com.appster.dentamatch.interfaces.JobTitleSelectionListener;
import com.appster.dentamatch.interfaces.YearSelectionListener;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.workexp.WorkExpRequest;
import com.appster.dentamatch.network.response.workexp.WorkExpResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.common.HomeActivity;
import com.appster.dentamatch.ui.searchjob.JobDetailActivity;
import com.appster.dentamatch.util.Alert;
import com.appster.dentamatch.util.Constants;
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
 * Created by virender on 04/01/17.
 */
public class ViewAndEditWorkExperienceActivity extends BaseActivity implements View.OnClickListener, YearSelectionListener, JobTitleSelectionListener {
    private ActivityViewAndWditWorkExperienceBinding mBinder;
    private int position;
    private String selectedJobtitle;
    private ArrayList<WorkExpRequest> workExpList;
    private int expMonth, jobTitleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_view_and_wdit_work_experience);
        initViews();
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
    }

    private void setViewData() {
        selectedJobtitle = workExpList.get(position).getJobTitleName();
        jobTitleId = workExpList.get(position).getJobTitleId();
        expMonth = workExpList.get(position).getMonthsOfExpereince();
        mBinder.layoutWorkExpViewEdit.etOfficeCity.setText(workExpList.get(position).getCity());
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
    public String getActivityName() {
        return null;
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
                updateExp(false);

                break;


            case R.id.tv_experience_work_exp:
                int year = 0, month = 0;
                if (!TextUtils.isEmpty(mBinder.layoutWorkExpViewEdit.tvExperienceWorkExp.getText().toString())) {
                    String split[] = mBinder.layoutWorkExpViewEdit.tvExperienceWorkExp.getText().toString().split(" ");
                    if(split!=null && split.length==4) {
                        year = Integer.parseInt(split[0]);
                        month = Integer.parseInt(split[2]);
                    }else {
                        if(split!=null && split.length==2 && (split[1].equals(getString(R.string.txt_single_month))|| split[1].equals(getString(R.string.txt_multiple_months)))) {
                            month = Integer.parseInt(split[0]);
                        }else {
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
                updateExp(true);
                break;

            default:
                break;
        }
    }

    private void updateExp(boolean isBack){
        final HashMap<Boolean, String> result = WorkExpValidationUtil.checkValidation(mBinder.layoutReference2.getVisibility(), selectedJobtitle, expMonth,
                Utils.getStringFromEditText(mBinder.layoutWorkExpViewEdit.etOfficeName),
                Utils.getStringFromEditText(mBinder.layoutWorkExpViewEdit.etOfficeAddress),
                Utils.getStringFromEditText(mBinder.layoutWorkExpViewEdit.etOfficeCity),
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
            if(TextUtils.isEmpty(result.get(true))) {
                WorkExpRequest request = WorkExpValidationUtil.prepareWorkExpRequest(mBinder.layoutReference2.getVisibility(),
                        Constants.APIS.ACTION_EDIT,
                        jobTitleId,
                        expMonth,
                        Utils.getStringFromEditText(mBinder.layoutWorkExpViewEdit.etOfficeName),
                        Utils.getStringFromEditText(mBinder.layoutWorkExpViewEdit.etOfficeAddress),
                        Utils.getStringFromEditText(mBinder.layoutWorkExpViewEdit.etOfficeCity),
                        Utils.getStringFromEditText(mBinder.includeLayoutReference1.etOfficeReferenceName),
                        Utils.getStringFromEditText(mBinder.includeLayoutReference1.etOfficeReferenceMobile),
                        Utils.getStringFromEditText(mBinder.includeLayoutReference1.etOfficeReferenceEmail),
                        Utils.getStringFromEditText(mBinder.includeLayoutReference2.etOfficeReferenceEmail),
                        Utils.getStringFromEditText(mBinder.includeLayoutReference2.etOfficeReferenceName),
                        Utils.getStringFromEditText(mBinder.includeLayoutReference2.etOfficeReferenceMobile));
                request.setId(workExpList.get(position).getId());
                callUpdateExpApi(request,isBack);

            }else{
                showToast(result.get(true));
            }
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

        mBinder.layoutWorkExpViewEdit.tvExperienceWorkExp
                .setText(year + " " + yearLabel + " " + month + " " + monthLabel);

        expMonth = year * 12 + month;
    }

    private void callDeleteApi() {
        processToShowDialog();

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.workExpDelete(Integer.parseInt(workExpList.get(position).getId())).enqueue(new BaseCallback<BaseResponse>(ViewAndEditWorkExperienceActivity.this) {
            @Override
            public void onSuccess(BaseResponse response) {
                Utils.showToast(getApplicationContext(), response.getMessage());

                if (response.getStatus() == 1) {
                    workExpList.remove(position);
                    Intent intent = new Intent();
                    intent.putExtra(Constants.INTENT_KEY.DATA, workExpList);
                    setResult(Constants.REQUEST_CODE.REQUEST_CODE_PASS_INTENT, intent);
                    EventBus.getDefault().post(new ProfileUpdatedEvent(true));
                    finish();
                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                Utils.showToast(getApplicationContext(), baseResponse.getMessage());
            }
        });
    }

    private void callUpdateExpApi(final WorkExpRequest workExpRequest,final boolean isBack) {
        processToShowDialog();

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.addWorkExp(workExpRequest).enqueue(new BaseCallback<WorkExpResponse>(ViewAndEditWorkExperienceActivity.this) {
            @Override
            public void onSuccess(WorkExpResponse response) {
                Utils.showToast(getApplicationContext(), response.getMessage());

                if (response.getStatus() == 1) {
                    workExpList.remove(position);
                    response.getWorkExpResponseData().getSaveList().get(0).setJobTitleName(selectedJobtitle);
                    workExpList.add(position, response.getWorkExpResponseData().getSaveList().get(0));

                    if(isBack) {
                        Intent intent = new Intent();
                        intent.putExtra(Constants.INTENT_KEY.DATA, workExpList);
                        setResult(Constants.REQUEST_CODE.REQUEST_CODE_PASS_INTENT, intent);
                        EventBus.getDefault().post(new ProfileUpdatedEvent(true));
                        finish();
                    }else {
                        startActivity(new Intent(ViewAndEditWorkExperienceActivity.this, HomeActivity.class)
                                .putExtra(Constants.EXTRA_FROM_JOB_DETAIL, true)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                }
            }

            @Override
            public void onFail(Call<WorkExpResponse> call, BaseResponse baseResponse) {
            }
        });

    }

    @Override
    public void onJobTitleSelection(String title, int titleId, int postion, int isLicenseRequired) {
        selectedJobtitle = title;
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
