/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.ui.calendar;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CompoundButton;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivitySetAvailabilityBinding;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.base.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.calendar.GetAvailabilityRequest;
import com.appster.dentamatch.network.request.calendar.SaveAvailabilityRequest;
import com.appster.dentamatch.network.response.calendar.AvailabilityResponse;
import com.appster.dentamatch.network.response.calendar.AvailabilityResponseData;
import com.appster.dentamatch.network.response.calendar.CalendarAvailability;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.base.BaseActivity;
import com.appster.dentamatch.ui.common.HomeActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;

/**
 * Created by virender on 01/02/17.
 * To inject activity reference.
 */
public class SetAvailabilityActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private ActivitySetAvailabilityBinding mBinder;
    private ArrayList<String> mPartTimeDays;
    private AvailabilityResponse mAvailabilityResponse;
    private boolean mIsPartTime, mIsFullTime, mIsTemporary, mIsSunday, mIsMonday, mIsTuesday, mIsWednesday, mIsThursday, mIsFriday, mIsSaturday, mIsFromProfileComplete;
    private boolean isFromRegistration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_set_availability);
        initViews();
        getIntentData();

        //open it
        getAvailability(prepareGetAvailableRequest());
        PreferenceUtil.setSetAvailability(true);
    }

    private void getIntentData() {
       /* if (getIntent().getExtras() != null && getIntent().hasExtra(Constants.IS_LICENCE_REQUIRED)) {
            mIsFromProfileComplete = getIntent().getBooleanExtra(Constants.IS_FROM_PROFILE_COMPLETE, Boolean.FALSE);
            if (mIsFromProfileComplete)
                getAvailability(prepareGetAvailableRequest());
        }*/

        if (getIntent().getExtras() != null){
            isFromRegistration=getIntent().getBooleanExtra(Constants.IS_FROM_PROFILE_COMPLETE, Boolean.FALSE);
        }

    }

    private void initViews() {
        mPartTimeDays = new ArrayList<>();
        mBinder.toolbarSetAvailability.tvToolbarGeneralLeft.setText(getString(R.string.header_set_availability));
        mBinder.toolbarSetAvailability.txvToolbarGeneralRight.setVisibility(View.VISIBLE);
        mBinder.toolbarSetAvailability.txvToolbarGeneralRight.setText(getString(R.string.save_label));

        mBinder.toolbarSetAvailability.txvToolbarGeneralRight.setOnClickListener(this);
        mBinder.toolbarSetAvailability.ivToolBarLeft.setOnClickListener(this);
        mBinder.cbFullTimeCheckBox.setOnCheckedChangeListener(this);
        mBinder.cbPartTimeCheckBox.setOnCheckedChangeListener(this);
        mBinder.cbTemporaryCheckBox.setOnCheckedChangeListener(this);
        mBinder.tvSaturday.setOnClickListener(this);
        mBinder.tvSunday.setOnClickListener(this);
        mBinder.tvMonday.setOnClickListener(this);
        mBinder.tvTuesday.setOnClickListener(this);
        mBinder.tvWednesday.setOnClickListener(this);
        mBinder.tvThursday.setOnClickListener(this);
        mBinder.tvFriday.setOnClickListener(this);
    }

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_tool_bar_left:
                finish();
                break;

            case R.id.txv_toolbar_general_right:
                //if (checkValidation()) {
                    saveAvailability(prepareSaveRequest());
                //}

                break;

            case R.id.tv_sunday:
                if (mIsSunday) {
                    mIsSunday = false;
                    mPartTimeDays.remove(getString(R.string.txt_full_sunday));
                    mBinder.tvSunday.setBackground(null);
                    mBinder.tvSunday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
                } else {
                    mPartTimeDays.add(getString(R.string.txt_full_sunday));
                    mIsSunday = true;
                    mBinder.tvSunday.setBackgroundResource(R.drawable.shape_circular_text_view);
                    mBinder.tvSunday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));
                }

                break;

            case R.id.tv_monday:
                if (mIsMonday) {
                    mIsMonday = false;
                    mPartTimeDays.remove(getString(R.string.txt_full_monday));
                    mBinder.tvMonday.setBackground(null);
                    mBinder.tvMonday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
                } else {
                    mIsMonday = true;
                    mPartTimeDays.add(getString(R.string.txt_full_monday));
                    mBinder.tvMonday.setBackgroundResource(R.drawable.shape_circular_text_view);
                    mBinder.tvMonday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));
                }

                break;

            case R.id.tv_tuesday:
                if (mIsTuesday) {
                    mIsTuesday = false;
                    mPartTimeDays.remove(getString(R.string.txt_full_tuesday));
                    mBinder.tvTuesday.setBackground(null);
                    mBinder.tvTuesday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
                } else {
                    mIsTuesday = true;
                    mPartTimeDays.add(getString(R.string.txt_full_tuesday));
                    mBinder.tvTuesday.setBackgroundResource(R.drawable.shape_circular_text_view);
                    mBinder.tvTuesday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));
                }

                break;

            case R.id.tv_wednesday:
                if (mIsWednesday) {
                    mIsWednesday = false;
                    mPartTimeDays.remove(getString(R.string.txt_full_wednesday));
                    mBinder.tvWednesday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
                    mBinder.tvWednesday.setBackground(null);
                } else {
                    mIsWednesday = true;
                    mPartTimeDays.add(getString(R.string.txt_full_wednesday));
                    mBinder.tvWednesday.setBackgroundResource(R.drawable.shape_circular_text_view);
                    mBinder.tvWednesday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));
                }

                break;

            case R.id.tv_thursday:
                if (mIsThursday) {
                    mIsThursday = false;
                    mPartTimeDays.remove(getString(R.string.txt_full_thursday));
                    mBinder.tvThursday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
                    mBinder.tvThursday.setBackground(null);
                } else {
                    mIsThursday = true;
                    mPartTimeDays.add(getString(R.string.txt_full_thursday));
                    mBinder.tvThursday.setBackgroundResource(R.drawable.shape_circular_text_view);
                    mBinder.tvThursday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));
                }

                break;

            case R.id.tv_friday:
                if (mIsFriday) {
                    mIsFriday = false;
                    mPartTimeDays.remove(getString(R.string.txt_full_friday));
                    mBinder.tvFriday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
                    mBinder.tvFriday.setBackground(null);
                } else {
                    mIsFriday = true;
                    mPartTimeDays.add(getString(R.string.txt_full_friday));
                    mBinder.tvFriday.setBackgroundResource(R.drawable.shape_circular_text_view);
                    mBinder.tvFriday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));
                }

                break;

            case R.id.tv_saturday:
                if (mIsSaturday) {
                    mIsSaturday = false;
                    mPartTimeDays.remove(getString(R.string.txt_full_saturday));
                    mBinder.tvSaturday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
                    mBinder.tvSaturday.setBackground(null);
                } else {
                    mIsSaturday = true;
                    mPartTimeDays.add(getString(R.string.txt_full_saturday));
                    mBinder.tvSaturday.setBackgroundResource(R.drawable.shape_circular_text_view);
                    mBinder.tvSaturday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));
                }

                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {

            case R.id.cb_part_time_check_box:

                if (isChecked) {
                    mIsPartTime = true;
                    mBinder.tvPartTime.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.black_color));
                    mBinder.dayLayout.setVisibility(View.VISIBLE);
                } else {
                    mIsPartTime = false;
                    mBinder.dayLayout.setVisibility(View.GONE);
                    mBinder.tvPartTime.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.grayish_two));
                }

                break;


            case R.id.cb_full_time_check_box:

                if (isChecked) {
                    mIsFullTime = true;
                    mBinder.tvFullTime.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.black_color));
                } else {
                    mIsFullTime = false;
                    mBinder.tvFullTime.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.grayish_two));
                }

                break;

            case R.id.cb_temporary_check_box:

                if (isChecked) {
                    mIsTemporary = true;
                    mBinder.customCalendar.setVisibility(View.VISIBLE);
                    mBinder.tvTemporary.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.black_color));
                } else {
                    mIsTemporary = false;
                    mBinder.customCalendar.setVisibility(View.GONE);
                    mBinder.tvTemporary.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.grayish_two));
                }

                break;


            default:
                break;
        }
    }

    private void resetParTimeAvailability() {
        if (mAvailabilityResponse != null && mAvailabilityResponse.getAvailabilityResponseData() != null && mAvailabilityResponse.getAvailabilityResponseData().getCalendarAvailability() != null) {
            CalendarAvailability calendarAvailability = mAvailabilityResponse.getAvailabilityResponseData().getCalendarAvailability();

            calendarAvailability.setIsParttimeMonday(0);
            calendarAvailability.setIsParttimeTuesday(0);
            calendarAvailability.setIsParttimeWednesday(0);
            calendarAvailability.setIsParttimeThursday(0);
            calendarAvailability.setIsParttimeFriday(0);
            calendarAvailability.setIsParttimeSaturday(0);
            calendarAvailability.setIsParttimeSunday(0);
            setPartTimeDayView(calendarAvailability);
        }
    }

   /* private boolean checkValidation() {
        if (!mIsPartTime && !mIsFullTime && !mIsTemporary) {
            showToast(getString(R.string.alert_select_job_type));
            return false;
        }
        if (mIsPartTime && (!mIsSunday && !mIsMonday && !mIsTuesday && !mIsWednesday && !mIsThursday && !mIsFriday && !mIsSaturday && !mIsSunday)) {
            showToast(getString(R.string.alert_invalid_part_time));

            return false;
        }
        if (mIsTemporary && (mBinder.customCalendar.getAvailabilityList() == null || mBinder.customCalendar.getAvailabilityList().size() == 0)) {
            showToast(getString(R.string.alert_invalid_temp_job));
            return false;

        }
        return true;
    }*/

    private GetAvailabilityRequest prepareGetAvailableRequest() {
        GetAvailabilityRequest request = new GetAvailabilityRequest();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -6);
        Date startDate = calendar.getTime();
        Calendar endDateCalendar = Calendar.getInstance();
        endDateCalendar.add(Calendar.MONTH, 6);

        /**
         * to get  the number of days  in month
         */
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(Calendar.MONTH, endDateCalendar.get(Calendar.MONTH));
        int days = tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        Date endDate = endDateCalendar.getTime();

        String startStr = Utils.dateFormetyyyyMMdd(startDate);
        startStr = startStr.substring(0, startStr.lastIndexOf("-"));
        startStr = startStr + "-01";
        String[] splitEndDate = Utils.dateFormetyyyyMMdd(endDate).split("-");
        request.setCalendarStartDate(startStr);
        request.setCalendarEndDate(splitEndDate[0] + "-" + splitEndDate[1] + "-" + days);

        return request;

    }

    private SaveAvailabilityRequest prepareSaveRequest() {

        SaveAvailabilityRequest request = new SaveAvailabilityRequest();
        ArrayList<String> dayList = new ArrayList<>();
        ArrayList<String> temporaryList = new ArrayList<>();


        if (mIsFullTime) {
            request.setIsFulltime(1);
        } else {
            request.setIsFulltime(0);
        }
        if (mIsPartTime) {

            if (mIsMonday) {
                dayList.add(getString(R.string.txt_full_monday).toLowerCase());
            }

            if (mIsTuesday) {
                dayList.add(getString(R.string.txt_full_tuesday).toLowerCase());
            }

            if (mIsWednesday) {
                dayList.add(getString(R.string.txt_full_wednesday).toLowerCase());
            }

            if (mIsThursday) {
                dayList.add(getString(R.string.txt_full_thursday).toLowerCase());
            }

            if (mIsFriday) {
                dayList.add(getString(R.string.txt_full_friday).toLowerCase());
            }

            if (mIsSaturday) {
                dayList.add(getString(R.string.txt_full_saturday).toLowerCase());
            }

            if (mIsSunday) {
                dayList.add(getString(R.string.txt_full_sunday).toLowerCase());
            }
        }
        request.setPartTimeDays(dayList);

        if (mIsTemporary) {
            temporaryList = mBinder.customCalendar.getAvailabilityList();
        }

        request.setTempDates(temporaryList);
        return request;
    }


    private void getAvailability(GetAvailabilityRequest request) {
        processToShowDialog();
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.getAvailabilityList(request).enqueue(new BaseCallback<AvailabilityResponse>(SetAvailabilityActivity.this) {
            @Override
            public void onSuccess(AvailabilityResponse response) {
                if (response.getStatus() == 1) {
                    mAvailabilityResponse = response;
                    setViewData(response);

                } else {
                    Utils.showToast(SetAvailabilityActivity.this, response.getMessage());

                }
            }

            @Override
            public void onFail(Call<AvailabilityResponse> call, BaseResponse baseResponse) {
            }
        });
    }

    private void setViewData(AvailabilityResponse res) {
        AvailabilityResponseData data = res.getAvailabilityResponseData();

        if (data.getCalendarAvailability() != null) {
            CalendarAvailability calendarAvailability = data.getCalendarAvailability();

            if (calendarAvailability != null) {

                if (calendarAvailability.getIsFulltime() == 1) {
                    mIsFullTime = true;
                    mBinder.cbFullTimeCheckBox.setChecked(true);
                    mBinder.tvFullTime.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.black_color));
                } else {
                    mBinder.cbFullTimeCheckBox.setChecked(false);

                    mIsFullTime = false;
                    mBinder.tvFullTime.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.grayish_two));
                }

                if (calendarAvailability.getIsParttimeSunday() == 1 || calendarAvailability.getIsParttimeMonday() == 1 || calendarAvailability.getIsParttimeTuesday() == 1 || calendarAvailability.getIsParttimeWednesday() == 1 || calendarAvailability.getIsParttimeThursday() == 1 || calendarAvailability.getIsParttimeFriday() == 1 || calendarAvailability.getIsParttimeSaturday() == 1) {
                    mIsPartTime = true;
                    mBinder.cbPartTimeCheckBox.setChecked(true);

                    mBinder.tvPartTime.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.black_color));
                    mBinder.dayLayout.setVisibility(View.VISIBLE);
                    setPartTimeDayView(calendarAvailability);

                } else {
                    mBinder.cbPartTimeCheckBox.setChecked(false);

                    mIsPartTime = false;
                    mBinder.dayLayout.setVisibility(View.GONE);
                    mBinder.tvPartTime.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.grayish_two));
                }

            }
            mBinder.customCalendar.setAvailableDate(data.getTempDateList());

            if (data.getTempDateList() != null && data.getTempDateList().size() > 0) {
                mIsTemporary = true;
                mBinder.cbTemporaryCheckBox.setChecked(true);

                mBinder.customCalendar.setVisibility(View.VISIBLE);
                mBinder.tvTemporary.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.black_color));
            } else {
                mBinder.cbTemporaryCheckBox.setChecked(false);

                mIsTemporary = false;
                mBinder.customCalendar.setVisibility(View.GONE);
                mBinder.tvTemporary.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.grayish_two));
            }

        }
    }

    private void setPartTimeDayView(CalendarAvailability calendarAvailability) {

        if (calendarAvailability.getIsParttimeSaturday() == 0) {
            mIsSaturday = false;
            mPartTimeDays.remove(getString(R.string.txt_full_saturday));
            mBinder.tvSaturday.setBackground(null);
            mBinder.tvSaturday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
        } else {
            mPartTimeDays.add(getString(R.string.txt_full_saturday));
            mIsSaturday = true;
            mBinder.tvSaturday.setBackgroundResource(R.drawable.shape_circular_text_view);
            mBinder.tvSaturday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));

        }

        if (calendarAvailability.getIsParttimeSunday() == 0) {
            mIsSunday = false;
            mPartTimeDays.remove(getString(R.string.txt_full_sunday));
            mBinder.tvSunday.setBackground(null);
            mBinder.tvSunday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
        } else {
            mPartTimeDays.add(getString(R.string.txt_full_sunday));
            mIsSunday = true;
            mBinder.tvSunday.setBackgroundResource(R.drawable.shape_circular_text_view);
            mBinder.tvSunday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));

        }

        if (calendarAvailability.getIsParttimeMonday() == 0) {
            mIsMonday = false;
            mPartTimeDays.remove(getString(R.string.txt_full_monday));
            mBinder.tvMonday.setBackground(null);
            mBinder.tvMonday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
        } else {
            mPartTimeDays.add(getString(R.string.txt_full_monday));
            mIsMonday = true;
            mBinder.tvMonday.setBackgroundResource(R.drawable.shape_circular_text_view);
            mBinder.tvMonday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));

        }

        if (calendarAvailability.getIsParttimeTuesday() == 0) {
            mIsTuesday = false;
            mPartTimeDays.remove(getString(R.string.txt_full_tuesday));
            mBinder.tvTuesday.setBackground(null);
            mBinder.tvTuesday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
        } else {
            mPartTimeDays.add(getString(R.string.txt_full_tuesday));
            mIsTuesday = true;
            mBinder.tvTuesday.setBackgroundResource(R.drawable.shape_circular_text_view);
            mBinder.tvTuesday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));

        }

        if (calendarAvailability.getIsParttimeWednesday() == 0) {
            mIsWednesday = false;
            mPartTimeDays.remove(getString(R.string.txt_full_wednesday));
            mBinder.tvWednesday.setBackground(null);
            mBinder.tvWednesday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
        } else {
            mPartTimeDays.add(getString(R.string.txt_full_wednesday));
            mIsWednesday = true;
            mBinder.tvWednesday.setBackgroundResource(R.drawable.shape_circular_text_view);
            mBinder.tvWednesday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));
        }

        if (calendarAvailability.getIsParttimeThursday() == 0) {
            mIsThursday = false;
            mPartTimeDays.remove(getString(R.string.txt_full_thursday));
            mBinder.tvThursday.setBackground(null);
            mBinder.tvThursday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
        } else {
            mPartTimeDays.add(getString(R.string.txt_full_thursday));
            mIsThursday = true;
            mBinder.tvThursday.setBackgroundResource(R.drawable.shape_circular_text_view);
            mBinder.tvThursday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));
        }

        if (calendarAvailability.getIsParttimeFriday() == 0) {
            mIsFriday = false;
            mPartTimeDays.remove(getString(R.string.txt_full_friday));
            mBinder.tvFriday.setBackground(null);
            mBinder.tvFriday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
        } else {
            mPartTimeDays.add(getString(R.string.txt_full_friday));
            mIsFriday = true;
            mBinder.tvFriday.setBackgroundResource(R.drawable.shape_circular_text_view);
            mBinder.tvFriday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));
        }

    }

    private void saveAvailability(SaveAvailabilityRequest request) {
        processToShowDialog();
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.saveAvailability(request).enqueue(new BaseCallback<BaseResponse>(SetAvailabilityActivity.this) {
            @Override
            public void onSuccess(BaseResponse response) {
                Utils.showToast(SetAvailabilityActivity.this, response.getMessage());

                if (response.getStatus() == 1) {
                    EventBus.getDefault().isRegistered(true);

                    if(isFromRegistration){
                        startActivity(new Intent(SetAvailabilityActivity.this, HomeActivity.class));
                    }
                    finish();

                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
            }
        });
    }


}

