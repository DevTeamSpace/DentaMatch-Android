package com.appster.dentamatch.ui.searchjob;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityJobDetailBinding;
import com.appster.dentamatch.model.JobDetailModel;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.jobs.JobApplyRequest;
import com.appster.dentamatch.network.request.jobs.JobDetailRequest;
import com.appster.dentamatch.network.response.jobs.JobDetailResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;

/**
 * Created by Appster on 25/01/17.
 */

public class JobDetailActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener {
    private final int MAP_ZOOM_LEVEL = 7;
    private int jobID;
    private ActivityJobDetailBinding mBinding;
    private GoogleMap mGoogleMap;
    private boolean mIsDescExpanded, mIsDescCollapsed;

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_job_detail);
        setData();

        if (getIntent().hasExtra(Constants.EXTRA_JOB_DETAIL_ID)) {
            jobID = getIntent().getIntExtra(Constants.EXTRA_JOB_DETAIL_ID, 0);
        }

        mBinding.mapJobDetail.onCreate(savedInstanceState);
        mBinding.mapJobDetail.getMapAsync(this);
        getJobDetail();
        mIsDescCollapsed = true;
    }


    private void setData() {
        mBinding.jobDetailToolbar.tvToolbarGeneralLeft.setText(getString(R.string.header_job_detail));
        mBinding.jobDetailToolbar.ivToolBarLeft.setOnClickListener(this);

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
    protected void onStart() {
        super.onStart();
        mBinding.mapJobDetail.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.mapJobDetail.onResume();
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

    private void cycleTextViewExpansion() {
        int collapsedMaxLines = 4;
        ObjectAnimator animation = ObjectAnimator.ofInt(mBinding.tvJobDetailDocDescription, "maxLines",
                mBinding.tvJobDetailDocDescription.getMaxLines() == collapsedMaxLines ? mBinding.tvJobDetailDocDescription.getLineCount() : collapsedMaxLines);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(2000).start();
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                if(mBinding.tvJobDetailDocReadMore.getText().toString().equalsIgnoreCase(getString(R.string.txt_read_more))){
                    mBinding.tvJobDetailDocReadMore.setText(getString(R.string.text_show_label));
                }else{
                    mBinding.tvJobDetailDocReadMore.setText(getString(R.string.txt_read_more));
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_tool_bar_left:
                onBackPressed();
                break;

            case R.id.tv_job_detail_doc_read_more:
                if(mIsDescCollapsed) {
                    mIsDescCollapsed = false;
                    mBinding.tvJobDetailDocDescription.setMaxLines(Integer.MAX_VALUE);
                    mBinding.tvJobDetailDocReadMore.setText(getString(R.string.text_show_label));
                }else{
                    mIsDescCollapsed = true;
                    mBinding.tvJobDetailDocDescription.setMaxLines(4);
                    mBinding.tvJobDetailDocReadMore.setText(getString(R.string.txt_read_more));
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

        showProgressBar();
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        webServices.applyJob(request).enqueue(new BaseCallback<BaseResponse>(this) {
            @Override
            public void onSuccess(BaseResponse response) {
                showToast(response.getMessage());
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {

            }
        });
    }

    private void getJobDetail() {
        JobDetailRequest request = new JobDetailRequest();
        request.setJobId(jobID);
        showProgressBar();
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        webServices.getJobDetail(request).enqueue(new BaseCallback<JobDetailResponse>(this) {
            @Override
            public void onSuccess(JobDetailResponse response) {
                if (response != null) {

                    if (response.getStatus() == 1) {
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
            params.addRule(RelativeLayout.BELOW, mBinding.tvJobDetailName.getId());
            params.addRule(RelativeLayout.END_OF, mBinding.tvJobDetailType.getId());
            params.setMargins(Utils.dpToPx(this, 12), 0, Utils.dpToPx(this, 10), 0);

            mBinding.tvJobDetailDate.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mBinding.tvJobDetailDate.getLineCount() == 1) {
                        params.addRule(RelativeLayout.ALIGN_BASELINE, mBinding.tvJobDetailType.getId());
                        mBinding.tvJobDetailDate.setLayoutParams(params);
                    } else {
                        mBinding.tvJobDetailDate.setLayoutParams(params);
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
        String startTime = Utils.convertUTCtoLocal(dataModel.getWorkEverydayStart());
        String endTime = Utils.convertUTCtoLocal(dataModel.getWorkEverydayEnd());
        mBinding.tvJobDetailOfficeWorkingHours.setText(startTime.concat(" - ").concat(endTime));
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
            mBinding.btnApplyJob.setOnClickListener(JobDetailActivity.this);
            mBinding.btnApplyJob.setText(getString(R.string.button_apply_for_job));
        } else {
            mBinding.btnApplyJob.setOnClickListener(null);
            mBinding.btnApplyJob.setText(getString(R.string.button_already_applied));
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
}
