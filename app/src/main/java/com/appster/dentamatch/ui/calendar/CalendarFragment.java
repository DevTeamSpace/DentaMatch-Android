package com.appster.dentamatch.ui.calendar;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.FragmentCalendarBinding;
import com.appster.dentamatch.eventbus.JobCancelEvent;
import com.appster.dentamatch.interfaces.OnDateSelected;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.jobs.HiredJobRequest;
import com.appster.dentamatch.network.request.tracks.CancelJobRequest;
import com.appster.dentamatch.network.response.jobs.HiredJobResponse;
import com.appster.dentamatch.network.response.jobs.HiredJobs;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.common.BaseFragment;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;

/**
 * Created by Appster on 23/01/17.
 */

public class CalendarFragment extends BaseFragment implements View.OnClickListener, OnDateSelected {
    private FragmentCalendarBinding mCalendarBinding;
    private LinearLayoutManager mLayoutManager;
    private HiredJobAdapter mJobAdapter;
    private ArrayList<HiredJobs> mAllJobLIst;

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    @Override
    public String getFragmentName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        mCalendarBinding.toolbarCalendar.ivToolBarRight.setVisibility(View.VISIBLE);
        mCalendarBinding.toolbarCalendar.txvToolbarGeneralRight.setVisibility(View.VISIBLE);
        mCalendarBinding.toolbarCalendar.txvToolbarGeneralRight.setCompoundDrawables(ContextCompat.getDrawable(getActivity(), android.R.drawable.btn_plus), null, null, null);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mCalendarBinding.rvBookedJob.setLayoutManager(mLayoutManager);
        mAllJobLIst = new ArrayList<>();
        mJobAdapter = new HiredJobAdapter(getActivity(), mAllJobLIst);
        mCalendarBinding.rvBookedJob.setAdapter(mJobAdapter);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCalendarBinding.customCalendar.setDateSelectedInterface(this);
        getBookedJob(prepareRequest(Calendar.getInstance()), Utils.dateFormetyyyyMMdd(Calendar.getInstance().getTime()));
    }

    private HiredJobRequest prepareRequest(Calendar calendar) {
        HiredJobRequest request = new HiredJobRequest();
        /**
         * to get  the number of days  in month
         */
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Date date = calendar.getTime();
        String startStr = Utils.dateFormetyyyyMMdd(date);
        startStr = startStr.substring(0, startStr.lastIndexOf("-"));
        startStr = startStr + "-01";
        String[] splitEndDate = Utils.dateFormetyyyyMMdd(date).split("-");
        request.setJobStartDate(startStr);
        request.setJobEndDate(splitEndDate[0] + "-" + splitEndDate[1] + "-" + days);

        return request;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_tool_bar_left:
                break;

            case R.id.txv_toolbar_general_right:
                startActivity(new Intent(getActivity(), SetAvailabilityActivity.class));
                break;

            case R.id.iv_tool_bar_right:
                startActivity(new Intent(getActivity(), SetAvailabilityActivity.class));
                break;

            default: break;
        }

    }

    private void getBookedJob(HiredJobRequest request, final String date) {
        showProgressBar(getString(R.string.please_wait));
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.getHiredJob(request).enqueue(new BaseCallback<HiredJobResponse>(getBaseActivity()) {
            @Override
            public void onSuccess(HiredJobResponse response) {
                /**
                 * Once data has been loaded from the filter changes we can dismiss this filter.
                 */
                if (response.getStatus() == 1) {
                    mCalendarBinding.layoutBlankAlert.setVisibility(View.GONE);
                    mCalendarBinding.rvBookedJob.setVisibility(View.VISIBLE);
                    mAllJobLIst = response.getHiredJobResponseData().getJobList();
                    mCalendarBinding.customCalendar.setHiredListData(mAllJobLIst);
                    arrangeJobData(date);

                } else {
                    mCalendarBinding.customCalendar.isFullTimeJob(false);
                    mCalendarBinding.rvBookedJob.setVisibility(View.GONE);
                    mCalendarBinding.layoutBlankAlert.setVisibility(View.VISIBLE);

                    if (mAllJobLIst != null) {
                        mAllJobLIst.clear();
                    }

                    mCalendarBinding.customCalendar.setHiredListData(mAllJobLIst);
                    Utils.showToast(getActivity(), response.getMessage());
                }

            }


            @Override
            public void onFail(Call<HiredJobResponse> call, BaseResponse baseResponse) {
            }
        });

    }

    private void cancelJob(final int jobId, String msg) {
        showProgressBar(getString(R.string.please_wait));

        CancelJobRequest request = new CancelJobRequest();
        request.setCancelReason(msg);
        request.setJobId(jobId);

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        webServices.cancelJob(request).enqueue(new BaseCallback<BaseResponse>((BaseActivity) getActivity()) {

            @Override
            public void onSuccess(BaseResponse response) {
                Toast.makeText(getActivity(), response.getMessage(), Toast.LENGTH_SHORT).show();

                if (response.getStatus() == 1) {
                    mJobAdapter.cancelJob(jobId);

                    if (mJobAdapter.getList().size() > 0) {
                        mCalendarBinding.rvBookedJob.setVisibility(View.VISIBLE);
                        mCalendarBinding.layoutBlankAlert.setVisibility(View.GONE);

                    } else {
                        mCalendarBinding.layoutBlankAlert.setVisibility(View.VISIBLE);
                        mCalendarBinding.rvBookedJob.setVisibility(View.GONE);
                    }

                    for(int i = 0; i < mAllJobLIst.size(); i++){

                        if(mAllJobLIst.get(i).getId() == jobId){
                            mAllJobLIst.remove(i);
                            i = 0;
                        }

                    }

                    mCalendarBinding.customCalendar.setHiredListData(mAllJobLIst);
                }else{
                    showToast(response.getMessage());
                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
            }
        });
    }


    @Override
    public void selectedDate(String date) {
        arrangeJobData(date);
    }

    @Override
    public void onMonthChanged(Calendar cal) {
        getBookedJob(prepareRequest(cal), Utils.dateFormetyyyyMMdd(Calendar.getInstance().getTime()));
    }


    private void arrangeJobData(String date) {
        ArrayList<HiredJobs> selectedDateJobList = new ArrayList<>();
        boolean isFullTime = false;
        try {
            if (mAllJobLIst != null && mAllJobLIst.size() > 0) {

                for (int i = 0; i < mAllJobLIst.size(); i++) {

                    if (mAllJobLIst.get(i).getJobType() == Constants.JOBTYPE.FULL_TIME.getValue()) {
                        isFullTime = true;

                    } else if (mAllJobLIst.get(i).getJobType() == Constants.JOBTYPE.PART_TIME.getValue()) {
                        String day = Utils.getDayOfWeek(date);

                        if (day.equalsIgnoreCase(getString(R.string.txt_full_monday)) && mAllJobLIst.get(i).getIsMonday() == 1) {
                            selectedDateJobList.add(mAllJobLIst.get(i));

                        } else if (day.equalsIgnoreCase(getString(R.string.txt_full_tuesday)) && mAllJobLIst.get(i).getIsTuesday() == 1) {
                            selectedDateJobList.add(mAllJobLIst.get(i));

                        } else if (day.equalsIgnoreCase(getString(R.string.txt_full_wednesday)) && mAllJobLIst.get(i).getIsWednesday() == 1) {
                            selectedDateJobList.add(mAllJobLIst.get(i));

                        } else if (day.equalsIgnoreCase(getString(R.string.txt_full_thursday)) && mAllJobLIst.get(i).getIsThursday() == 1) {
                            selectedDateJobList.add(mAllJobLIst.get(i));

                        } else if (day.equalsIgnoreCase(getString(R.string.txt_full_friday)) && mAllJobLIst.get(i).getIsFriday() == 1) {
                            selectedDateJobList.add(mAllJobLIst.get(i));

                        } else if (day.equalsIgnoreCase(getString(R.string.txt_full_saturday)) && mAllJobLIst.get(i).getIsSaturday() == 1) {
                            selectedDateJobList.add(mAllJobLIst.get(i));

                        } else if (day.equalsIgnoreCase(getString(R.string.txt_full_sunday)) && mAllJobLIst.get(i).getIsSunday() == 1) {
                            selectedDateJobList.add(mAllJobLIst.get(i));

                        }

                    } else if (mAllJobLIst.get(i).getJobType() == Constants.JOBTYPE.TEMPORARY.getValue()) {

                        if(mAllJobLIst.get(i).getTempDates().equalsIgnoreCase(date)){
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

        }catch(Exception e){
            e.printStackTrace();
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
