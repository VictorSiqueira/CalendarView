package com.nurik.calendar.utils

import com.nurik.calendar.CalendarDate
import com.nurik.calendar.CalendarView


/**
 * Internal interface that defines methods for providing required information
 * for the specific [CalendarDate].
 */
interface DateInfoProvider {

    fun isToday(date: CalendarDate): Boolean

    fun isDateSelected(date: CalendarDate): Boolean

    fun isDateOutOfRange(date: CalendarDate): Boolean

    fun isDateSelectable(date: CalendarDate): Boolean

    fun isWeekend(date: CalendarDate): Boolean

    fun getDateIndicators(date: CalendarDate): List<CalendarView.DateIndicator>

}