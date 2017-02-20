package com.appster.dentamatch.ui.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.network.response.jobs.HiredJobs;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarEventGridAdapter extends ArrayAdapter {
    private static final String TAG = CalendarEventGridAdapter.class.getSimpleName();
    private LayoutInflater mInflater;
    private List<CalenderAvailableCellModel> monthlyDates;
    private Calendar currentDate;
    private Context mContext;
    private ArrayList<HiredJobs> mJobList;


    public CalendarEventGridAdapter(Context context, List<CalenderAvailableCellModel> monthlyDates, Calendar currentDate) {
        super(context, R.layout.single_cell_layout);
        this.monthlyDates = monthlyDates;
        this.currentDate = currentDate;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }


    public void setJobList(ArrayList<HiredJobs> list) {
        if (mJobList != null) {
            mJobList = null;
        }
        mJobList = new ArrayList<>();
        mJobList = list;

        notifyDataSetChanged();

    }

    public List<CalenderAvailableCellModel> getList() {
        return monthlyDates;
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
//        LogUtils.LOGD(TAG,"date is current="+currentYear+"-"+currentMonth+"-"+currentDay);
//        LogUtils.LOGD(TAG,"date is displayed="+displayYear+"-"+displayMonth+"-"+dayValue);
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.single_cell_layout, parent, false);
        }
        //Add day to calendar
        TextView cellNumber = (TextView) view.findViewById(R.id.calendar_date_id);
//        ImageView dot1 = (ImageView) view.findViewById(R.id.dot1);
        ImageView dot2 = (ImageView) view.findViewById(R.id.dot2);
        ImageView dot3 = (ImageView) view.findViewById(R.id.dot3);
        view.setVisibility(View.VISIBLE);

        if (displayMonth == currentMonth && displayYear == currentYear) {
            LogUtils.LOGD(TAG, "date is displayed=" + displayYear + "-" + displayMonth + "-" + dayValue);

            cellNumber.setText(String.valueOf(dayValue));
            view.setTag(position);

            if (monthlyDates.get(position).isSelected()) {
                cellNumber.setTextColor(ContextCompat.getColor(mContext, R.color.white_color));
                view.setBackgroundResource(R.drawable.shape_date_selection);

            } else {
                cellNumber.setTextColor(ContextCompat.getColor(mContext, R.color.graish_brown_color));

                view.setBackgroundResource(0);

            }

            if (mJobList != null && mJobList.size() > 0) {
                String date = Utils.dateFormetyyyyMMdd(mDate);
                Calendar eventCalendar = Calendar.getInstance();
                eventCalendar.setTime(mDate);
                String day = Utils.getDayOfWeek(date);
                dot2.setVisibility(View.GONE);
                for (int k = 0; k < mJobList.size(); k++) {
                    if (Utils.getDate(mJobList.get(k).getJobDate(),Constants.DateFormet.YYYYMMDD).compareTo(Utils.parseDate(mDate)) <= 0) {

                        if (day.equalsIgnoreCase(mContext.getString(R.string.txt_full_monday)) && mJobList.get(k).getIsMonday() == 1) {
                            dot2.setVisibility(View.VISIBLE);
                        } else if (day.equalsIgnoreCase(mContext.getString(R.string.txt_full_tuesday)) && mJobList.get(k).getIsTuesday() == 1) {
                            dot2.setVisibility(View.VISIBLE);
                        } else if (day.equalsIgnoreCase(mContext.getString(R.string.txt_full_wednesday)) && mJobList.get(k).getIsWednesday() == 1) {
                            dot2.setVisibility(View.VISIBLE);
                        } else if (day.equalsIgnoreCase(mContext.getString(R.string.txt_full_thursday)) && mJobList.get(k).getIsThursday() == 1) {
                            dot2.setVisibility(View.VISIBLE);
                        } else if (day.equalsIgnoreCase(mContext.getString(R.string.txt_full_friday)) && mJobList.get(k).getIsFriday() == 1) {
                            dot2.setVisibility(View.VISIBLE);
                        } else if (day.equalsIgnoreCase(mContext.getString(R.string.txt_full_saturday)) && mJobList.get(k).getIsSaturday() == 1) {
                            dot2.setVisibility(View.VISIBLE);
                        } else if (day.equalsIgnoreCase(mContext.getString(R.string.txt_full_sunday)) && mJobList.get(k).getIsSunday() == 1) {
                            dot2.setVisibility(View.VISIBLE);
                        }
                        for (int i = 0; i < mJobList.get(k).getTemporaryJobDates().size(); i++) {
                            if (mJobList.get(k).getTemporaryJobDates().get(i).getJobDate().equalsIgnoreCase(date)) {
                                dot3.setVisibility(View.VISIBLE);
                            }
//                        else {
//                            dot3.setVisibility(View.GONE);
//
//                        }
                        }
                    }
                }
            }


        } else

        {
            view.setBackgroundColor(mContext.getResources().getColor(R.color.white_color));
            view.setVisibility(View.GONE);

            view.setTag(-1);
        }
//
//        Calendar eventCalendar = Calendar.getInstance();
////        eventCalendar.setTime(mDate);
//
//        if (eventCalendar.get(Calendar.MONTH) + 1 == displayMonth && eventCalendar.get(Calendar.YEAR) == displayYear) {
//
//            if (!mIsSetCurrentDate) {
//                if (monthlyDates.get(position).isSelected()) {
//                    view.setBackgroundResource(R.drawable.shape_date_selection);
//
//                } else {
//                    view.setBackgroundResource(0);
//
//                }
//            }
//            if (mJobList != null && mJobList.size() > 0) {
//                String date = Utils.dateFormetyyyyMMdd(mDate);
//                eventCalendar.setTime(mDate);
//                String day = Utils.getDayOfWeek(date);
//                dot2.setVisibility(View.GONE);
//                for (int k = 0; k < mJobList.size(); k++) {
//                    if (day.equalsIgnoreCase(mContext.getString(R.string.txt_full_monday)) && mJobList.get(k).getIsMonday() == 1) {
//                        dot2.setVisibility(View.VISIBLE);
//                    } else if (day.equalsIgnoreCase(mContext.getString(R.string.txt_full_tuesday)) && mJobList.get(k).getIsTuesday() == 1) {
//                        dot2.setVisibility(View.VISIBLE);
//                    } else if (day.equalsIgnoreCase(mContext.getString(R.string.txt_full_wednesday)) && mJobList.get(k).getIsWednesday() == 1) {
//                        dot2.setVisibility(View.VISIBLE);
//                    } else if (day.equalsIgnoreCase(mContext.getString(R.string.txt_full_thursday)) && mJobList.get(k).getIsThursday() == 1) {
//                        dot2.setVisibility(View.VISIBLE);
//                    } else if (day.equalsIgnoreCase(mContext.getString(R.string.txt_full_friday)) && mJobList.get(k).getIsFriday() == 1) {
//                        dot2.setVisibility(View.VISIBLE);
//                    } else if (day.equalsIgnoreCase(mContext.getString(R.string.txt_full_saturday)) && mJobList.get(k).getIsSaturday() == 1) {
//                        dot2.setVisibility(View.VISIBLE);
//                    } else if (day.equalsIgnoreCase(mContext.getString(R.string.txt_full_sunday)) && mJobList.get(k).getIsSunday() == 1) {
//                        dot2.setVisibility(View.VISIBLE);
//                    }
//                    for (int i = 0; i < mJobList.get(k).getTemporaryJobDates().size(); i++) {
//                        if (mJobList.get(k).getTemporaryJobDates().get(i).getJobDate().equalsIgnoreCase(date)) {
//                            dot3.setVisibility(View.VISIBLE);
//
//                        }
////                        else {
////                            dot3.setVisibility(View.GONE);
////
////                        }
//                    }
//                }
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