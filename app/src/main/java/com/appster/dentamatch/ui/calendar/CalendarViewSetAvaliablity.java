package com.appster.dentamatch.ui.calendar;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.appster.dentamatch.R;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.Utils;
import com.google.android.gms.vision.text.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CalendarViewSetAvaliablity extends LinearLayout {
    private static final String TAG = CalendarViewSetAvaliablity.class.getSimpleName();
    private ImageView previousButton, nextButton;
    private TextView currentDate;
    private GridView calendarGridView;
    private int count = 6;
    private static int MAX_CALENDAR_COLUMN = 42;
    private int month, year;
    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    private Context context;
    private CalendarGridAdapter mAdapter;
    private HashMap<Integer, ArrayList<CalenderAvailableCellModel>> mDateMap = new HashMap<>();
    private ArrayList<String> mTempDateList = new ArrayList<>();


    public CalendarViewSetAvaliablity(Context context) {
        super(context);
    }

    public CalendarViewSetAvaliablity(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializeUILayout();
        setUpCalendarAdapter();
        setPreviousButtonClickEvent();
        setNextButtonClickEvent();
        setGridCellClickEvents();
    }

    public CalendarViewSetAvaliablity(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAvailableDate(ArrayList<String> tempDateList) {
        mTempDateList = tempDateList;
        setUpCalendarAdapter();
    }

    private void initializeUILayout() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        previousButton = (ImageView) view.findViewById(R.id.previous_month);
        nextButton = (ImageView) view.findViewById(R.id.next_month);
        currentDate = (TextView) view.findViewById(R.id.display_current_date);
        calendarGridView = (GridView) view.findViewById(R.id.calendar_grid);
    }

    private void setPreviousButtonClickEvent() {

        previousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.LOGD(TAG, "pre count is ===" + count);
                if (count > 0) {
                    count--;
                    cal.add(Calendar.MONTH, -1);
                    setUpCalendarAdapter();
                } else {
                    Utils.showToast(context, context.getString(R.string.alert_previous_month_click));

                }
            }
        });

    }

    private void setNextButtonClickEvent() {

        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.LOGD(TAG, "next  count is ===" + count);

                if (count < Constants.MAX_MONTH_COUNT - 1) {
                    ++count;
                    cal.add(Calendar.MONTH, 1);
                    setUpCalendarAdapter();
                } else {
                    Utils.showToast(context, context.getString(R.string.alert_next_month_click));
                }
            }
        });

    }

    public ArrayList<String> getAvailabilityList() {
        return mTempDateList;
    }

    private void setGridCellClickEvents() {
        calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                          TextView day=(TextView) view.findViewById(R.id.calendar_date_id);
//                            day.setTextColor(ContextCompat.getColor(context,R.color.text_default_color));
                            mAdapter.getList().get(position).setSelected(true);
                            mTempDateList.add(Utils.dateFormetyyyyMMdd(mAdapter.getList().get(position).getDate()));

                        }
                        mAdapter.notifyDataSetChanged();

                    } else {
                        Utils.showToast(context, context.getString(R.string.alert_past_date));
                    }
                }
            }
        });
    }

    public HashMap<Integer, ArrayList<CalenderAvailableCellModel>> getTempDateHashMAp() {
        return mDateMap;
    }

    private void setUpCalendarAdapter() {
        List<CalenderAvailableCellModel> dayValueInCells = new ArrayList<CalenderAvailableCellModel>();
        Calendar mCal = (Calendar) cal.clone();
        mCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;
        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);


        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
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
//            dayValueInCells.add(mCal.getTime());
            dayValueInCells.add(data);
            mCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        Log.d(TAG, "Number of date " + dayValueInCells.size());
        String sDate = formatter.format(cal.getTime());
        currentDate.setText(sDate);
        mAdapter = new CalendarGridAdapter(context, dayValueInCells, cal);
        calendarGridView.setAdapter(mAdapter);
//        setFontFaceLatoBold(currentDate);
    }


}