package com.nurik.calendar.selection

import android.os.Bundle
import com.nurik.calendar.CalendarDate

/**
 * Empty date selection strategy implementation that do nothing.
 */
internal class NoDateSelectionStrategy : DateSelectionStrategy {

    override fun onDateSelected(date: CalendarDate) {
        // Do nothing.
    }

    override fun getSelectedDates(): List<CalendarDate> {
        return emptyList()
    }

    override fun isDateSelected(date: CalendarDate): Boolean {
        return false
    }

    override fun saveSelectedDates(bundle: Bundle) {
        // Do nothing.
    }

    override fun restoreSelectedDates(bundle: Bundle) {
        // Do nothing.
    }

    override fun clear() {
        // Do nothing.
    }

}