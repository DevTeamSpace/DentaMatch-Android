/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.interfaces.OnDateSelected;
import com.appster.dentamatch.network.response.jobs.HiredJobs;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarViewEvent extends LinearLayout {
    private static int MAX_CALENDAR_COLUMN = 35;
    private static final String TAG = LogUtils.makeLogTag(CalendarViewEvent.class);

    private ImageView mIvPreviousButton, mIvNextButton;
    private TextView mCurrentDate;
    private GridView mCalendarGridView;
    private final SimpleDateFormat mFormatter = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private final Calendar mCal = Calendar.getInstance(Locale.ENGLISH);
    private Context mContext;
    private CalendarEventGridAdapter mAdapter;
    private int mOldClickedPos = -1;
    private int mCount = 6;
    private OnDateSelected mDateSelectedListener;
    private ArrayList<HiredJobs> mHiredListData;
    private ImageView mIvFullTime;
    private List<CalenderAvailableCellModel> mDayList = new ArrayList<>();

    public CalendarViewEvent(Context context) {
        super(context);
    }

    public CalendarViewEvent(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mHiredListData = new ArrayList<>();
        initializeUILayout();
        setUpCalendarAdapter();
        setPreviousButtonClickEvent();
        setNextButtonClickEvent();
        setGridCellClickEvents();
    }

    public void setDateSelectedInterface(OnDateSelected listener) {
        mDateSelectedListener = listener;
    }

    public CalendarViewEvent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHiredListData(ArrayList<HiredJobs> hiredListData) {
        try {
            if (mHiredListData != null && mHiredListData.size() > 0) {
                mHiredListData.clear();

                mHiredListData.addAll(hiredListData);
            }//Need to remove else part
            else {

                if (hiredListData != null && hiredListData.size() > 0) {
                    if(mHiredListData!=null) {
                        mHiredListData.clear();
                        mHiredListData.addAll(hiredListData);
                    }

                }
            }

            if (mAdapter != null) {
                mAdapter.setJobList(mHiredListData);
            }
        } catch (Exception e) {
            LogUtils.LOGE(TAG,e.getMessage());
        }

    }

    public void isFullTimeJob(boolean isFullTime) {

        if (isFullTime) {
            mIvFullTime.setVisibility(View.VISIBLE);
        } else {
            mIvFullTime.setVisibility(View.GONE);
        }

    }

    private void initializeUILayout() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View calendarView = inflater.inflate(R.layout.calendar_layout, this);
        mIvPreviousButton = calendarView.findViewById(R.id.previous_month);
        mIvNextButton = calendarView.findViewById(R.id.next_month);
        mIvFullTime = calendarView.findViewById(R.id.iv_full_time);
        mCurrentDate = calendarView.findViewById(R.id.display_current_date);
        mCalendarGridView = calendarView.findViewById(R.id.calendar_grid);
    }

    private void setPreviousButtonClickEvent() {
        mIvPreviousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                --mCount;
                mCal.add(Calendar.MONTH, -1);
                mDateSelectedListener.onMonthChanged(mCal);
                setUpCalendarAdapter();

            }
        });
    }

    private void setNextButtonClickEvent() {
        mIvNextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCount < Constants.MAX_MONTH_COUNT - 1) {
                    ++mCount;
                    mCal.add(Calendar.MONTH, 1);
                    mDateSelectedListener.onMonthChanged(mCal);
                    setUpCalendarAdapter();

                } else {
                    Utils.showToast(mContext, mContext.getString(R.string.alert_next_month_job));

                }
            }
        });
    }

    private void setGridCellClickEvents() {
        mCalendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ((Integer) view.getTag() != -1) {
                    if (!mDayList.get(position).isSelected()) {

                        mDayList.get(position).setSelected(true);
                        if (mOldClickedPos != -1) {
                            mDayList.get(mOldClickedPos).setSelected(false);
                        }
                        mOldClickedPos = position;

                        view.setBackgroundResource(R.drawable.shape_date_selection);
                        mAdapter.setJobList(mHiredListData);

                    }

                    mDateSelectedListener.selectedDate(Utils.dateFormetyyyyMMdd(mAdapter.getList().get(position).getDate()));
                }
            }
        });
    }

    private void setUpCalendarAdapter() {
        List<CalenderAvailableCellModel> dayValueInCells = new ArrayList<>();
        Calendar mCal = (Calendar) this.mCal.clone();
        mCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;
        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        mCal.get(Calendar.DAY_OF_WEEK);
        mCal.set(Calendar.MONTH, mCal.get(Calendar.MONTH));
        int days = this.mCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        if (firstDayOfTheMonth == 6 && (days == 30 || days == 31) || (firstDayOfTheMonth == 5 && days == 31)) {
            MAX_CALENDAR_COLUMN = 42;
        } else {
            MAX_CALENDAR_COLUMN = 35;
        }

        boolean reqPos = false;

        for (int i = 0; i < MAX_CALENDAR_COLUMN; i++) {
            CalenderAvailableCellModel data = new CalenderAvailableCellModel();
            data.setDate(mCal.getTime());
            mCal.add(Calendar.DAY_OF_MONTH, 1);

            if (Utils.dateFormetyyyyMMdd(mCal.getTime()).equalsIgnoreCase(Utils.dateFormetyyyyMMdd(Calendar.getInstance().getTime()))) {
                reqPos = true;

            } else if (i == firstDayOfTheMonth && !(Calendar.getInstance().get(Calendar.MONTH) == this.mCal.get(Calendar.MONTH) && Calendar.getInstance().get(Calendar.YEAR) == this.mCal.get(Calendar.YEAR))) {
                mOldClickedPos = i;
                data.setSelected(true);

            } else {
                data.setSelected(false);

            }

            if (!Utils.dateFormetyyyyMMdd(mCal.getTime()).equalsIgnoreCase(Utils.dateFormetyyyyMMdd(Calendar.getInstance().getTime()))) {

                if (reqPos) {
                    reqPos = false;
                    data.setSelected(true);
                    mOldClickedPos = i;
                }
            }

            dayValueInCells.add(data);
        }

        String sDate = mFormatter.format(this.mCal.getTime());
        mCurrentDate.setText(sDate);
        mDayList = dayValueInCells;
        mAdapter = new CalendarEventGridAdapter(mContext, dayValueInCells, this.mCal);
        mCalendarGridView.setAdapter(mAdapter);
    }


}