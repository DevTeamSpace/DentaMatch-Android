package com.appster.dentamatch.ui.calendar;

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
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarViewSetAvailability extends LinearLayout {
    private static int MAX_CALENDAR_COLUMN = 42;
    private ImageView mIvPreviousButton, mNextButton;
    private TextView mCurrentDate;
    private GridView mCalendarGridView;
    private int mCount = 6;
    private SimpleDateFormat mFormatter = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private Calendar mCal = Calendar.getInstance(Locale.ENGLISH);
    private Context mContext;
    private CalendarGridAdapter mAdapter;
    private ArrayList<String> mTempDateList = new ArrayList<>();


    public CalendarViewSetAvailability(Context context) {
        super(context);
    }

    public CalendarViewSetAvailability(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initializeUILayout();
        setUpCalendarAdapter();
        setPreviousButtonClickEvent();
        setNextButtonClickEvent();
        setGridCellClickEvents();
    }

    public CalendarViewSetAvailability(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAvailableDate(ArrayList<String> tempDateList) {
        mTempDateList = tempDateList;
        setUpCalendarAdapter();
    }

    private void initializeUILayout() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        mIvPreviousButton = (ImageView) view.findViewById(R.id.previous_month);
        mNextButton = (ImageView) view.findViewById(R.id.next_month);
        mCurrentDate = (TextView) view.findViewById(R.id.display_current_date);
        mCalendarGridView = (GridView) view.findViewById(R.id.calendar_grid);
    }

    private void setPreviousButtonClickEvent() {

        mIvPreviousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCount > 0) {
                    mCount--;
                    mCal.add(Calendar.MONTH, -1);
                    setUpCalendarAdapter();
                } else {
                    Utils.showToast(mContext, mContext.getString(R.string.alert_previous_month_click));
                }
            }
        });

    }

    private void setNextButtonClickEvent() {

        mNextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCount < Constants.MAX_MONTH_COUNT - 1) {
                    ++mCount;
                    mCal.add(Calendar.MONTH, 1);
                    setUpCalendarAdapter();
                } else {
                    Utils.showToast(mContext, mContext.getString(R.string.alert_next_month_click));
                }
            }
        });

    }

    public ArrayList<String> getAvailabilityList() {
        return mTempDateList;
    }

    private void setGridCellClickEvents() {
        mCalendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if ((Integer) view.getTag() != -1) {

                    if (Utils.parseDate(mAdapter.getList().get(position).getDate()).compareTo(Utils.parseDate(Calendar.getInstance().getTime())) >= 0) {

                        if (mAdapter.getList().get(position).isSelected()) {
                            view.setBackgroundResource(R.color.white_color);

                            if (mTempDateList.contains(Utils.dateFormetyyyyMMdd(mAdapter.getList().get(position).getDate()))) {
                                mTempDateList.remove(Utils.dateFormetyyyyMMdd(mAdapter.getList().get(position).getDate()));
                            }

                            mAdapter.getList().get(position).setSelected(false);

                        } else {
                            view.setBackgroundResource(R.drawable.shape_temporary_date_selection);
                            mAdapter.getList().get(position).setSelected(true);
                            mTempDateList.add(Utils.dateFormetyyyyMMdd(mAdapter.getList().get(position).getDate()));

                        }

                        mAdapter.notifyDataSetChanged();

                    } else {
                        Utils.showToast(mContext, mContext.getString(R.string.alert_past_date));
                    }
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
        int days = this.mCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        if (firstDayOfTheMonth == 6 && (days == 30 || days == 31) || (firstDayOfTheMonth == 5 && days == 31)) {
            MAX_CALENDAR_COLUMN = 42;
        } else {
            MAX_CALENDAR_COLUMN = 35;
        }

        while (dayValueInCells.size() < MAX_CALENDAR_COLUMN) {
            CalenderAvailableCellModel data = new CalenderAvailableCellModel();
            data.setDate(mCal.getTime());

            if (mTempDateList != null && mTempDateList.size() > 0) {

                for (int i = 0; i < mTempDateList.size(); i++) {

                    if (Utils.dateFormetyyyyMMdd(mCal.getTime()).equalsIgnoreCase(mTempDateList.get(i))) {
                        data.setSelected(true);
                        break;

                    } else {
                        data.setSelected(false);

                    }
                }

            } else {
                data.setSelected(false);
            }

            dayValueInCells.add(data);
            mCal.add(Calendar.DAY_OF_MONTH, 1);
        }

        String sDate = mFormatter.format(this.mCal.getTime());
        mCurrentDate.setText(sDate);
        mAdapter = new CalendarGridAdapter(mContext, dayValueInCells, this.mCal);
        mCalendarGridView.setAdapter(mAdapter);
    }


}