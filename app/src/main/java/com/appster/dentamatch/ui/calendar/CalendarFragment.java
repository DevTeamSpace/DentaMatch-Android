package com.appster.dentamatch.ui.calendar;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.FragmentCalendarBinding;
import com.appster.dentamatch.ui.common.BaseFragment;

/**
 * Created by Appster on 23/01/17.
 */

public class CalendarFragment extends BaseFragment implements View.OnClickListener {
    private FragmentCalendarBinding calendarBinding;


    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }


    @Override
    public String getFragmentName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        calendarBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false);
//        fragmentManager = getActivity().getSupportFragmentManager();
//        fragmentTransaction = fragmentManager.beginTransaction();
        initViews();
        return calendarBinding.getRoot();
//        return inflater.inflate(R.layout.fragment_calendar, container, false);


    }

    private void initViews() {
//        calendarBinding.toolbarCalendar.tvToolbarGeneralLeft.setVisibility(View.GONE);
////        calendarBinding.toolbarCalendar.ivToolBarLeft.setOnClickListener(this);
//        calendarBinding.toolbarCalendar.txvToolbarGeneralRight.setOnClickListener(this);
//        calendarBinding.toolbarCalendar.txvToolbarGeneralRight.setCompoundDrawables(ContextCompat.getDrawable(getActivity(), android.R.drawable.btn_plus), null, null, null);
//        calendarBinding.toolbarCalendar.tvToolbarGeneralLeft.setText("CALENDER");
//        calendarBinding.toolbarCalendar.ivToolBarRight.setBackgroundResource(R.drawable.ic_profile_plus);

        calendarBinding.toolbarCalendar.ivToolBarLeft.setVisibility(View.GONE);
        calendarBinding.toolbarCalendar.tvToolbarGeneralLeft.setText("CALENDER");
        calendarBinding.toolbarCalendar.ivToolBarRight.setVisibility(View.VISIBLE);
        calendarBinding.toolbarCalendar.txvToolbarGeneralRight.setVisibility(View.VISIBLE);
        calendarBinding.toolbarCalendar.txvToolbarGeneralRight.setCompoundDrawables(ContextCompat.getDrawable(getActivity(), android.R.drawable.btn_plus), null, null,null);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_tool_bar_left:
                break;
            case R.id.iv_tool_bar_right:
                startActivity(new Intent(getActivity(), SetAvailabilityActivity.class));
                break;
        }

    }
}
