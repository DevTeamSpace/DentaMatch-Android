package com.appster.dentamatch.presentation.calendar

import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.appster.dentamatch.R
import kotlinx.android.synthetic.main.single_cell_layout.view.*
import kotlin.collections.HashMap

open class CalendarEventGridAdapter(
        private val month: CalendarMonthModel,
        private val offset: Int,
        private val listener: CalendarPagerAdapter.CalendarPagerListener?
) : BaseAdapter() {

    private val views = HashMap<Int, View>()

    override fun getCount() = month.days.size + offset

    override fun getItemId(p0: Int) = p0.toLong()

    override fun getItem(p0: Int) = p0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val positionWithOffset = position - offset
        val view: View
        if (positionWithOffset >= 0) {
            val day = month.days[position - offset]
            view = convertView?.apply {
                fillViewWithData(this, day)
            } ?: LayoutInflater.from(parent.context)
                    .inflate(R.layout.single_cell_layout, parent, false).apply {
                        fillViewWithData(this, day)
                    }
        } else {
            view = convertView?.apply {
                visibility = View.GONE
                setOnClickListener(null)
            } ?: LayoutInflater.from(parent.context)
                    .inflate(R.layout.single_cell_layout, parent, false).apply {
                        visibility = View.GONE
                        setOnClickListener(null)
                    }
        }
        views[position] = view
        return view
    }

    private fun fillViewWithData(view: View, day: CalendarDayModel) {
        view.visibility = View.VISIBLE
        view.calendarDateId.text = day.text
        view.dot2.visibility = if (day.isPartTime) View.VISIBLE else View.GONE
        view.dot3.visibility = if (day.isTemporary) View.VISIBLE else View.GONE
        if (isSelected(day)) {
            makeSelected(view)
        } else {
            makeUnSelected(view)
        }
        view.setOnClickListener {
            listener?.onDaySelected(day)
            selectDay(day)
            notifyDataSetChanged()
        }
    }

    protected open fun isSelected(day: CalendarDayModel) =
            day.isSelected || month.selected == day

    protected open fun selectDay(day: CalendarDayModel) {
        month.days.forEach { it.isSelected = false }
        day.isSelected = true
        month.selected = day
    }

    protected open fun makeUnSelected(view: View) {
        view.calendarDateId.background = null
        view.calendarDateId.setTextColor(ResourcesCompat.getColor(view.context.resources, R.color.grayish_brown_color, null))
    }

    protected open fun makeSelected(view: View) {
        view.calendarDateId.setBackgroundResource(R.drawable.shape_date_selection)
        view.calendarDateId.setTextColor(ResourcesCompat.getColor(view.context.resources, android.R.color.white, null))
    }
}