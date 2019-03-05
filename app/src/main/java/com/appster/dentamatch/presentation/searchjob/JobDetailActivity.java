/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.searchjob;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.appster.dentamatch.R;
import com.appster.dentamatch.base.BaseLoadingActivity;
import com.appster.dentamatch.databinding.ActivityJobDetailBinding;
import com.appster.dentamatch.databinding.ItemDateTvBinding;
import com.appster.dentamatch.eventbus.SaveUnSaveEvent;
import com.appster.dentamatch.model.JobDetailModel;
import com.appster.dentamatch.base.BaseResponse;
import com.appster.dentamatch.network.response.jobs.JobDetailResponse;
import com.appster.dentamatch.presentation.common.HomeActivity;
import com.appster.dentamatch.presentation.tracks.TrackJobsDataHelper;
import com.appster.dentamatch.util.Alert;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.StringUtils;
import com.appster.dentamatch.util.TextViewUtils;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.CustomTextView;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;

import kotlin.Pair;

/**
 * Created by Appster on 25/01/17.
 * User Interface to view job detail-.
 */

public class JobDetailActivity extends BaseLoadingActivity<JobDetailViewModel>
        implements OnMapReadyCallback, View.OnClickListener {

    private static final String TAG = LogUtils.makeLogTag(JobDetailActivity.class);
    private final int DESC_MAX_LINES = 4;
    private final int JOB_SAVED = 1;
    private static final int DURATION_TIME_0 = 0;
    private static final int DURATION_TIME_1 = 1;
    private static final int VIEW_DELAY_TIME = 200;

    private int jobId;
    private ActivityJobDetailBinding mBinding;
    private GoogleMap mGoogleMap;
    private JobDetailModel mJobDetailModel;
    private double mMatchPercent;
    private int mNoOfItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.pull_in, R.anim.hold_still);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_job_detail);
        initViews();

        if (getIntent().hasExtra(Constants.EXTRA_JOB_DETAIL_ID)) {
            jobId = getIntent().getIntExtra(Constants.EXTRA_JOB_DETAIL_ID, 0);
            mMatchPercent = getIntent().getDoubleExtra(Constants.EXTRA_MATCHES_PERCENT, 0);
        }
        mBinding.mapJobDetail.onCreate(savedInstanceState);
        mBinding.mapJobDetail.getMapAsync(this);

        viewModel.getApplyJobResponse().observe(this, this::onSuccessApplyJob);
        viewModel.getJobDetailResponse().observe(this, this::onSuccessJobDetailResponse);
        viewModel.getSaveUnSave().observe(this, this::onSuccessSaveUnSave);
        viewModel.getFailedSaveUnSave().observe(this, this::onFailedSaveUnSave);
    }

    private void onFailedSaveUnSave(@Nullable Integer status) {
        mBinding.cbJobSelection.setChecked(!(Integer.valueOf(1).equals(status)));
    }

    private void onSuccessSaveUnSave(@Nullable Pair<Integer, Integer> result) {
        if (result != null && result.getFirst() != null && result.getSecond() != null) {
            int jobId = result.getFirst();
            int status = result.getSecond();
            mJobDetailModel.setIsSaved(status);
            mBinding.cbJobSelection.setChecked(status == 1);
            EventBus.getDefault().post(new SaveUnSaveEvent(jobId, status));
            TrackJobsDataHelper.getInstance().updateSavedData();
        }
    }

    private void onSuccessJobDetailResponse(@Nullable JobDetailResponse response) {
        if (response != null) {
            if (response.getStatus() == 1) {
                mBinding.tvJobNotFound.setVisibility(View.GONE);
                mJobDetailModel = response.getResult();
                mBinding.layJobDetailHolder.setVisibility(View.VISIBLE);
                if (response.getResult() != null) {
                    setDetailData(response.getResult());
                }
            } else if (response.getStatusCode() == 201) {
                mBinding.tvJobNotFound.setVisibility(View.VISIBLE);
                mBinding.layJobDetailHolder.setVisibility(View.GONE);
                mBinding.btnApplyJob.setVisibility(View.GONE);
            } else {
                mBinding.tvJobNotFound.setVisibility(View.GONE);
                showToast(response.getMessage());
                mBinding.layJobDetailHolder.setVisibility(View.GONE);
                mBinding.btnApplyJob.setVisibility(View.GONE);
                mBinding.btnApplyJob.setOnClickListener(null);
            }
        }
    }

    private void onSuccessApplyJob(@Nullable BaseResponse response) {
        if (response != null) {
            if (response.getStatus() == 1) {
                onJobApplied();
            } else {
                onJobAccepted(response);
            }
        }
    }

    private void onJobAccepted(@NotNull BaseResponse response) {
        mBinding.tvJobStatus.setVisibility(View.GONE);
        mBinding.btnApplyJob.setVisibility(View.VISIBLE);
        if (response.getStatusCode() == 202) {
            Alert.createYesNoAlert(JobDetailActivity.this,
                    getString(R.string.yes),
                    getString(R.string.no),
                    getString(R.string.txt_complete_profile),
                    response.getMessage(),
                    new Alert.OnAlertClickListener() {
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

    private void onJobApplied() {
        mBinding.tvJobStatus.setVisibility(View.VISIBLE);
        mBinding.btnApplyJob.setVisibility(View.GONE);
        mBinding.tvJobStatus.setText(getString(R.string.txt_applied));
        Alert.alert(JobDetailActivity.this,
                getString(R.string.txt_congratulations),
                getString(R.string.msg_successfully_applied_job));
        TrackJobsDataHelper.getInstance().updateAppliedData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.hasExtra(Constants.EXTRA_JOB_DETAIL_ID)) {
            jobId = getIntent().getIntExtra(Constants.EXTRA_JOB_DETAIL_ID, 0);
        }
    }

    private void initViews() {
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

        if (jobId != 0) {
            getJobDetail();
        }
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
        int JOB_UNSAVED = 0;
        switch (v.getId()) {
            case R.id.iv_tool_bar_left:
                onBackPressed();
                break;
            case R.id.tv_job_detail_doc_read_more:
                if (mBinding.tvJobDetailDocReadMore.getText().toString().equalsIgnoreCase(getString(R.string.txt_read_more))) {
                    mBinding.tvJobDetailDocDescription.setMaxLines(Integer.MAX_VALUE);
                    mBinding.tvJobDetailDocReadMore.setText(R.string.txt_show_less);
                } else {
                    mBinding.tvJobDetailDocDescription.setMaxLines(DESC_MAX_LINES);
                    mBinding.tvJobDetailDocReadMore.setText(getString(R.string.txt_read_more));
                }
                break;
            case R.id.cb_job_selection:
                final int status = mJobDetailModel.getIsSaved() == JOB_SAVED ? JOB_UNSAVED : JOB_SAVED;

                if (status == JOB_UNSAVED) {
                    Alert.createYesNoAlert(JobDetailActivity.this,
                            getString(R.string.txt_ok),
                            getString(R.string.txt_cancel),
                            getString(R.string.txt_alert_title),
                            getString(R.string.msg_unsave_warning),
                            new Alert.OnAlertClickListener() {

                                @Override
                                public void onPositive(DialogInterface dialog) {
                                    saveUnSaveJob(jobId, status);
                                }

                                @Override
                                public void onNegative(DialogInterface dialog) {
                                    ((CheckBox) v).setChecked(true);
                                    dialog.dismiss();
                                }
                            });
                } else {
                    saveUnSaveJob(jobId, status);
                }
                break;
            case R.id.btn_apply_job:
                if (PreferenceUtil.getUserModel().getIsCompleted() == Constants.PROFILE_COMPLETED_STATUS
                        || PreferenceUtil.getUserModel().getProfileCompleted() == Constants.PROFILE_COMPLETED_STATUS) {
                    applyJob();
                } else {
                    Intent intent = new Intent(getApplicationContext(), CompleteProfileDialogActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_job_detail_office_read_more:
                if (mBinding.tvJobDetailOfficeReadMore.getText().toString().equalsIgnoreCase(getString(R.string.txt_read_more))) {
                    mBinding.tvJobDetailOfficeDescription.setMaxLines(Integer.MAX_VALUE);
                    mBinding.tvJobDetailOfficeReadMore.setText(R.string.txt_show_less);
                } else {
                    mBinding.tvJobDetailOfficeDescription.setMaxLines(DESC_MAX_LINES);
                    mBinding.tvJobDetailOfficeReadMore.setText(getString(R.string.txt_read_more));
                }
                break;
            default:
                break;
        }
    }

    private void applyJob() {
        viewModel.applyJob(jobId);
    }

    private void getJobDetail() {
        viewModel.requestJobDetail(jobId);
    }

    private void setDetailData(final JobDetailModel dataModel) {
        try {
            mBinding.tvJobDetailName.setText(dataModel.getJobTitleName());

            if (dataModel.getJobType() == Constants.JOBTYPE.PART_TIME.getValue()) {
                mBinding.tvJobDetailType.setText(getString(R.string.txt_part_time));
                mBinding.tvJobDetailType.setBackgroundResource(R.drawable.job_type_background_part_time);

                ArrayList<String> partTimeDaysArray = new ArrayList<>();
                int ADDED_PART_TIME = 1;
                if (dataModel.getIsMonday() == ADDED_PART_TIME) {
                    partTimeDaysArray.add(getString(R.string.mon));
                }
                if (dataModel.getIsTuesday() == ADDED_PART_TIME) {
                    partTimeDaysArray.add(getString(R.string.tue));
                }
                if (dataModel.getIsWednesday() == ADDED_PART_TIME) {
                    partTimeDaysArray.add(getString(R.string.wed));
                }
                if (dataModel.getIsThursday() == ADDED_PART_TIME) {
                    partTimeDaysArray.add(getString(R.string.thu));
                }
                if (dataModel.getIsFriday() == ADDED_PART_TIME) {
                    partTimeDaysArray.add(getString(R.string.fri));
                }
                if (dataModel.getIsSaturday() == ADDED_PART_TIME) {
                    partTimeDaysArray.add(getString(R.string.sat));
                }
                if (dataModel.getIsSunday() == ADDED_PART_TIME) {
                    partTimeDaysArray.add(getString(R.string.sun));
                }
                String partTimeDays = TextUtils.join(", ", partTimeDaysArray);
                mBinding.tvJobDetailDate.setVisibility(View.VISIBLE);
                mBinding.tvJobDetailDate.setText(partTimeDays);
            } else if (dataModel.getJobType() == Constants.JOBTYPE.FULL_TIME.getValue()) {
                mBinding.tvJobDetailType.setBackgroundResource(R.drawable.job_type_background_full_time);
                mBinding.tvJobDetailDate.setVisibility(View.GONE);
                mBinding.tvJobDetailType.setText(getString(R.string.txt_full_time));
            } else if (dataModel.getJobType() == Constants.JOBTYPE.TEMPORARY.getValue()) {
                mBinding.tvJobDetailType.setBackgroundResource(R.drawable.job_type_background_temporary);
                mBinding.tvJobDetailDate.setVisibility(View.GONE);
                mBinding.tvJobDetailType.setText(getString(R.string.txt_temporary));
                final ArrayList<String> tempDates = new ArrayList<>();

                if (dataModel.getJobTypeDates() != null && dataModel.getJobTypeDates().size() > 0) {
                    for (int i = 0; i < dataModel.getJobTypeDates().size(); i++) {

                        if (Utils.parseDateForTemp(dataModel.getJobTypeDates().get(i)) != null) {
                            tempDates.add(Utils.parseDateForTempWitCom(dataModel.getJobTypeDates().get(i)));
                        }
                    }
                    mBinding.tempDateContainer.removeAllViews();
                    for (String date : tempDates) {
                        final ItemDateTvBinding dateTvBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_date_tv, null, false);
                        dateTvBinding.tvTempDate.setText(date);
                        FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(Utils.dpToPx(this, (int) getResources().getDimension(R.dimen.date_tag_width)), FlexboxLayout.LayoutParams.WRAP_CONTENT);
                        dateTvBinding.tvTempDate.setLayoutParams(lp);
                        mBinding.tempDateContainer.addView(dateTvBinding.tvTempDate);
                    }
                    mBinding.tempDateContainer.setVisibility(View.VISIBLE);
                    mBinding.tempDateContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            mBinding.tempDateContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            if (mBinding.tempDateContainer.getFlexLines().size() > Constants.NO_OF_DATE_LINES) {
                                mNoOfItems = 0;
                                for (int index = 0; index < Constants.NO_OF_DATE_LINES; index++)
                                    mNoOfItems += mBinding.tempDateContainer.getFlexLines().get(index).getItemCount();
                                showLess(tempDates);
                            }
                        }
                    });
                    mBinding.tvHwoLabel.setVisibility(View.VISIBLE);
                    mBinding.tvHwoValue.setVisibility(View.VISIBLE);
                    mBinding.tvHwoValue.setText(StringUtils.getPayRate(dataModel.getPayRate()));
                    TextViewUtils.setTextColor(mBinding.tvHwoValue, R.color.bold_red);
                    TextViewUtils.setCustomFont(mBinding.tvHwoValue, R.string.font_bold);
                }
            }
            if (dataModel.getJobPostedTimeGap() == DURATION_TIME_0) {
                mBinding.tvJobDocTime.setText(getString(R.string.text_todays));
            } else {
                String endMessage = dataModel.getJobPostedTimeGap() > DURATION_TIME_1 ? getString(R.string.txt_days_ago) : getString(R.string.txt_day_ago);
                mBinding.tvJobDocTime.setText(String.valueOf(dataModel.getJobPostedTimeGap()).concat(" ").concat(endMessage));
            }
            mBinding.tvJobDetailDocName.setText(dataModel.getOfficeName());
            mBinding.tvJobDetailDocAddress.setText(dataModel.getAddress());
            mBinding.tvJobDetailJobDistance.setText(R.string.txt_view_on_map);
            mBinding.tvJobDetailJobDistance.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_blue, 0, 0, 0);
            mBinding.tvJobDetailJobDistance.setCompoundDrawablePadding(5);

            mBinding.tvJobDetailJobDistance.setTextColor(ContextCompat.getColor(this, R.color.cerulean_color));
            mBinding.tvMap.setVisibility(View.GONE);
            mBinding.mapJobDetail.setVisibility(View.GONE);
            mBinding.lineMap.setVisibility(View.VISIBLE);
            if (dataModel.getPercentaSkillsMatch() != null && !TextUtils.isEmpty(dataModel.getPercentaSkillsMatch())) {
                mMatchPercent = Double.parseDouble(dataModel.getPercentaSkillsMatch());
            }
            if (mMatchPercent > 0) {
                mBinding.tvMatchesPercent.setText(String.format(Locale.getDefault(), "%.2f", mMatchPercent).concat("%"));
                mBinding.tvMatchesPercent.setVisibility(View.VISIBLE);
                mBinding.tvMatch.setVisibility(View.VISIBLE);
            }
            mBinding.tvJobDetailJobDistance.setOnClickListener(view -> {
                String uri = "https://www.google.com/maps?q=" + dataModel.getLatitude() + "," + dataModel.getLongitude();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            });
            mBinding.tvJobDetailDocOfficeType.setText(dataModel.getOfficeTypeName());
            mBinding.tvJobDetailDocDescription.setText(dataModel.getTemplateDesc());
            mBinding.tvJobDetailDocDescription.postDelayed(() -> {
                if (mBinding.tvJobDetailDocDescription.getLineCount() > DESC_MAX_LINES) {
                    mBinding.tvJobDetailDocDescription.setMaxLines(DESC_MAX_LINES);
                    mBinding.tvJobDetailDocReadMore.setVisibility(View.VISIBLE);
                    mBinding.tvJobDetailDocReadMore.setText(getString(R.string.txt_read_more));
                    mBinding.tvJobDetailDocReadMore.setOnClickListener(JobDetailActivity.this);
                } else {
                    mBinding.tvJobDetailDocReadMore.setVisibility(View.GONE);
                    mBinding.tvJobDetailDocReadMore.setOnClickListener(null);
                }
            }, VIEW_DELAY_TIME);
            mBinding.tvJobDetailOfficeDescription.setText(dataModel.getOfficeDesc());
            mBinding.tvJobDetailOfficeDescription.postDelayed(() -> {
                if (mBinding.tvJobDetailOfficeDescription.getLineCount() > DESC_MAX_LINES) {
                    mBinding.tvJobDetailOfficeDescription.setMaxLines(DESC_MAX_LINES);
                    mBinding.tvJobDetailOfficeReadMore.setVisibility(View.VISIBLE);
                    mBinding.tvJobDetailOfficeReadMore.setOnClickListener(JobDetailActivity.this);
                } else {
                    mBinding.tvJobDetailOfficeReadMore.setVisibility(View.GONE);
                    mBinding.tvJobDetailOfficeReadMore.setOnClickListener(null);
                }
            }, VIEW_DELAY_TIME);

            if (dataModel.getIsApplied() != 0) {
                switch (Constants.JOBSTATUS.values()[dataModel.getIsApplied() - 1]) {
                    case APPLIED:
                        mBinding.tvJobStatus.setText(getString(R.string.txt_applied));
                        mBinding.tvJobStatus.setTextColor(ContextCompat.getColor(this, R.color.light_moss_green));
                        mBinding.tvJobStatus.setVisibility(View.VISIBLE);
                        mBinding.btnApplyJob.setVisibility(View.GONE);
                        break;
                    case INVITED:
                        mBinding.tvJobStatus.setText(getString(R.string.txt_invited));
                        mBinding.tvJobStatus.setTextColor(ContextCompat.getColor(this, R.color.light_moss_green));
                        mBinding.tvJobStatus.setVisibility(View.VISIBLE);
                        mBinding.btnApplyJob.setVisibility(View.GONE);
                        break;
                    case SHORTLISTED:
                        mBinding.tvJobStatus.setText(getString(R.string.txt_shortlisted));
                        mBinding.tvJobStatus.setTextColor(ContextCompat.getColor(this, R.color.light_moss_green));
                        mBinding.tvJobStatus.setVisibility(View.VISIBLE);
                        mBinding.btnApplyJob.setVisibility(View.GONE);
                        break;
                    case HIRED:
                        mBinding.tvJobStatus.setText(getString(R.string.txt_hired));
                        mBinding.tvJobStatus.setTextColor(ContextCompat.getColor(this, R.color.light_moss_green));
                        mBinding.tvJobStatus.setVisibility(View.VISIBLE);
                        mBinding.btnApplyJob.setVisibility(View.GONE);
                        break;
                    case REJECTED:
                        mBinding.tvJobStatus.setText(getString(R.string.txt_rejected));
                        mBinding.tvJobStatus.setTextColor(ContextCompat.getColor(this, R.color.red_color));
                        mBinding.tvJobStatus.setVisibility(View.VISIBLE);
                        mBinding.btnApplyJob.setVisibility(View.GONE);
                        break;
                    case CANCELLED:
                        mBinding.tvJobStatus.setText(getString(R.string.txt_cancelled));
                        mBinding.tvJobStatus.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                        mBinding.tvJobStatus.setVisibility(View.VISIBLE);
                        if (dataModel.getJobType() == Constants.JOBTYPE.TEMPORARY.getValue()) {
                            mBinding.btnApplyJob.setVisibility(View.GONE);
                        } else {
                            mBinding.btnApplyJob.setVisibility(View.VISIBLE);
                        }
                        break;
                    default:
                        mBinding.tvJobStatus.setVisibility(View.GONE);
                        mBinding.btnApplyJob.setVisibility(View.VISIBLE);
                        break;
                }
            } else {
                mBinding.tvJobStatus.setVisibility(View.GONE);
                mBinding.btnApplyJob.setVisibility(View.VISIBLE);
            }
            if (dataModel.getIsSaved() == JOB_SAVED) {
                mBinding.cbJobSelection.setChecked(true);
            } else {
                mBinding.cbJobSelection.setChecked(false);
            }
            if (dataModel.getWorkEverydayStart() != null && dataModel.getWorkEverydayEnd() != null) {
                mBinding.tvJobDetailFull.setVisibility(View.VISIBLE);

                SpannableStringBuilder SpanBuilder = new SpannableStringBuilder(getString(R.string.txt_all_days))
                        .append(Utils.convertUTCtoLocal(dataModel.getWorkEverydayStart()))
                        .append(getString(R.string.hyphen))
                        .append(Utils.convertUTCtoLocal(dataModel.getWorkEverydayEnd()));

                SpanBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mBinding.tvJobDetailFull.setText(SpanBuilder);
            } else {
                if (dataModel.getMondayStart() != null && dataModel.getMondayEnd() != null) {
                    mBinding.tvJobDetailMon.setVisibility(View.VISIBLE);
                    SpannableStringBuilder SpanBuilder = new SpannableStringBuilder(getString(R.string.txt_monday_colon))
                            .append(Utils.convertUTCtoLocal(dataModel.getMondayStart()))
                            .append(getString(R.string.hyphen))
                            .append(Utils.convertUTCtoLocal(dataModel.getMondayEnd()));

                    SpanBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mBinding.tvJobDetailMon.setText(SpanBuilder);
                }
                if (dataModel.getTuesdayStart() != null && dataModel.getTuesdayEnd() != null) {
                    mBinding.tvJobDetailTue.setVisibility(View.VISIBLE);
                    SpannableStringBuilder SpanBuilder = new SpannableStringBuilder(getString(R.string.txt_tuesday_colon))
                            .append(Utils.convertUTCtoLocal(dataModel.getTuesdayStart()))
                            .append(getString(R.string.hyphen))
                            .append(Utils.convertUTCtoLocal(dataModel.getTuesdayEnd()));

                    SpanBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mBinding.tvJobDetailTue.setText(SpanBuilder);
                }
                if (dataModel.getWednesdayStart() != null && dataModel.getWednesdayEnd() != null) {
                    mBinding.tvJobDetailWed.setVisibility(View.VISIBLE);
                    SpannableStringBuilder SpanBuilder = new SpannableStringBuilder(getString(R.string.txt_wednesday_colon))
                            .append(Utils.convertUTCtoLocal(dataModel.getWednesdayStart()))
                            .append(getString(R.string.hyphen))
                            .append(Utils.convertUTCtoLocal(dataModel.getWednesdayEnd()));

                    SpanBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mBinding.tvJobDetailWed.setText(SpanBuilder);
                }
                if (dataModel.getThursdayStart() != null && dataModel.getThursdayEnd() != null) {
                    mBinding.tvJobDetailThu.setVisibility(View.VISIBLE);
                    SpannableStringBuilder SpanBuilder = new SpannableStringBuilder(getString(R.string.txt_thursday_colon))
                            .append(Utils.convertUTCtoLocal(dataModel.getThursdayStart()))
                            .append(getString(R.string.hyphen))
                            .append(Utils.convertUTCtoLocal(dataModel.getThursdayEnd()));

                    SpanBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mBinding.tvJobDetailThu.setText(SpanBuilder);
                }
                if (dataModel.getFridayStart() != null && dataModel.getFridayEnd() != null) {
                    mBinding.tvJobDetailFri.setVisibility(View.VISIBLE);
                    SpannableStringBuilder SpanBuilder = new SpannableStringBuilder(getString(R.string.txt_friday_colon))
                            .append(Utils.convertUTCtoLocal(dataModel.getFridayStart()))
                            .append(getString(R.string.hyphen))
                            .append(Utils.convertUTCtoLocal(dataModel.getFridayEnd()));

                    SpanBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mBinding.tvJobDetailFri.setText(SpanBuilder);
                }
                if (dataModel.getSaturdayStart() != null && dataModel.getSaturdayEnd() != null) {
                    mBinding.tvJobDetailSat.setVisibility(View.VISIBLE);
                    SpannableStringBuilder SpanBuilder = new SpannableStringBuilder(getString(R.string.txt_saturday_colon))
                            .append(Utils.convertUTCtoLocal(dataModel.getSaturdayStart()))
                            .append(getString(R.string.hyphen))
                            .append(Utils.convertUTCtoLocal(dataModel.getSaturdayEnd()));

                    SpanBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mBinding.tvJobDetailSat.setText(SpanBuilder);
                }
                if (dataModel.getSundayStart() != null && dataModel.getSundayEnd() != null) {
                    mBinding.tvJobDetailSun.setVisibility(View.VISIBLE);
                    SpannableStringBuilder SpanBuilder = new SpannableStringBuilder(getString(R.string.txt_sunday_colon))
                            .append(Utils.convertUTCtoLocal(dataModel.getSundayStart()))
                            .append(getString(R.string.hyphen))
                            .append(Utils.convertUTCtoLocal(dataModel.getSundayEnd()));

                    SpanBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mBinding.tvJobDetailSun.setText(SpanBuilder);
                }
            }
            RelativeLayout.LayoutParams officeTypeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            officeTypeParams.addRule(RelativeLayout.ALIGN_START, mBinding.tvOfficeLocationLabel.getId());
            officeTypeParams.addRule(RelativeLayout.BELOW, mBinding.tvJobDetailOfficeTypeLabel.getId());

            if (dataModel.getNoOfJobs() != 0) {
                officeTypeParams.setMargins(getResources().getInteger(R.integer.margin_0),
                        getResources().getInteger(R.integer.margin_0),
                        getResources().getInteger(R.integer.margin_0),
                        getResources().getInteger(R.integer.margin_0));
                mBinding.tvJobDetailDocOfficeType.setLayoutParams(officeTypeParams);
                mBinding.tvJobDetailJobOpenings.setVisibility(View.VISIBLE);
                mBinding.tvJobDetailTotalJobLabel.setVisibility(View.VISIBLE);
                mBinding.tvJobDetailJobOpenings.setText(String.valueOf(dataModel.getNoOfJobs()));
            } else {
                officeTypeParams.setMargins(getResources().getInteger(R.integer.margin_0),
                        getResources().getInteger(R.integer.margin_0),
                        getResources().getInteger(R.integer.margin_0),
                        Utils.dpToPx(this, getResources().getInteger(R.integer.margin_20)));
                mBinding.tvJobDetailDocOfficeType.setLayoutParams(officeTypeParams);
                mBinding.tvJobDetailJobOpenings.setVisibility(View.GONE);
                mBinding.tvJobDetailTotalJobLabel.setVisibility(View.GONE);
            }
            addMarker(dataModel.getLatitude(), dataModel.getLongitude());
        } catch (Exception e) {
            LogUtils.LOGE(TAG, e.getMessage());
        }
    }

    private void showMore(final ArrayList<String> tempDates) {
        for (int index = mNoOfItems - 1; index < tempDates.size(); index++) {
            final ItemDateTvBinding dateTvBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_date_tv, null, false);
            dateTvBinding.tvTempDate.setText(tempDates.get(index));
            FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(Utils.dpToPx(this, (int) getResources().getDimension(R.dimen.date_tag_width)), FlexboxLayout.LayoutParams.WRAP_CONTENT);
            dateTvBinding.tvTempDate.setLayoutParams(lp);
            mBinding.tempDateContainer.addView(dateTvBinding.tvTempDate, mBinding.tempDateContainer.getChildCount() - 1);
        }
        CustomTextView customTextView = (CustomTextView) mBinding.tempDateContainer.getFlexItemAt(mBinding.tempDateContainer.getChildCount() - 1);
        customTextView.setText(getString(R.string.show_less));
        customTextView.setOnClickListener(view -> showLess(tempDates));
    }

    private void showLess(final ArrayList<String> tempDates) {
        mBinding.tempDateContainer.removeViews(mNoOfItems - 1, tempDates.size() - (mNoOfItems - 1));
        CustomTextView customTextView = (CustomTextView) mBinding.tempDateContainer.getFlexItemAt(mBinding.tempDateContainer.getChildCount() - 1);
        if (customTextView.getText().toString().equalsIgnoreCase(getString(R.string.show_less))) {
            customTextView.setText(getString(R.string.show_more));
            customTextView.setOnClickListener(view -> showMore(tempDates));
            return;
        }
        final ItemDateTvBinding dateTvBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_date_tv, null, false);
        dateTvBinding.tvTempDate.setBackground(ContextCompat.getDrawable(this, R.drawable.more_less_rounded_corners));
        dateTvBinding.tvTempDate.setText(getString(R.string.show_more));
        dateTvBinding.tvTempDate.setTextColor(ContextCompat.getColor(this, R.color.txt_clr));
        FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(Utils.dpToPx(this, (int) getResources().getDimension(R.dimen.date_tag_width)), FlexboxLayout.LayoutParams.WRAP_CONTENT);
        dateTvBinding.tvTempDate.setLayoutParams(lp);
        mBinding.tempDateContainer.addView(dateTvBinding.tvTempDate);
        dateTvBinding.tvTempDate.setOnClickListener(view -> showMore(tempDates));
    }

    private void addMarker(double lat, double lng) {
        MarkerOptions options = new MarkerOptions();
        options.position(new LatLng(lat, lng));

        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_selected_large));

        mGoogleMap.addMarker(options);
        int MAP_ZOOM_LEVEL = 7;
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), MAP_ZOOM_LEVEL));
    }

    private void saveUnSaveJob(final int JobID, final int status) {
        viewModel.saveUnSaveJob(JobID, status);
    }
}
