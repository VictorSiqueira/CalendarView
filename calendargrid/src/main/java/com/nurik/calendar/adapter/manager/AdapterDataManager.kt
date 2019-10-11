package com.nurik.calendar.adapter.manager

import com.nurik.calendar.CalendarDate

internal interface AdapterDataManager {

    fun findDatePosition(date: CalendarDate): Int

    fun getDatesRange(dateFrom: CalendarDate, dateTo: CalendarDate): List<CalendarDate>

    fun notifyDateItemChanged(position: Int)

    fun notifyDateItemsChanged()
}