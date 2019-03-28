/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.calendar;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CompoundButton;

import com.appster.dentamatch.R;
import com.appster.dentamatch.base.BaseLoadingActivity;
import com.appster.dentamatch.databinding.ActivitySetAvailabilityBinding;
import com.appster.dentamatch.network.request.calendar.GetAvailabilityRequest;
import com.appster.dentamatch.network.request.calendar.SaveAvailabilityRequest;
import com.appster.dentamatch.network.response.calendar.AvailabilityResponse;
import com.appster.dentamatch.network.response.calendar.AvailabilityResponseData;
import com.appster.dentamatch.network.response.calendar.CalendarAvailability;
import com.appster.dentamatch.presentation.common.HomeActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by virender on 01/02/17.
 * To inject activity reference.
 */
public class SetAvailabilityActivity extends BaseLoadingActivity<SetAvailabilityViewModel>
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ActivitySetAvailabilityBinding mBinder;
    private boolean mIsPartTime;
    private boolean mIsFullTime;
    private boolean mIsTemporary;
    private boolean mIsSunday;
    private boolean mIsMonday;
    private boolean mIsTuesday;
    private boolean mIsWednesday;
    private boolean mIsThursday;
    private boolean mIsFriday;
    private boolean mIsSaturday;
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

        viewModel.getAvailabilityResponse().observe(this, this::onSuccessRequestAvailability);
        viewModel.getSaveAvailability().observe(this, save -> onSuccessSaveAvailability());
    }

    private void onSuccessSaveAvailability() {
        EventBus.getDefault().isRegistered(true);
        if(isFromRegistration){
            startActivity(new Intent(SetAvailabilityActivity.this, HomeActivity.class));
        }
        finish();
    }

    private void onSuccessRequestAvailability(@Nullable AvailabilityResponse response) {
        if (response != null) {
            setViewData(response);
        }
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null){
            isFromRegistration=getIntent().getBooleanExtra(Constants.IS_FROM_PROFILE_COMPLETE, Boolean.FALSE);
        }
    }

    private void initViews() {
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
        mBinder.customCalendar.initialize(getSupportFragmentManager());
        mBinder.customCalendar.setSelectedPosition(Calendar.getInstance(Locale.ENGLISH).get(Calendar.MONTH));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivToolBarLeft:
                finish();
                break;
            case R.id.txv_toolbar_general_right:
                    saveAvailability(prepareSaveRequest());
                break;
            case R.id.tv_sunday:
                if (mIsSunday) {
                    mIsSunday = false;
                    mBinder.tvSunday.setBackground(null);
                    mBinder.tvSunday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
                } else {
                    mIsSunday = true;
                    mBinder.tvSunday.setBackgroundResource(R.drawable.shape_circular_text_view);
                    mBinder.tvSunday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));
                }
                break;
            case R.id.tv_monday:
                if (mIsMonday) {
                    mIsMonday = false;
                    mBinder.tvMonday.setBackground(null);
                    mBinder.tvMonday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
                } else {
                    mIsMonday = true;
                    mBinder.tvMonday.setBackgroundResource(R.drawable.shape_circular_text_view);
                    mBinder.tvMonday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));
                }
                break;
            case R.id.tv_tuesday:
                if (mIsTuesday) {
                    mIsTuesday = false;
                    mBinder.tvTuesday.setBackground(null);
                    mBinder.tvTuesday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
                } else {
                    mIsTuesday = true;
                    mBinder.tvTuesday.setBackgroundResource(R.drawable.shape_circular_text_view);
                    mBinder.tvTuesday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));
                }
                break;
            case R.id.tv_wednesday:
                if (mIsWednesday) {
                    mIsWednesday = false;
                    mBinder.tvWednesday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
                    mBinder.tvWednesday.setBackground(null);
                } else {
                    mIsWednesday = true;
                    mBinder.tvWednesday.setBackgroundResource(R.drawable.shape_circular_text_view);
                    mBinder.tvWednesday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));
                }
                break;
            case R.id.tv_thursday:
                if (mIsThursday) {
                    mIsThursday = false;
                    mBinder.tvThursday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
                    mBinder.tvThursday.setBackground(null);
                } else {
                    mIsThursday = true;
                    mBinder.tvThursday.setBackgroundResource(R.drawable.shape_circular_text_view);
                    mBinder.tvThursday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));
                }
                break;
            case R.id.tv_friday:
                if (mIsFriday) {
                    mIsFriday = false;
                    mBinder.tvFriday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
                    mBinder.tvFriday.setBackground(null);
                } else {
                    mIsFriday = true;
                    mBinder.tvFriday.setBackgroundResource(R.drawable.shape_circular_text_view);
                    mBinder.tvFriday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));
                }
                break;
            case R.id.tv_saturday:
                if (mIsSaturday) {
                    mIsSaturday = false;
                    mBinder.tvSaturday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
                    mBinder.tvSaturday.setBackground(null);
                } else {
                    mIsSaturday = true;
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

    @NonNull
    private GetAvailabilityRequest prepareGetAvailableRequest() {
        GetAvailabilityRequest request = new GetAvailabilityRequest();
        int year = Calendar.getInstance(Locale.ENGLISH).get(Calendar.YEAR);
        request.setCalendarStartDate(String.format("%s-01-01", String.valueOf(year)));
        request.setCalendarEndDate(String.format("%s-12-31", String.valueOf(year)));
        return request;
    }

    @NonNull
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

    private void getAvailability(@NonNull GetAvailabilityRequest request) {
        viewModel.requestAvailabilityList(request);
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
            mBinder.tvSaturday.setBackground(null);
            mBinder.tvSaturday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
        } else {
            mIsSaturday = true;
            mBinder.tvSaturday.setBackgroundResource(R.drawable.shape_circular_text_view);
            mBinder.tvSaturday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));
        }
        if (calendarAvailability.getIsParttimeSunday() == 0) {
            mIsSunday = false;
            mBinder.tvSunday.setBackground(null);
            mBinder.tvSunday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
        } else {
            mIsSunday = true;
            mBinder.tvSunday.setBackgroundResource(R.drawable.shape_circular_text_view);
            mBinder.tvSunday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));
        }
        if (calendarAvailability.getIsParttimeMonday() == 0) {
            mIsMonday = false;
            mBinder.tvMonday.setBackground(null);
            mBinder.tvMonday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
        } else {
            mIsMonday = true;
            mBinder.tvMonday.setBackgroundResource(R.drawable.shape_circular_text_view);
            mBinder.tvMonday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));
        }
        if (calendarAvailability.getIsParttimeTuesday() == 0) {
            mIsTuesday = false;
            mBinder.tvTuesday.setBackground(null);
            mBinder.tvTuesday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
        } else {
            mIsTuesday = true;
            mBinder.tvTuesday.setBackgroundResource(R.drawable.shape_circular_text_view);
            mBinder.tvTuesday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));
        }
        if (calendarAvailability.getIsParttimeWednesday() == 0) {
            mIsWednesday = false;
            mBinder.tvWednesday.setBackground(null);
            mBinder.tvWednesday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
        } else {
            mIsWednesday = true;
            mBinder.tvWednesday.setBackgroundResource(R.drawable.shape_circular_text_view);
            mBinder.tvWednesday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));
        }
        if (calendarAvailability.getIsParttimeThursday() == 0) {
            mIsThursday = false;
            mBinder.tvThursday.setBackground(null);
            mBinder.tvThursday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
        } else {
            mIsThursday = true;
            mBinder.tvThursday.setBackgroundResource(R.drawable.shape_circular_text_view);
            mBinder.tvThursday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));
        }
        if (calendarAvailability.getIsParttimeFriday() == 0) {
            mIsFriday = false;
            mBinder.tvFriday.setBackground(null);
            mBinder.tvFriday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
        } else {
            mIsFriday = true;
            mBinder.tvFriday.setBackgroundResource(R.drawable.shape_circular_text_view);
            mBinder.tvFriday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));
        }
    }

    private void saveAvailability(@NonNull SaveAvailabilityRequest request) {
        viewModel.saveAvailability(request);
    }
}

