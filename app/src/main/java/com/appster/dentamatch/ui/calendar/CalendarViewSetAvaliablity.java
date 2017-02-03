package com.appster.dentamatch.ui.calendar;

import android.content.Context;
import android.graphics.Typeface;
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

public class CalendarViewSetAvaliablity extends LinearLayout {
    private static final String TAG = CalendarViewSetAvaliablity.class.getSimpleName();
    private ImageView previousButton, nextButton;
    private TextView currentDate;
    private GridView calendarGridView;
    private static final int MAX_CALENDAR_COLUMN = 35;
    private int month, year;
    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    private Context context;
    private CalendarGridAdapter mAdapter;
    List<CalenderAvailableCellModel> dayValueInCells = new ArrayList<CalenderAvailableCellModel>();


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
        Log.d(TAG, "I need to call this method");
    }

    public CalendarViewSetAvaliablity(Context context, AttributeSet attrs, int defStyleAttr) {
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
                cal.add(Calendar.MONTH, -1);
                setUpCalendarAdapter();
            }
        });
    }

    private void setNextButtonClickEvent() {
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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
//                if((Integer)view.getTag()!=-1)
                    if(dayValueInCells.get(position).isSelected()){
                        view.setBackgroundResource(R.color.white_color);

                        dayValueInCells.get(position).setSelected(false);

                    }else{
                        view.setBackgroundResource(R.drawable.shape_temporary_date_selection);

                        dayValueInCells.get(position).setSelected(true);

                    }

            }
        });
    }

    private void setUpCalendarAdapter() {
//        List<Date> dayValueInCells = new ArrayList<Date>();
//        mQuery = new DatabaseQuery(context);
        List<EventObjects> mEvents = getAllFutureEvents();
        Calendar mCal = (Calendar) cal.clone();
        mCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;
        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        while (dayValueInCells.size() < MAX_CALENDAR_COLUMN) {
            CalenderAvailableCellModel data=new CalenderAvailableCellModel();
            data.setDate(mCal.getTime());
            data.setSelected(false);
//            dayValueInCells.add(mCal.getTime());
            dayValueInCells.add(data);
            mCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        Log.d(TAG, "Number of date " + dayValueInCells.size());
        String sDate = formatter.format(cal.getTime());
        currentDate.setText(sDate);
        mAdapter = new CalendarGridAdapter(context, dayValueInCells, cal, mEvents);
        calendarGridView.setAdapter(mAdapter);
//        setFontFaceLatoBold(currentDate);
    }

    public List<EventObjects> getAllFutureEvents() {
        Calendar cal = Calendar.getInstance();

        List<EventObjects> list = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
//            dt.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            EventObjects eventObjects = new EventObjects("" + i,  null,cal.get(Calendar.DAY_OF_MONTH)-i);
            list.add(eventObjects);
        }
        return list;
    }
        public  void setFontFaceLatoBold( TextView view) {
            Typeface tf = Typeface.createFromAsset(view.getContext()
                    .getAssets(), "untitled-font-6.ttf");

            view.setTypeface(tf);
            view.setText("a");

    }
}