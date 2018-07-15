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

import static android.view.View.GONE;

class CalendarEventGridAdapter extends ArrayAdapter<CalenderAvailableCellModel> {
    private static final String TAG = LogUtils.makeLogTag(CalendarEventGridAdapter.class);
    private final LayoutInflater mInflater;
    private final List<CalenderAvailableCellModel> monthlyDates;
    private final Calendar mCurrentDate;
    private final Context mContext;
    private ArrayList<HiredJobs> mJobList;


    CalendarEventGridAdapter(Context context, List<CalenderAvailableCellModel> monthlyDates, Calendar currentDate) {
        super(context, R.layout.single_cell_layout);
        this.monthlyDates = monthlyDates;
        this.mCurrentDate = currentDate;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }


    void setJobList(ArrayList<HiredJobs> list) {
        if (mJobList != null) {
            mJobList = null;
        }

        mJobList = new ArrayList<>();
        mJobList.addAll(list);

        notifyDataSetChanged();
    }

    public List<CalenderAvailableCellModel> getList() {
        return monthlyDates;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Date mDate = monthlyDates.get(position).getDate();
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

        /*
         * Add day to calendar
         */
        TextView cellNumber = view.findViewById(R.id.calendar_date_id);
        ImageView dot2 = view.findViewById(R.id.dot2);
        ImageView dot3 = view.findViewById(R.id.dot3);
        view.setVisibility(View.VISIBLE);

        if (displayMonth == currentMonth && displayYear == currentYear) {
            cellNumber.setText(String.valueOf(dayValue));
            view.setTag(position);

            if (monthlyDates.get(position).isSelected()) {
                cellNumber.setTextColor(ContextCompat.getColor(mContext, R.color.white_color));
                view.setBackgroundResource(R.drawable.shape_date_selection);

            } else {
                cellNumber.setTextColor(ContextCompat.getColor(mContext, R.color.grayish_brown_color));
                view.setBackgroundResource(0);
            }

            if (mJobList != null && mJobList.size() > 0) {
                String date = Utils.dateFormetyyyyMMdd(mDate);
                Calendar eventCalendar = Calendar.getInstance();
                eventCalendar.setTime(mDate);
                String day = Utils.getDayOfWeek(date);
                dot2.setVisibility(GONE);
                dot3.setVisibility(GONE);

                try {
                    for (int k = 0; k < mJobList.size(); k++) {
                        Date jobDate = Utils.getDate(mJobList.get(k).getJobDate(), Constants.DateFormat.YYYYMMDD);

                        if (jobDate != null && jobDate.compareTo(Utils.parseDate(mDate)) <= 0) {

                            if ((day.equalsIgnoreCase(mContext.getString(R.string.txt_full_monday)) && mJobList.get(k).getIsMonday() == 1)
                                    || (day.equalsIgnoreCase(mContext.getString(R.string.txt_full_tuesday)) && mJobList.get(k).getIsTuesday() == 1)
                                    || (day.equalsIgnoreCase(mContext.getString(R.string.txt_full_wednesday)) && mJobList.get(k).getIsWednesday() == 1)
                                    || (day.equalsIgnoreCase(mContext.getString(R.string.txt_full_thursday)) && mJobList.get(k).getIsThursday() == 1)
                                    || (day.equalsIgnoreCase(mContext.getString(R.string.txt_full_friday)) && mJobList.get(k).getIsFriday() == 1)
                                    || (day.equalsIgnoreCase(mContext.getString(R.string.txt_full_saturday)) && mJobList.get(k).getIsSaturday() == 1)
                                    || (day.equalsIgnoreCase(mContext.getString(R.string.txt_full_sunday)) && mJobList.get(k).getIsSunday() == 1)) {
                                dot2.setVisibility(View.VISIBLE);

                            }
                        }

                        if ((mJobList.get(k).getTempDates() != null && mJobList.get(k).getTempDates().equalsIgnoreCase(date))) {
                            dot3.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                    LogUtils.LOGE(TAG, e.getMessage());
                }
            } else {
                dot2.setVisibility(GONE);
                dot3.setVisibility(GONE);
            }

        } else {
            view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white_color));
            view.setVisibility(GONE);
            view.setTag(-1);
        }

        return view;
    }

    @Override
    public int getCount() {
        return monthlyDates.size();
    }

    @Nullable
    @Override
    public CalenderAvailableCellModel getItem(int position) {
        return monthlyDates.get(position);
    }

    @Override
    public int getPosition(CalenderAvailableCellModel item) {
        return monthlyDates.indexOf(item);
    }
}