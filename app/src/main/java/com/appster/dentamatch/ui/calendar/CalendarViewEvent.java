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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarViewEvent extends LinearLayout {
    private static final String TAG = CalendarViewEvent.class.getSimpleName();
    private ImageView previousButton, nextButton;
    private TextView currentDate;
    private GridView calendarGridView;
    private static final int MAX_CALENDAR_COLUMN = 35;
    private int month, year;
    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    private Context context;
    private CalendarEventGridAdapter mAdapter;
    private int oldClickedPos = -1;
    private View oldView;

    private List<CalenderAvailableCellModel> mDayList=new ArrayList<>();

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

    public CalendarViewEvent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
//                Toast.makeText(context, "under development", Toast.LENGTH_SHORT).show();
                cal.add(Calendar.MONTH, -1);
                setUpCalendarAdapter();
            }
        });
    }

    private void setNextButtonClickEvent() {
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "under development", Toast.LENGTH_SHORT).show();
                cal.add(Calendar.MONTH, 1);
                setUpCalendarAdapter();
            }
        });
    }

    private void setGridCellClickEvents() {
        calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(context, "Clicked " + (Integer)view.getTag(), Toast.LENGTH_LONG).show();
                if((Integer)view.getTag()!=-1) {
                    if (!mDayList.get(position).isSelected()) {
                        oldClickedPos = position;
                        mDayList.get(position).setSelected(true);
                        mDayList.get(oldClickedPos).setSelected(false);
                        oldView = view;
                    mAdapter = new CalendarEventGridAdapter(context, mDayList, cal, null);
                    calendarGridView.setAdapter(mAdapter);
                    }
                }
                oldView.setBackgroundResource(0);

                view.setBackgroundResource(R.drawable.shape_date_selection);

//                    view.setBackgroundResource(R.drawable.shape_date_selection);

            }
        });
    }

    private void setUpCalendarAdapter() {
//        List<Date> dayValueInCells = new ArrayList<Date>();
        List<CalenderAvailableCellModel> dayValueInCells = new ArrayList<CalenderAvailableCellModel>();
        List<EventObjects> mEvents = getAllFutureEvents();
        Calendar mCal = (Calendar) cal.clone();
        mCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;
        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        while (dayValueInCells.size() < MAX_CALENDAR_COLUMN) {
            CalenderAvailableCellModel data = new CalenderAvailableCellModel();
            data.setDate(mCal.getTime());
            mCal.add(Calendar.DAY_OF_MONTH, 1);

            data.setSelected(false);
//            dayValueInCells.add(mCal.getTime());
            dayValueInCells.add(data);
//            mCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        Log.d(TAG, "Number of date " + dayValueInCells.size());
        String sDate = formatter.format(cal.getTime());
        currentDate.setText(sDate);
        mDayList=dayValueInCells;
        mAdapter = new CalendarEventGridAdapter(context, dayValueInCells, cal, mEvents);
        calendarGridView.setAdapter(mAdapter);
//        setFontFaceLatoBold(currentDate);
    }

    public List<EventObjects> getAllFutureEvents() {
        Calendar cal = Calendar.getInstance();

        List<EventObjects> list = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
//            dt.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            EventObjects eventObjects = new EventObjects("" + i, null, cal.get(Calendar.DAY_OF_MONTH) - i);
            list.add(eventObjects);
        }
        return list;
    }

}