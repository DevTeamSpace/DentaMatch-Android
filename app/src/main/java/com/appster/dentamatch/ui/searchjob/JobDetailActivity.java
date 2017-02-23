package com.appster.dentamatch.ui.searchjob;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityJobDetailBinding;
import com.appster.dentamatch.model.JobDetailModel;
import com.appster.dentamatch.model.SaveUnSaveEvent;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.jobs.JobApplyRequest;
import com.appster.dentamatch.network.request.jobs.JobDetailRequest;
import com.appster.dentamatch.network.request.jobs.SaveUnSaveRequest;
import com.appster.dentamatch.network.response.jobs.JobDetailResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.common.HomeActivity;
import com.appster.dentamatch.ui.tracks.TrackJobsDataHelper;
import com.appster.dentamatch.util.Alert;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;

/**
 * Created by Appster on 25/01/17.
 */

public class JobDetailActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener {
    private final int MAP_ZOOM_LEVEL = 7;
    private int jobID;
    private boolean isFromHiredJob;
    private ActivityJobDetailBinding mBinding;
    private GoogleMap mGoogleMap;
    private JobDetailModel mJobDetailModel;

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.pull_in, R.anim.hold_still);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_job_detail);
        setData();

        if (getIntent().hasExtra(Constants.EXTRA_JOB_DETAIL_ID)) {
            jobID = getIntent().getIntExtra(Constants.EXTRA_JOB_DETAIL_ID, 0);
        }
//        if (getIntent().hasExtra(Constants.INTENT_KEY.FROM_WHERE)) {
//
//            isFromHiredJob = getIntent().getBooleanExtra(Constants.INTENT_KEY.FROM_WHERE, false);
//            if (isFromHiredJob) {
//                mBinding.btnApplyJob.setVisibility(View.GONE);
//            }
//
//        }
        mBinding.mapJobDetail.onCreate(savedInstanceState);
        mBinding.mapJobDetail.getMapAsync(this);
    }


    private void setData() {
        mBinding.btnApplyJob.setOnClickListener(JobDetailActivity.this);
        mBinding.jobDetailToolbar.tvToolbarGeneralLeft.setText(getString(R.string.header_job_detail));
        mBinding.cbJobSelection.setOnClickListener(this);
        mBinding.jobDetailToolbar.ivToolBarLeft.setOnClickListener(this);
        mBinding.tvJobDetailDocReadMore.setOnClickListener(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        mGoogleMap.getUiSettings().setCompassEnabled(false);
        mGoogleMap.getUiSettings().setScrollGesturesEnabled(false);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(false);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold_still, R.anim.pull_out);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBinding.mapJobDetail.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.mapJobDetail.onResume();
        getJobDetail();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.mapJobDetail.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBinding.mapJobDetail.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding.mapJobDetail.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mBinding.mapJobDetail.onLowMemory();
    }


    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.iv_tool_bar_left:
                onBackPressed();
                break;

            case R.id.tv_job_detail_doc_read_more:

                if (mBinding.tvJobDetailDocReadMore.getText().toString().equalsIgnoreCase(getString(R.string.txt_read_more))) {
                    mBinding.tvJobDetailDocDescription.setMaxLines(Integer.MAX_VALUE);
                    mBinding.tvJobDetailDocReadMore.setText(R.string.txt_show_less);
                } else {
                    mBinding.tvJobDetailDocDescription.setMaxLines(4);
                    mBinding.tvJobDetailDocReadMore.setText(getString(R.string.txt_read_more));

                }

                break;

            case R.id.cb_job_selection:
                final int status = mJobDetailModel.getIsSaved() == 1 ? 0 : 1;
                if (status == 0) {
                    Alert.createYesNoAlert(JobDetailActivity.this, "OK", "CANCEL", getString(R.string.app_name), "Are you sure you want to unsave the job?", new Alert.OnAlertClickListener() {
                        @Override
                        public void onPositive(DialogInterface dialog) {
                            saveUnSaveJob(jobID, status);
                        }

                        @Override
                        public void onNegative(DialogInterface dialog) {
                            ((CheckBox) v).setChecked(true);
                            dialog.dismiss();
                        }
                    });
                } else {
                    saveUnSaveJob(jobID, status);
                }
                break;

            case R.id.btn_apply_job:
                applyJob();
                break;


            default:
                break;
        }
    }

    private void applyJob() {
        JobApplyRequest request = new JobApplyRequest();
        request.setJobId(jobID);

        processToShowDialog("", getString(R.string.please_wait), null);
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        webServices.applyJob(request).enqueue(new BaseCallback<BaseResponse>(this) {
            @Override
            public void onSuccess(BaseResponse response) {
                if (response.getStatus() == 1) {
                    mBinding.tvJobStatus.setVisibility(View.VISIBLE);
                    mBinding.btnApplyJob.setVisibility(View.GONE);
                    Alert.alert(JobDetailActivity.this, "Congratulations", "You have successfully applied for the job.");
                    TrackJobsDataHelper.getInstance().updateAppliedData();
                } else {
                    mBinding.tvJobStatus.setVisibility(View.GONE);
                    mBinding.btnApplyJob.setVisibility(View.VISIBLE);
                    if (response.getStatusCode() == 202) {
                        Alert.createYesNoAlert(JobDetailActivity.this, "Yes", "No", "Complete your profile", response.getMessage(), new Alert.OnAlertClickListener() {
                            @Override
                            public void onPositive(DialogInterface dialog) {
                                startActivity(new Intent(JobDetailActivity.this, HomeActivity.class)
                                        .putExtra(Constants.EXTRA_FROM_JOB_DETAIL, true)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            }

                            @Override
                            public void onNegative(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                    } else {
                        showToast(response.getMessage());
                    }
                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {

            }
        });
    }

    private void getJobDetail() {
        JobDetailRequest request = new JobDetailRequest();
        request.setJobId(jobID);
        processToShowDialog("", getString(R.string.please_wait), null);
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        webServices.getJobDetail(request).enqueue(new BaseCallback<JobDetailResponse>(this) {
            @Override
            public void onSuccess(JobDetailResponse response) {
                if (response != null) {

                    if (response.getStatus() == 1) {
                        mJobDetailModel = response.getResult();
                        mBinding.layJobDetailHolder.setVisibility(View.VISIBLE);
                        setDetailData(response.getResult());
                    } else {
                        showToast(response.getMessage());
                        mBinding.layJobDetailHolder.setVisibility(View.GONE);
                        mBinding.btnApplyJob.setOnClickListener(null);
                    }
                }
            }


            @Override
            public void onFail(Call<JobDetailResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGD(TAG, "FAiled");
            }
        });
    }

    private void setDetailData(JobDetailModel dataModel) {
        mBinding.tvJobDetailName.setText(dataModel.getJobTitleName());

        if (dataModel.getJobType() == Constants.JOBTYPE.PART_TIME.getValue()) {
            mBinding.tvJobDetailType.setText(getString(R.string.txt_part_time));
            mBinding.tvJobDetailType.setBackgroundResource(R.drawable.job_type_background_part_time);

            ArrayList<String> partTimeDaysArray = new ArrayList<>();
            if (dataModel.getIsMonday() == 1) {
                partTimeDaysArray.add(getString(R.string.txt_monday));
            }

            if (dataModel.getIsTuesday() == 1) {
                partTimeDaysArray.add(getString(R.string.txt_tuesday));
            }

            if (dataModel.getIsWednesday() == 1) {
                partTimeDaysArray.add(getString(R.string.txt_wednesday));
            }

            if (dataModel.getIsThursday() == 1) {
                partTimeDaysArray.add(getString(R.string.txt_thursday));
            }

            if (dataModel.getIsFriday() == 1) {
                partTimeDaysArray.add(getString(R.string.txt_friday));
            }

            if (dataModel.getIsSaturday() == 1) {
                partTimeDaysArray.add(getString(R.string.txt_saturday));
            }

            if (dataModel.getIsSunday() == 1) {
                partTimeDaysArray.add(getString(R.string.txt_sunday));
            }

            String partTimeDays = TextUtils.join(", ", partTimeDaysArray);
            mBinding.tvJobDetailDate.setVisibility(View.VISIBLE);
            mBinding.tvJobDetailDate.setText(partTimeDays);

            final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_BOTTOM, mBinding.tvJobDetailType.getId());
            params.addRule(RelativeLayout.END_OF, mBinding.tvJobDetailType.getId());
            params.setMargins(Utils.dpToPx(this, 12), 0, Utils.dpToPx(this, 10), 0);

            mBinding.tvJobDetailDate.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mBinding.tvJobDetailDate.getLineCount() == 1) {
                        params.addRule(RelativeLayout.ALIGN_BOTTOM, mBinding.tvJobDetailType.getId());
                        mBinding.tvJobDetailDate.setLayoutParams(params);
                    } else {
                        params.addRule(RelativeLayout.ALIGN_BASELINE, mBinding.tvJobDetailType.getId());
                        mBinding.tvJobDetailDate.setLayoutParams(params);
                        mBinding.tvJobDetailDate.setPadding(0, 4, 0, 0);
                    }
                }
            }, 200);

        } else if (dataModel.getJobType() == Constants.JOBTYPE.FULL_TIME.getValue()) {
            mBinding.tvJobDetailType.setBackgroundResource(R.drawable.job_type_background_full_time);
            mBinding.tvJobDetailDate.setVisibility(View.GONE);
            mBinding.tvJobDetailType.setText(getString(R.string.txt_full_time));

        } else if (dataModel.getJobType() == Constants.JOBTYPE.TEMPORARY.getValue()) {
            mBinding.tvJobDetailType.setBackgroundResource(R.drawable.job_type_background_temporary);
            mBinding.tvJobDetailDate.setVisibility(View.GONE);
            mBinding.tvJobDetailType.setText(getString(R.string.txt_temporary));
        }

        String endMessage = dataModel.getJobPostedTimeGap() > 1 ? getString(R.string.txt_days_ago) : getString(R.string.txt_day_ago);
        mBinding.tvJobDocTime.setText(String.valueOf(dataModel.getJobPostedTimeGap()).concat(" ").concat(endMessage));

        mBinding.tvJobDetailDocName.setText(dataModel.getOfficeName());
        mBinding.tvJobDetailDocAddress.setText(dataModel.getAddress());
//        mBinding.tvJobDetailJobOpenings.setText(dataModel.);
//        mBinding.tvOfficeDress.setText(dataModel.g);
//        mBinding.tvOfficeParkingAvailibility.setText(dataModel.get);
//        String startTime = Utils.convertUTCtoLocal(dataModel.getWorkEverydayStart());
//        String endTime = Utils.convertUTCtoLocal(dataModel.getWorkEverydayEnd());
//        mBinding.tvJobDetailOfficeWorkingHours.setText(startTime.concat(" - ").concat(endTime));
        mBinding.tvJobDetailOfficeWorkingHours.setVisibility(View.GONE);
        mBinding.tvJobDetailJobDistance.setText(String.format(Locale.getDefault(), "%.1f", dataModel.getDistance()).concat(getString(R.string.txt_miles)));
        mBinding.tvJobDetailDocOfficeType.setText(dataModel.getOfficeTypeName());

        mBinding.tvJobDetailDocDescription.setText(dataModel.getTemplateDesc());
        mBinding.tvJobDetailDocDescription.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mBinding.tvJobDetailDocDescription.getLineCount() > 4) {
                    mBinding.tvJobDetailDocDescription.setMaxLines(4);
                    mBinding.tvJobDetailDocReadMore.setVisibility(View.VISIBLE);
                    mBinding.tvJobDetailDocReadMore.setOnClickListener(JobDetailActivity.this);

                } else {
                    mBinding.tvJobDetailDocReadMore.setVisibility(View.GONE);
                    mBinding.tvJobDetailDocReadMore.setOnClickListener(null);
                }
            }
        }, 200);


        if (dataModel.getIsApplied() == 0) {

            mBinding.tvJobStatus.setVisibility(View.GONE);
            mBinding.btnApplyJob.setVisibility(View.VISIBLE);
        } else {
            mBinding.btnApplyJob.setVisibility(View.GONE);
            mBinding.tvJobStatus.setVisibility(View.VISIBLE);

        }

        if (dataModel.getIsSaved() == 1) {
            mBinding.cbJobSelection.setChecked(true);
        } else {
            mBinding.cbJobSelection.setChecked(false);
        }

        addMarker(dataModel.getLatitude(), dataModel.getLongitude(), true);
    }


    private void addMarker(double lat, double lng, boolean isSelected) {
        MarkerOptions options = new MarkerOptions();
        options.position(new LatLng(lat, lng));

        if (isSelected) {
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_selected_large));
        } else {
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_unselected_large));
        }

        mGoogleMap.addMarker(options);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), MAP_ZOOM_LEVEL));

    }

    private void saveUnSaveJob(final int JobID, final int status) {
        SaveUnSaveRequest request = new SaveUnSaveRequest();
        request.setJobId(JobID);
        request.setStatus(status);
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        processToShowDialog("", getString(R.string.please_wait), null);
        webServices.saveUnSaveJob(request).enqueue(new BaseCallback<BaseResponse>(this) {
            @Override
            public void onSuccess(BaseResponse response) {
                showToast(response.getMessage());
                if (response.getStatus() == 1) {
                    mJobDetailModel.setIsSaved(status);
                    mBinding.cbJobSelection.setChecked(status == 1);
                    EventBus.getDefault().post(new SaveUnSaveEvent(JobID, status));
                    TrackJobsDataHelper.getInstance().updateSavedData();
                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {

            }
        });
    }
}
