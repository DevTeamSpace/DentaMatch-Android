package com.appster.dentamatch.ui.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.appster.dentamatch.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarGridAdapter extends ArrayAdapter {
    private LayoutInflater mInflater;
    private List<CalenderAvailableCellModel> mMonthlyDates;
    private Calendar mCurrentDate;
    private Context mContext;

    public CalendarGridAdapter(Context context, List<CalenderAvailableCellModel> monthlyDates, Calendar currentDate) {
        super(context, R.layout.single_cell_layout);
        this.mMonthlyDates = monthlyDates;
        this.mCurrentDate = currentDate;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public List<CalenderAvailableCellModel> getList() {
        return mMonthlyDates;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Date mDate = mMonthlyDates.get(position).getDate();
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(mDate);
        int dayValue = dateCal.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCal.get(Calendar.MONTH) + 1;
        int displayYear = dateCal.get(Calendar.YEAR);
        int currentMonth = mCurrentDate.get(Calendar.MONTH) + 1;
        int currentYear = mCurrentDate.get(Calendar.YEAR);
        View view = convertView;

        if (view == null) {
            view = mInflater.inflate(R.layout.single_cell_layout, parent, false);
        }

        /**
         * Add day to calendar
         */
        TextView cellNumber = (TextView) view.findViewById(R.id.calendar_date_id);

        if (displayMonth == currentMonth && displayYear == currentYear) {
            cellNumber.setText(String.valueOf(dayValue));
            view.setTag(position);
            view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white_color));

            if (mMonthlyDates.get(position).isSelected()) {
                view.setBackgroundResource(R.drawable.shape_temporary_date_selection);
                cellNumber.setTextColor(ContextCompat.getColor(mContext, R.color.white_color));
            } else {
                cellNumber.setTextColor(ContextCompat.getColor(mContext, R.color.grayish_brown_color));
                view.setBackgroundResource(0);
            }

        } else {
            view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white_color));
            view.setTag(-1);
        }

        return view;
    }

    @Override
    public int getCount() {
        return mMonthlyDates.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return mMonthlyDates.get(position);
    }

    @Override
    public int getPosition(Object item) {
        return mMonthlyDates.indexOf(item);
    }
}