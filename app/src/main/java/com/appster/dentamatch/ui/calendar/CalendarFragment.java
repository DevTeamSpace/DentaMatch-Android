package com.appster.dentamatch.ui.calendar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.appster.dentamatch.R;
import com.appster.dentamatch.adapters.JobListAdapter;
import com.appster.dentamatch.databinding.FragmentCalendarBinding;
import com.appster.dentamatch.model.JobDataReceivedEvent;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.calendar.GetAvailabilityRequest;
import com.appster.dentamatch.network.request.jobs.HiredJobRequest;
import com.appster.dentamatch.network.request.jobs.SearchJobRequest;
import com.appster.dentamatch.network.request.tracks.CancelJobRequest;
import com.appster.dentamatch.network.response.jobs.HiredJobResponse;
import com.appster.dentamatch.network.response.jobs.HiredJobs;
import com.appster.dentamatch.network.response.jobs.SearchJobResponse;
import com.appster.dentamatch.network.response.profile.ProfileResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.common.BaseFragment;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;

/**
 * Created by Appster on 23/01/17.
 */

public class CalendarFragment extends BaseFragment implements View.OnClickListener, CalendarViewEvent.OnDateSelected {
    private FragmentCalendarBinding calendarBinding;
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
        calendarBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false);
        initViews();
        return calendarBinding.getRoot();


    }

    private void initViews() {

        calendarBinding.toolbarCalendar.ivToolBarLeft.setVisibility(View.GONE);
        calendarBinding.toolbarCalendar.ivToolBarLeft.setVisibility(View.GONE);
        calendarBinding.toolbarCalendar.txvToolbarGeneralRight.setOnClickListener(this);
        calendarBinding.toolbarCalendar.tvToolbarGeneralLeft.setText("CALENDER");
        calendarBinding.toolbarCalendar.ivToolBarRight.setBackgroundResource(R.drawable.ic_plus);
        calendarBinding.toolbarCalendar.ivToolBarRight.setOnClickListener(this);
        calendarBinding.toolbarCalendar.ivToolBarRight.setVisibility(View.VISIBLE);
        calendarBinding.toolbarCalendar.txvToolbarGeneralRight.setVisibility(View.VISIBLE);
        calendarBinding.toolbarCalendar.txvToolbarGeneralRight.setCompoundDrawables(ContextCompat.getDrawable(getActivity(), android.R.drawable.btn_plus), null, null, null);
        mLayoutManager = new LinearLayoutManager(getActivity());
        calendarBinding.rvBookedJob.setLayoutManager(mLayoutManager);
        mAllJobLIst = new ArrayList<>();
        mJobAdapter = new HiredJobAdapter(getActivity(), mAllJobLIst);
        calendarBinding.rvBookedJob.setAdapter(mJobAdapter);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calendarBinding.customCalendar.setDateSelectedInterface(this);
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

//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
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
                    calendarBinding.rvBookedJob.setVisibility(View.VISIBLE);
                    calendarBinding.rvBookedJob.requestLayout();
                    calendarBinding.rvBookedJob.setFocusable(true);
                    mAllJobLIst = response.getHiredJobResponseData().getJobList();
                    calendarBinding.customCalendar.setHiredListData(mAllJobLIst);
                    arrangeJobData(date);

                } else {
                    calendarBinding.customCalendar.isFullTimeJob(false);
                    calendarBinding.rvBookedJob.setVisibility(View.GONE);
                    calendarBinding.layoutBlankAlert.setVisibility(View.VISIBLE);

                    Utils.showToast(getActivity(), response.getMessage());
                }

            }


            @Override
            public void onFail(Call<HiredJobResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGD(TAG, "Failed job hired");
            }
        });

    }

    private void cancelJob(final int ID, String msg, final int position) {
        showProgressBar(getString(R.string.please_wait));

        CancelJobRequest request = new CancelJobRequest();
        request.setCancelReason(msg);
        request.setJobId(ID);

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        webServices.cancelJob(request).enqueue(new BaseCallback<BaseResponse>((BaseActivity) getActivity()) {

            @Override
            public void onSuccess(BaseResponse response) {
                Toast.makeText(getActivity(), response.getMessage(), Toast.LENGTH_SHORT).show();

                if (response.getStatus() == 1) {
                    mJobAdapter.cancelJob(position);

                    if (mJobAdapter.getList().size() > 0) {
                        calendarBinding.rvBookedJob.setVisibility(View.GONE);
                        calendarBinding.layoutBlankAlert.setVisibility(View.GONE);

                    } else {
                        calendarBinding.layoutBlankAlert.setVisibility(View.VISIBLE);
                        mJobAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGD(TAG, "Fail");
            }
        });
    }


    @Override
    public void selectedDate(String date) {
        arrangeJobData(date);
    }

    @Override
    public void onMonthChanged(Calendar cal) {
        ViewGroup.LayoutParams params = calendarBinding.customCalendar.getLayoutParams();
//        cal.set(Calendar.DAY_OF_MONTH, 1);
//        int firstDayOfTheMonth = cal.get(Calendar.DAY_OF_WEEK) - 1;
//        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
////        int days = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);
//
//        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
//        Utils.showToast(getActivity(), "Days is-=" + firstDayOfTheMonth + "&&days month=" + days);
//        if (firstDayOfTheMonth == 6 && (days == 30 || days == 31)) {
//            params.height = 330;
//        } else if (firstDayOfTheMonth == 5 && days == 31) {
//            params.height = 330;
//        } else {
//            params.height = 300;
//        }

//        calendarBinding.customCalendar.setLayoutParams(params);
        getBookedJob(prepareRequest(cal), Utils.dateFormetyyyyMMdd(Calendar.getInstance().getTime()));

    }


    private void arrangeJobData(String date) {
        ArrayList<HiredJobs> selectedDateJobList = new ArrayList<>();
        boolean isFullTime = false;
        if (mAllJobLIst != null && mAllJobLIst.size() > 0) {
            for (int i = 0; i < mAllJobLIst.size(); i++) {
                if (mAllJobLIst.get(i).getJobType() == Constants.JOBTYPE.FULL_TIME.getValue()) {
                    isFullTime = true;
                    selectedDateJobList.add(mAllJobLIst.get(i));
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
                    for (int t = 0; t < mAllJobLIst.get(i).getTemporaryJobDates().size(); t++) {
                        if (mAllJobLIst.get(i).getTemporaryJobDates().get(t).getJobDate().equalsIgnoreCase(date)) {
                            selectedDateJobList.add(mAllJobLIst.get(i));

                        }
                    }
                }

            }
            if (selectedDateJobList != null && selectedDateJobList.size() > 0) {
                calendarBinding.customCalendar.isFullTimeJob(isFullTime);
                calendarBinding.rvBookedJob.setVisibility(View.VISIBLE);
                mJobAdapter.setJobList(selectedDateJobList);
//                mJobAdapter.notifyDataSetChanged();
            } else {
                calendarBinding.rvBookedJob.setVisibility(View.GONE);
                calendarBinding.layoutBlankAlert.setVisibility(View.VISIBLE);

            }
        }
    }
}
