/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.calendar;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appster.dentamatch.R;
import com.appster.dentamatch.base.BaseLoadingFragment;
import com.appster.dentamatch.databinding.FragmentCalendarBinding;
import com.appster.dentamatch.eventbus.JobCancelEvent;
import com.appster.dentamatch.interfaces.OnDateSelected;
import com.appster.dentamatch.network.response.jobs.HiredJobResponse;
import com.appster.dentamatch.network.response.jobs.HiredJobs;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Appster on 23/01/17.
 * To inject activity reference.
 */

public class CalendarFragment extends BaseLoadingFragment<CalendarViewModel>
        implements View.OnClickListener, OnDateSelected {

    private static final String TAG = LogUtils.makeLogTag(CalendarFragment.class);
    private FragmentCalendarBinding mCalendarBinding;
    private HiredJobAdapter mJobAdapter;
    private ArrayList<HiredJobs> mAllJobLIst;

    @NonNull
    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mCalendarBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false);
        initViews();
        return mCalendarBinding.getRoot();
    }

    private void initViews() {
        mCalendarBinding.toolbarCalendar.ivToolBarLeft.setVisibility(View.GONE);
        mCalendarBinding.toolbarCalendar.ivToolBarLeft.setVisibility(View.GONE);
        mCalendarBinding.toolbarCalendar.txvToolbarGeneralRight.setOnClickListener(this);
        mCalendarBinding.toolbarCalendar.tvToolbarGeneralLeft.setText(getString(R.string.header_calendar).toUpperCase());
        mCalendarBinding.toolbarCalendar.ivToolBarRight.setBackgroundResource(R.drawable.ic_plus);
        mCalendarBinding.toolbarCalendar.ivToolBarRight.setOnClickListener(this);
        mCalendarBinding.tvUpdateAvl.setOnClickListener(this);
        mCalendarBinding.toolbarCalendar.ivToolBarRight.setVisibility(View.GONE);
        mCalendarBinding.toolbarCalendar.txvToolbarGeneralRight.setVisibility(View.GONE);
        if (getActivity() != null)
            mCalendarBinding.toolbarCalendar.txvToolbarGeneralRight.setCompoundDrawables(ContextCompat.getDrawable(getActivity(), android.R.drawable.btn_plus), null, null, null);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mCalendarBinding.rvBookedJob.setLayoutManager(mLayoutManager);
        mAllJobLIst = new ArrayList<>();
        mJobAdapter = new HiredJobAdapter(getActivity(), mAllJobLIst);
        mCalendarBinding.rvBookedJob.setAdapter(mJobAdapter);

        viewModel.getCancelJob().observe(this, this::onSuccessCancelJob);
        viewModel.getHiredJob().observe(this, this::onSuccessRequestHiredJob);
        viewModel.getHiredJobFailed().observe(this, throwable -> onFailedRequestHiredJob());
    }

    private void onSuccessCancelJob(@Nullable Integer jobId) {
        if (jobId != null) {
            mJobAdapter.cancelJob(jobId);
            if (mJobAdapter.getList().size() > 0) {
                mCalendarBinding.rvBookedJob.setVisibility(View.VISIBLE);
                mCalendarBinding.layoutBlankAlert.setVisibility(View.GONE);
            } else {
                mCalendarBinding.layoutBlankAlert.setVisibility(View.VISIBLE);
                mCalendarBinding.rvBookedJob.setVisibility(View.GONE);
            }
            for (int i = 0; i < mAllJobLIst.size(); i++) {
                if (mAllJobLIst.get(i).getId() == jobId) {
                    mAllJobLIst.remove(i);
                    i = -1;
                }
            }
            mCalendarBinding.customCalendar.setHiredListData(mAllJobLIst);
        }
    }

    private void onSuccessRequestHiredJob(@Nullable HiredJobResponse response) {
        if (response != null && response.getHiredJobResponseData() != null) {
            mCalendarBinding.layoutBlankAlert.setVisibility(View.GONE);
            mCalendarBinding.rvBookedJob.setVisibility(View.VISIBLE);
            mAllJobLIst = response.getHiredJobResponseData().getJobList();
            mCalendarBinding.customCalendar.setHiredListData(mAllJobLIst);
            arrangeJobData(getCurrentDateString());
        }
    }

    private void onFailedRequestHiredJob() {
        mCalendarBinding.customCalendar.isFullTimeJob(false);
        mCalendarBinding.rvBookedJob.setVisibility(View.GONE);
        mCalendarBinding.layoutBlankAlert.setVisibility(View.VISIBLE);
        if (mAllJobLIst != null) {
            mAllJobLIst.clear();
        }
        mCalendarBinding.customCalendar.setHiredListData(mAllJobLIst);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCalendarBinding.customCalendar.setDateSelectedInterface(this);
        getBookedJob(Calendar.getInstance());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_tool_bar_left:
                break;
            case R.id.txv_toolbar_general_right:
                startActivity(new Intent(getActivity(), SetAvailabilityActivity.class));
                break;
            case R.id.tvUpdateAvl:
                startActivity(new Intent(getActivity(), SetAvailabilityActivity.class));
                break;
            default:
                break;
        }
    }

    private void getBookedJob(Calendar cal) {
        viewModel.requestHiredJob(cal);
    }

    private void cancelJob(int jobId, @NonNull String msg) {
        viewModel.cancelJob(jobId, msg);
    }

    @Override
    public void selectedDate(String date) {
        arrangeJobData(date);
    }

    @Override
    public void onMonthChanged(Calendar cal) {
        getBookedJob(cal);
    }

    @NonNull
    private String getCurrentDateString() {
        return Utils.dateFormetyyyyMMdd(Calendar.getInstance().getTime());
    }

    private void arrangeJobData(@NonNull String date) {
        ArrayList<HiredJobs> selectedDateJobList = new ArrayList<>();
        boolean isFullTime = false;
        try {
            if (mAllJobLIst != null && mAllJobLIst.size() > 0) {
                for (int i = 0; i < mAllJobLIst.size(); i++) {
                    if (mAllJobLIst.get(i).getJobType() == Constants.JOBTYPE.FULL_TIME.getValue()) {
                        isFullTime = true;
                    } else if (mAllJobLIst.get(i).getJobType() == Constants.JOBTYPE.PART_TIME.getValue()) {
                        Date jobHireDate = Utils.getDate(mAllJobLIst.get(i).getJobDate(), Constants.DateFormat.YYYYMMDD);
                        Date currentDate = Utils.getDate(date, Constants.DateFormat.YYYYMMDD);
                        if (jobHireDate != null && jobHireDate.compareTo(currentDate) <= 0) {
                            String day = Utils.getDayOfWeek(date);
                            if ((day.equalsIgnoreCase(getString(R.string.txt_full_monday)) && mAllJobLIst.get(i).getIsMonday() == 1)
                                    || (day.equalsIgnoreCase(getString(R.string.txt_full_tuesday)) && mAllJobLIst.get(i).getIsTuesday() == 1)
                                    || (day.equalsIgnoreCase(getString(R.string.txt_full_wednesday)) && mAllJobLIst.get(i).getIsWednesday() == 1)
                                    || (day.equalsIgnoreCase(getString(R.string.txt_full_thursday)) && mAllJobLIst.get(i).getIsThursday() == 1)
                                    || (day.equalsIgnoreCase(getString(R.string.txt_full_friday)) && mAllJobLIst.get(i).getIsFriday() == 1)
                                    || (day.equalsIgnoreCase(getString(R.string.txt_full_saturday)) && mAllJobLIst.get(i).getIsSaturday() == 1)
                                    || (day.equalsIgnoreCase(getString(R.string.txt_full_sunday)) && mAllJobLIst.get(i).getIsSunday() == 1)) {
                                selectedDateJobList.add(mAllJobLIst.get(i));
                            }
                        }
                    } else if (mAllJobLIst.get(i).getJobType() == Constants.JOBTYPE.TEMPORARY.getValue()) {
                        if (mAllJobLIst.get(i).getTempDates().equalsIgnoreCase(date)) {
                            selectedDateJobList.add(mAllJobLIst.get(i));
                        }
                    }
                }
                if (isFullTime) {
                    mCalendarBinding.customCalendar.isFullTimeJob(true);
                }
                if (selectedDateJobList.size() > 0) {
                    mCalendarBinding.layoutBlankAlert.setVisibility(View.GONE);
                    mCalendarBinding.customCalendar.isFullTimeJob(isFullTime);
                    mCalendarBinding.rvBookedJob.setFocusable(true);
                    mCalendarBinding.rvBookedJob.setVisibility(View.VISIBLE);
                    mJobAdapter.setJobList(selectedDateJobList);
                } else {
                    mCalendarBinding.rvBookedJob.setVisibility(View.GONE);
                    mCalendarBinding.layoutBlankAlert.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            LogUtils.LOGE(TAG, e.getMessage());
        }
    }

    @Subscribe
    public void jobCancelled(JobCancelEvent event) {
        if (event != null) {
            for (HiredJobs model : mAllJobLIst) {
                if (model.getId() == event.getJobID()) {
                    cancelJob(event.getJobID(), event.getMsg());
                    break;
                }
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }
}
