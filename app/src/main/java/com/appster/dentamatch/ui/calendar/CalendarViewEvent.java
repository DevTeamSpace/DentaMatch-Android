package com.appster.dentamatch.ui.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.network.response.jobs.HiredJobs;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.internal.Util;

public class CalendarViewEvent extends LinearLayout {
    private static final String TAG = CalendarViewEvent.class.getSimpleName();
    private ImageView previousButton, nextButton;
    private TextView currentDate;
    private GridView calendarGridView;
    //    private static  int MAX_CALENDAR_COLUMN = 42;
    private static int MAX_CALENDAR_COLUMN = 35;
    private int month, year;
    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    private Context context;
    private CalendarEventGridAdapter mAdapter;
    private int oldClickedPos = -1;
    private int count = 3;
    private OnDateSelected mDateSelectedListener;
    private ArrayList<HiredJobs> mHiredListData;
    private ImageView ivFullTime;
    //    private View calendarView;
    private List<CalenderAvailableCellModel> mDayList = new ArrayList<>();

    public CalendarViewEvent(Context context) {
        super(context);
    }

    public CalendarViewEvent(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
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
        if (mHiredListData != null) {
            mHiredListData = null;
        }
        mHiredListData = new ArrayList<>();

        mHiredListData = hiredListData;
        if (mAdapter != null) {
            mAdapter.setJobList(mHiredListData);
        }

    }

    public void isFullTimeJob(boolean isFullTime) {
        if (isFullTime) {
            ivFullTime.setVisibility(View.VISIBLE);
        } else {
            ivFullTime.setVisibility(View.GONE);

        }
    }

    private void initializeUILayout() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
//        if (calendarView != null) {
//            calendarView = null;
//        }
        View calendarView = inflater.inflate(R.layout.calendar_layout, this);
        previousButton = (ImageView) calendarView.findViewById(R.id.previous_month);
        nextButton = (ImageView) calendarView.findViewById(R.id.next_month);
        ivFullTime = (ImageView) calendarView.findViewById(R.id.iv_full_time);
        currentDate = (TextView) calendarView.findViewById(R.id.display_current_date);
        calendarGridView = (GridView) calendarView.findViewById(R.id.calendar_grid);
    }

    private void setPreviousButtonClickEvent() {
        previousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                initializeUILayout();

//                Toast.makeText(context, "under development", Toast.LENGTH_SHORT).show();
                --count;
                cal.add(Calendar.MONTH, -1);
                mDateSelectedListener.onMonthChanged(cal);
                setUpCalendarAdapter();
            }
        });
    }

    private void setNextButtonClickEvent() {
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "under development", Toast.LENGTH_SHORT).show();
                if (count < Constants.MAX_MONTH_COUNT - 1) {
//                    initializeUILayout();

                    ++count;
                    cal.add(Calendar.MONTH, 1);
                    mDateSelectedListener.onMonthChanged(cal);
                    setUpCalendarAdapter();
                } else {
                    Utils.showToast(context, context.getString(R.string.alert_next_month_job));

                }
            }
        });
    }

    private void setGridCellClickEvents() {
        calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(context, "Clicked " + (Integer)view.getTag(), Toast.LENGTH_LONG).show();
                if ((Integer) view.getTag() != -1) {
                    if (!mDayList.get(position).isSelected()) {
                        mDayList.get(position).setSelected(true);
                        if (oldClickedPos != -1) {
                            mDayList.get(oldClickedPos).setSelected(false);
                        }
                        oldClickedPos = position;

                        view.setBackgroundResource(R.drawable.shape_date_selection);

                        mAdapter.setJobList(mHiredListData);

                    }

//                    view.setBackgroundResource(R.drawable.shape_date_selection);

                    mDateSelectedListener.selectedDate(Utils.dateFormetyyyyMMdd(mAdapter.getList().get(position).getDate()));
                }


//                    view.setBackgroundResource(R.drawable.shape_date_selection);

            }
        });
    }

    private void setUpCalendarAdapter() {
//        List<Date> dayValueInCells = new ArrayList<Date>();
        List<CalenderAvailableCellModel> dayValueInCells = new ArrayList<CalenderAvailableCellModel>();
        Calendar mCal = (Calendar) cal.clone();
        mCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;
        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        mCal.get(Calendar.DAY_OF_WEEK);
        mCal.set(Calendar.MONTH, mCal.get(Calendar.MONTH));

        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
//        Utils.showToast(context, "Days is-=" + firstDayOfTheMonth + "&&days month=" + days);
        if (firstDayOfTheMonth == 6 && (days == 30 || days == 31) || (firstDayOfTheMonth == 5 && days == 31)) {
            MAX_CALENDAR_COLUMN = 42;
        } else {
            MAX_CALENDAR_COLUMN = 35;
        }

        while (dayValueInCells.size() < MAX_CALENDAR_COLUMN) {
            CalenderAvailableCellModel data = new CalenderAvailableCellModel();
            data.setDate(mCal.getTime());
            mCal.add(Calendar.DAY_OF_MONTH, 1);

            data.setSelected(false);
//            dayValueInCells.add(mCal.getTime());
            dayValueInCells.add(data);
//            mCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        LogUtils.LOGD(TAG, "Number of date " + dayValueInCells.size());
        String sDate = formatter.format(cal.getTime());
        currentDate.setText(sDate);
        mDayList = dayValueInCells;
        mAdapter = new CalendarEventGridAdapter(context, dayValueInCells, cal);
        calendarGridView.setAdapter(mAdapter);
    }


    public interface OnDateSelected {
        public void selectedDate(String date);

        public void onMonthChanged(Calendar cal);
    }
}