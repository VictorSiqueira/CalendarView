package com.nurik.calendar.selection

import android.os.Bundle
import com.nurik.calendar.CalendarDate
import com.nurik.calendar.adapter.manager.AdapterDataManager
import com.nurik.calendar.utils.DateInfoProvider

internal class SingleDateSelectionStrategy(
    private val adapterDataManager: AdapterDataManager,
    private val dateInfoProvider: DateInfoProvider

) : DateSelectionStrategy {

    companion object {
        private const val BUNDLE_SELECTED_DATE = "com.nurik.calendar.selected_date"
    }

    private var selectedDate: CalendarDate? = null

    override fun onDateSelected(date: CalendarDate) {
        if (dateInfoProvider.isDateSelectable(date).not()) {
            return
        }
        if (selectedDate == date) {
            selectedDate = null
        } else {
            val previousSelectedDate = selectedDate
            selectedDate = date

            if (previousSelectedDate != null) {
                val previousSelectedPosition =
                    adapterDataManager.findDatePosition(previousSelectedDate)
                if (previousSelectedPosition != -1) {
                    adapterDataManager.notifyDateItemChanged(previousSelectedPosition)
                }
            }
        }
        val selectedDatePosition = adapterDataManager.findDatePosition(date)
        if (selectedDatePosition != -1) {
            adapterDataManager.notifyDateItemChanged(selectedDatePosition)
        }
    }

    override fun getSelectedDates(): List<CalendarDate> {
        val selectedDate = selectedDate
        return if (selectedDate != null) {
            listOf(selectedDate)
        } else {
            emptyList()
        }
    }

    override fun isDateSelected(date: CalendarDate): Boolean {
        return selectedDate == date
    }

    override fun saveSelectedDates(bundle: Bundle) {
        bundle.putParcelable(BUNDLE_SELECTED_DATE, selectedDate)
    }

    override fun restoreSelectedDates(bundle: Bundle) {
        selectedDate = bundle.getParcelable(BUNDLE_SELECTED_DATE)
    }

    override fun clear() {
        val date = selectedDate
        if (date != null) {
            selectedDate = null
            val datePosition = adapterDataManager.findDatePosition(date)
            adapterDataManager.notifyDateItemChanged(datePosition)
        }
    }
}