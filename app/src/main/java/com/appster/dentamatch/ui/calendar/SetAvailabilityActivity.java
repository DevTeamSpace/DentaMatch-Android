package com.appster.dentamatch.ui.calendar;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CompoundButton;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivitySetAvailabilityBinding;
import com.appster.dentamatch.model.JobTitleList;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.calendar.SaveAvailabilityRequest;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;

/**
 * Created by virender on 01/02/17.
 */
public class SetAvailabilityActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private ActivitySetAvailabilityBinding mBinder;
    private String mSelectedLat, mSelectedLng;
    private ArrayList<Integer> mSelectedJobID;
    private ArrayList<String> mPartTimeDays;
    private ArrayList<JobTitleList> mChosenTitles;
    private String mSelectedZipCode;
    private boolean isPartTime, isFullTime, isTemporary, isSunday, isMonday, isTuesday, isWednesday, isThursday, isFriday, isSaturday;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_set_availability);
        initViews();
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
                if (checkValidation()) {
                    finish();
//                    saveAvailability(prepareSaveRequest());
                }
                break;
            case R.id.tv_sunday:
                if (isSunday) {
                    isSunday = false;
                    mPartTimeDays.remove("Sunday");
                    mBinder.tvSunday.setBackground(null);
                    mBinder.tvSunday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
                } else {
                    mPartTimeDays.add("Sunday");
                    isSunday = true;
                    mBinder.tvSunday.setBackgroundResource(R.drawable.shape_circular_text_view);
                    mBinder.tvSunday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));
                }
                break;

            case R.id.tv_monday:
                if (isMonday) {
                    isMonday = false;
                    mPartTimeDays.remove("Monday");
                    mBinder.tvMonday.setBackground(null);
                    mBinder.tvMonday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));

                } else {
                    isMonday = true;
                    mPartTimeDays.add("Monday");
                    mBinder.tvMonday.setBackgroundResource(R.drawable.shape_circular_text_view);
                    mBinder.tvMonday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));
                }
                break;

            case R.id.tv_tuesday:
                if (isTuesday) {
                    isTuesday = false;
                    mPartTimeDays.remove("Tuesday");
                    mBinder.tvTuesday.setBackground(null);
                    mBinder.tvTuesday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));

                } else {
                    isTuesday = true;
                    mPartTimeDays.add("Tuesday");
                    mBinder.tvTuesday.setBackgroundResource(R.drawable.shape_circular_text_view);
                    mBinder.tvTuesday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));

                }
                break;

            case R.id.tv_wednesday:
                if (isWednesday) {
                    isWednesday = false;
                    mPartTimeDays.remove("Wednesday");
                    mBinder.tvWednesday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
                    mBinder.tvWednesday.setBackground(null);
                } else {
                    isWednesday = true;
                    mPartTimeDays.add("Wednesday");
                    mBinder.tvWednesday.setBackgroundResource(R.drawable.shape_circular_text_view);
                    mBinder.tvWednesday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));

                }
                break;

            case R.id.tv_thursday:
                if (isThursday) {
                    isThursday = false;
                    mPartTimeDays.remove("Thursday");
                    mBinder.tvThursday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
                    mBinder.tvThursday.setBackground(null);
                } else {
                    isThursday = true;
                    mPartTimeDays.add("Thursday");
                    mBinder.tvThursday.setBackgroundResource(R.drawable.shape_circular_text_view);
                    mBinder.tvThursday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));

                }
                break;

            case R.id.tv_friday:
                if (isFriday) {
                    isFriday = false;
                    mPartTimeDays.remove("Friday");
                    mBinder.tvFriday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
                    mBinder.tvFriday.setBackground(null);
                } else {
                    isFriday = true;
                    mPartTimeDays.add("Friday");
                    mBinder.tvFriday.setBackgroundResource(R.drawable.shape_circular_text_view);
                    mBinder.tvFriday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.white_color));
                }
                break;

            case R.id.tv_saturday:
                if (isSaturday) {
                    isSaturday = false;
                    mPartTimeDays.remove("Saturday");
                    mBinder.tvSaturday.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.brownish_grey));
                    mBinder.tvSaturday.setBackground(null);
                } else {
                    isSaturday = true;
                    mPartTimeDays.add("Saturday");
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
                    isPartTime = true;
                    mBinder.tvPartTime.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.black_color));
                    mBinder.dayLayout.setVisibility(View.VISIBLE);
                } else {
                    isPartTime = false;
                    mBinder.dayLayout.setVisibility(View.GONE);
                    mBinder.tvPartTime.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.grayish_two));
                }

                break;


            case R.id.cb_full_time_check_box:
                if (isChecked) {
                    isFullTime = true;
                    mBinder.tvFullTime.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.black_color));
                } else {
                    isFullTime = false;
                    mBinder.tvFullTime.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.grayish_two));
                }
                break;

            case R.id.cb_temporary_check_box:
                if (isChecked) {
                    isTemporary = true;
                    mBinder.customCalendar.setVisibility(View.GONE);

                    mBinder.tvTemporary.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.black_color));
                } else {
                    isTemporary = false;
                    mBinder.tvTemporary.setTextColor(ContextCompat.getColor(SetAvailabilityActivity.this, R.color.grayish_two));
                    mBinder.customCalendar.setVisibility(View.VISIBLE);
                }
                break;


            default:
                break;
        }
    }

    private boolean checkValidation() {
        if (!isPartTime && !isFullTime && !isTemporary) {
            showToast(getString(R.string.select_multiple_job));
            return false;
        }
        return true;
    }

    private void prepareGetAvailableRequest(){


    }
    private SaveAvailabilityRequest prepareSaveRequest() {

        SaveAvailabilityRequest request = new SaveAvailabilityRequest();
        ArrayList<String> dayList = new ArrayList<>();
        ArrayList<String> temporaryList = new ArrayList<>();


        if (isFullTime) {
            request.setIsFulltime(1);
        } else {
            request.setIsFulltime(0);
        }
        if (isPartTime) {
            if (isMonday) {
                dayList.add(getString(R.string.txt_monday));
            }
            if (isTuesday) {
                dayList.add(getString(R.string.txt_tuesday));
            }
            if (isWednesday) {
                dayList.add(getString(R.string.txt_wednesday));
            }
            if (isThursday) {
                dayList.add(getString(R.string.txt_thursday));
            }
            if (isFriday) {
                dayList.add(getString(R.string.txt_friday));
            }
            if (isSaturday) {
                dayList.add(getString(R.string.txt_saturday));
            }
            if (isSunday) {
                dayList.add(getString(R.string.txt_sunday));
            }
        }
        request.setPartTimeDays(dayList);
        if (isTemporary) {
            List<CalenderAvailableCellModel> tempList = mBinder.customCalendar.getAvailabilityList();
            if (tempList != null && tempList.size() > 0) {

                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).isSelected()) {
                        temporaryList.add(dateFormet(tempList.get(i).getDate()));
                    }
                }
            }
        }
        request.setTempDates(temporaryList);

        return request;
    }


    private void getAvailability() {
        processToShowDialog("", getString(R.string.please_wait), mBinder.cbFullTimeCheckBox);
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.getAvailabilityList().enqueue(new BaseCallback<BaseResponse>(SetAvailabilityActivity.this) {
            @Override
            public void onSuccess(BaseResponse response) {
                LogUtils.LOGD(TAG, "onSuccess");
                if (response.getStatus() == 1) {

                } else {
                    Utils.showToast(SetAvailabilityActivity.this, response.getMessage());
                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGD(TAG, "onFail");
            }
        });
    }

    private void saveAvailability(SaveAvailabilityRequest request) {
        processToShowDialog("", getString(R.string.please_wait), null);
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.saveAvailability(request).enqueue(new BaseCallback<BaseResponse>(SetAvailabilityActivity.this) {
            @Override
            public void onSuccess(BaseResponse response) {
                LogUtils.LOGD(TAG, "onSuccess");
                Utils.showToast(SetAvailabilityActivity.this, response.getMessage());

                if (response.getStatus() == 1) {
                    EventBus.getDefault().isRegistered(true);
                    finish();
                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGD(TAG, "onFail");
            }
        });
    }

    private String dateFormet(Date mydate) {
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-mm-dd");
        // myDate is the java.util.Date in yyyy-mm-dd format
        // Converting it into String using formatter
        String strDate = sm.format(mydate);
        //Converting the String back to java.util.Date
        return strDate;
    }
}

