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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;

/**
 * Created by Appster on 23/01/17.
 * To inject activity reference.
 */

public class CalendarFragment extends BaseLoadingFragment<CalendarViewModel>
        implements View.OnClickListener {

    private FragmentCalendarBinding mCalendarBinding;
    private HiredJobAdapter mJobAdapter;

    @NonNull
    private final CalendarModel mCalendarModel = CalendarModel.Companion.getInstance();

    @NonNull
    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }


    @Override
    public void onResume() {
        super.onResume();
//        mJobAdapter.updateCalendarPosition();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
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
        mCalendarBinding.toolbarCalendar.ivToolBarRight.setVisibility(View.GONE);
        mCalendarBinding.toolbarCalendar.txvToolbarGeneralRight.setVisibility(View.GONE);
        if (getActivity() != null)
            mCalendarBinding.toolbarCalendar.txvToolbarGeneralRight.setCompoundDrawables(ContextCompat.getDrawable(getActivity(), android.R.drawable.btn_plus), null, null, null);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mCalendarBinding.rvBookedJob.setLayoutManager(mLayoutManager);

        mJobAdapter = new HiredJobAdapter(this, mCalendarModel);
        mCalendarBinding.rvBookedJob.setAdapter(mJobAdapter);
        mJobAdapter.updateCalendarPosition();
        viewModel.getCancelJob().observe(this, this::onSuccessCancelJob);
        viewModel.getCalendarModel().observe(this, this::onSuccessRequestCalendarModel);
        viewModel.getHiredJobFailed().observe(this, throwable -> onFailedRequestHiredJob());
    }

    private void onSuccessRequestCalendarModel(@Nullable CalendarModel calendarModel) {
        if (calendarModel != null) {
            mJobAdapter.setCalendarModel(mCalendarModel);
        }
    }

    private void onSuccessCancelJob(@Nullable CalendarModel calendarModel) {
        if (calendarModel != null) {
            mJobAdapter.setCalendarModel(mCalendarModel);
        }
    }

    private void onFailedRequestHiredJob() {
        mJobAdapter.setCalendarModel(mCalendarModel);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
            default:
                break;
        }
    }

    private void getBookedJob(Calendar cal) {
        viewModel.requestHiredJob(cal, mCalendarModel);
    }

    private void cancelJob(int jobId, @NonNull String msg) {
        viewModel.cancelJob(jobId, msg, mCalendarModel);
    }

    @Subscribe
    public void jobCancelled(JobCancelEvent event) {
        if (event != null) {
            cancelJob(event.getJobID(), event.getMsg());
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
