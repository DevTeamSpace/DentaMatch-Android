package com.appster.dentamatch.presentation.calendar

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appster.dentamatch.R
import kotlinx.android.synthetic.main.fragment_calendar_pager.*

open class CalendarPagerFragment : Fragment() {

    companion object {

        const val ARGS_MONTH = "month"

        fun newInstance(month: CalendarMonthModel) =
                CalendarPagerFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ARGS_MONTH, month)
                    }
                }
    }

    protected var month: CalendarMonthModel? = null
    protected var offset = 0
    protected var listener: CalendarPagerAdapter.CalendarPagerListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.month = arguments?.getParcelable(ARGS_MONTH)
        offset = month?.offset ?: 0
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_calendar_pager, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (month != null) {
            calendarGrid.adapter = CalendarEventGridAdapter(month!!, offset, listener)
        }
    }

    fun setCalendarPagerListener(listener: CalendarPagerAdapter.CalendarPagerListener) {
        this.listener = listener
    }
}