package com.nurik.calendar.adapter.manager

import com.nurik.calendar.CalendarDate
import com.nurik.calendar.adapter.CalendarAdapter

internal class CalendarAdapterDataManager(
    private val calendarAdapter: CalendarAdapter) : AdapterDataManager {

    override fun findDatePosition(date: CalendarDate): Int {
        return calendarAdapter.findDatePosition(date)
    }

    override fun getDatesRange(dateFrom: CalendarDate, dateTo: CalendarDate): List<CalendarDate> {
        return calendarAdapter.getDatesRange(dateFrom = dateFrom, dateTo = dateTo)
    }

    override fun notifyDateItemChanged(position: Int) {
        calendarAdapter.notifyItemChanged(position)
    }

    override fun notifyDateItemsChanged() {
        calendarAdapter.notifyDataSetChanged()
    }
}