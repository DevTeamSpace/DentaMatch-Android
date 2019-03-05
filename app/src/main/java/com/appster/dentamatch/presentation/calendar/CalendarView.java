/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appster.dentamatch.R;

import java.util.Calendar;
import java.util.Locale;

public class CalendarView extends LinearLayout
        implements CalendarPagerAdapter.CalendarPagerListener, ViewPager.OnPageChangeListener {

    private ImageView mIvPreviousButton, mIvNextButton;
    private TextView mCurrentDate;
    private Context mContext;
    private ViewPager mPager;

    @Nullable
    protected CalendarPagerAdapter mPagerAdapter;

    @Nullable
    private CalendarPagerAdapter.CalendarPagerListener mListener;

    @Nullable
    protected CalendarModel mCalendarModel;

    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initializeUILayout();
        setPreviousButtonClickEvent();
        setNextButtonClickEvent();
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initializeUILayout() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View calendarView = inflater.inflate(R.layout.calendar_layout, this);
        mIvPreviousButton = calendarView.findViewById(R.id.previous_month);
        mIvNextButton = calendarView.findViewById(R.id.next_month);
        mCurrentDate = calendarView.findViewById(R.id.display_current_date);
        mPager = calendarView.findViewById(R.id.calendar_view_pager);
    }

    void initialize(@NonNull FragmentManager fragmentManager) {
        initializePagerAdapter(fragmentManager);
        initializePager();
    }

    void initializePagerAdapter(@NonNull FragmentManager fragmentManager) {
        mPagerAdapter = new CalendarPagerAdapter(this, fragmentManager);
    }

    void initializePager() {
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(this);
        if (mCalendarModel != null && mPagerAdapter != null) {
            mPagerAdapter.setData(mCalendarModel.getMonths());
        }
    }

    private void setPreviousButtonClickEvent() {
        mIvPreviousButton.setOnClickListener(v ->
                mPager.setCurrentItem(mPager.getCurrentItem() - 1, true));
    }

    private void setNextButtonClickEvent() {
        mIvNextButton.setOnClickListener(v ->
                mPager.setCurrentItem(mPager.getCurrentItem() + 1, true));
    }

    public void onMonthChanged(int month) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.set(Calendar.MONTH, month);
        String monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
        String yearName = String.valueOf(calendar.get(Calendar.YEAR));
        mCurrentDate.setText(getContext().getString(R.string.calendar_month, monthName, yearName));
        if (mCalendarModel != null) {
            onDaySelected(getSelectedDay(mCalendarModel));
        }
    }

    @Override
    public void onDaySelected(@Nullable CalendarDayModel day) {
        if (mListener != null && day != null) {
            mListener.onDaySelected(day);
        }
    }

    public void setCalendarModel(@NonNull CalendarModel calendarModel) {
        mCalendarModel = calendarModel;
        if (mPagerAdapter != null) {
            mPagerAdapter.setData(calendarModel.getMonths());
        }
        onDaySelected(getSelectedDay(calendarModel));
    }

    @Nullable
    private CalendarDayModel getSelectedDay(@NonNull CalendarModel calendarModel) {
        return calendarModel.getMonths().get(mPager.getCurrentItem()).getSelected();
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {  }

    @Override
    public void onPageSelected(int i) {
        onMonthChanged(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {  }

    public void setListener(@NonNull CalendarPagerAdapter.CalendarPagerListener listener) {
        mListener = listener;
    }

    public void setSelectedPosition(int mCalendarPosition) {
        mPager.setCurrentItem(mCalendarPosition, false);
    }
}