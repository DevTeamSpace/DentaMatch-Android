package com.appster.dentamatch.ui.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.appster.dentamatch.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarGridAdapter extends ArrayAdapter {
    private static final String TAG = CalendarGridAdapter.class.getSimpleName();
    private LayoutInflater mInflater;
    //    private List<Date> monthlyDates;
    private List<CalenderAvailableCellModel> monthlyDates;
    private Calendar currentDate;
    private List<EventObjects> allEvents;
    private Context mContext;

    public CalendarGridAdapter(Context context, List<CalenderAvailableCellModel> monthlyDates, Calendar currentDate, List<EventObjects> allEvents) {
        super(context, R.layout.single_cell_layout);
        this.monthlyDates = monthlyDates;
        this.currentDate = currentDate;
        this.allEvents = allEvents;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }
    public  List<CalenderAvailableCellModel> getList(){
       return  monthlyDates;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        Date mDate = monthlyDates.get(position);
        Date mDate = monthlyDates.get(position).getDate();
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(mDate);
        int dayValue = dateCal.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCal.get(Calendar.MONTH) + 1;
        int displayYear = dateCal.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
        int currentYear = currentDate.get(Calendar.YEAR);
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.single_cell_layout, parent, false);
        }
        //Add day to calendar
        TextView cellNumber = (TextView) view.findViewById(R.id.calendar_date_id);
        ImageView dot1 = (ImageView) view.findViewById(R.id.dot1);
        ImageView dot2 = (ImageView) view.findViewById(R.id.dot2);
        ImageView dot3 = (ImageView) view.findViewById(R.id.dot3);
//        cellNumber.setText(String.valueOf(dayValue));

        if (displayMonth == currentMonth && displayYear == currentYear) {
//            view.setBackgroundColor(Color.parseColor(R.color.colorPrimary));
            cellNumber.setText(String.valueOf(dayValue));
            view.setTag(position);
            view.setBackgroundColor(mContext.getResources().getColor(R.color.white_color));
//            if (dayValue == currentDay) {
//                view.setBackgroundResource(R.drawable.oval);
//            }
//            if (dayValue == currentDay - 10) {
//
//                view.setBackgroundResource(R.drawable.shape_date_selection);
//
//            }
            if (monthlyDates.get(position).isSelected()) {
                view.setBackgroundResource(R.drawable.shape_temporary_date_selection);

            } else {
                view.setBackgroundResource(0);

            }
//            for (int i = 0; i < allEvents.size(); i++) {
////            eventCalendar.setTime(allEvents.get(i).getDate());
////            if (dayValue == eventCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth == eventCalendar.get(Calendar.MONTH) + 1
////                    && displayYear == eventCalendar.get(Calendar.YEAR)) {
////                eventIndicator.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
////            }
//                if (dayValue == allEvents.get(i).getDayOfMonth()) {
////                    eventIndicator.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
//                    if (allEvents.get(i).getDayOfMonth() % 6 == 0) {
//                        dot1.setVisibility(View.VISIBLE);
//                        dot2.setVisibility(View.VISIBLE);
//                        dot3.setVisibility(View.VISIBLE);
//                    } else if (allEvents.get(i).getDayOfMonth() % 7 == 0) {
//                        dot1.setVisibility(View.VISIBLE);
//                        dot2.setVisibility(View.VISIBLE);
//                        dot3.setVisibility(View.GONE);
//                    } else if (allEvents.get(i).getDayOfMonth() % 5 == 0) {
//                        dot1.setVisibility(View.VISIBLE);
//                        dot2.setVisibility(View.GONE);
//                        dot3.setVisibility(View.GONE);
//                    }
//                }
//            }
        } else

        {
//            view.setBackgroundColor(Color.parseColor("#cccccc"));
//            view.setBackgroundColor(Color.parseColor("#cccccc"));
            view.setBackgroundColor(mContext.getResources().getColor(R.color.white_color));

            view.setTag(-1);
        }

        //Add events to the calendar
//        TextView eventIndicator = (TextView) view.findViewById(R.id.event_id);
//
//        for (int i = 0; i < allEvents.size(); i++) {
//            eventCalendar.setTime(allEvents.get(i).getDate());
//            if (dayValue == eventCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth == eventCalendar.get(Calendar.MONTH) + 1
//                    && displayYear == eventCalendar.get(Calendar.YEAR)) {
//                eventIndicator.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
//            }
//        }
        return view;
    }

    @Override
    public int getCount() {
        return monthlyDates.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return monthlyDates.get(position);
    }

    @Override
    public int getPosition(Object item) {
        return monthlyDates.indexOf(item);
    }
}