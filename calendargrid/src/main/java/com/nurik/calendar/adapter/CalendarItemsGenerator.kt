package com.nurik.calendar.adapter

import com.nurik.calendar.CalendarConst
import com.nurik.calendar.CalendarDate
import com.nurik.calendar.adapter.item.CalendarItem
import com.nurik.calendar.adapter.item.DateItem
import com.nurik.calendar.adapter.item.EmptyItem
import java.util.*

/**
 * This internal class responsible for generation items for the [CalendarAdapter].
 */
internal class CalendarItemsGenerator(private val firstDayOfWeek: Int) {

    /**
     * List of days of week according to [firstDayOfWeek].
     *
     * For example, when [firstDayOfWeek] is [Calendar.MONDAY] list looks like:
     * [MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY]
     */
    private val daysOfWeek = mutableListOf<Int>().apply {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek)

        repeat(CalendarConst.DAYS_IN_WEEK) {
            this += calendar.get(Calendar.DAY_OF_WEEK)
            calendar.add(Calendar.DAY_OF_WEEK, 1)
        }
    }

    /**
     * Generate calendar items for months between [dateFrom] and [dateTo]
     */
    fun generateCalendarItems(dateFrom: CalendarDate, dateTo: CalendarDate): List<CalendarItem> {
        val calendar = Calendar.getInstance()
        calendar.time = dateFrom.date

        var calendarItems = mutableListOf<CalendarItem>()
        val monthsBetween = dateFrom.monthsBetween(dateTo)

        repeat(monthsBetween.inc()) {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val itemsForMonth = generateCalendarItemsForMonth(year, month)

            calendarItems.addAll(itemsForMonth)
            calendar.add(Calendar.MONTH, 1)
        }
        calendarItems = addOffsetToItems(calendarItems)
        return calendarItems
    }

    private fun generateCalendarItemsForMonth(year: Int, month: Int): List<CalendarItem> {
        val itemsForMonth = mutableListOf<CalendarItem>()
        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = firstDayOfWeek
        calendar.set(year, month, 1)

        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        calendar.set(Calendar.DAY_OF_MONTH, daysInMonth)
        calendar.set(year, month, 1)

        // Add date items
        repeat(daysInMonth) {
            val date = CalendarDate(calendar.time)
            itemsForMonth += DateItem(date)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return itemsForMonth
    }


    private fun addOffsetToItems(list : List<CalendarItem>): MutableList<CalendarItem> {
        val newList = mutableListOf<CalendarItem>()
        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = firstDayOfWeek

        var item: DateItem = list.get(0)  as DateItem

        calendar.set(item.date.year, item.date.month, 1)
        val firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK)
        val startOffset = daysOfWeek.indexOf(firstDayOfMonth)
        clearEmptyItems(list, newList)
        // Add empty items for start offset
        //repeat(startOffset) { newList += EmptyItem }
        // Add date items
        for(l in list) {
            newList += l
        }
        return newList
    }

    private fun clearEmptyItems(list: List<CalendarItem>, newList: MutableList<CalendarItem>) {
        var removeList = mutableListOf<CalendarItem>()
        for (l in list) {
            when (l) {
                is EmptyItem -> removeList.add(l)
            }
            newList += l
        }
        newList.removeAll(removeList)
    }
}