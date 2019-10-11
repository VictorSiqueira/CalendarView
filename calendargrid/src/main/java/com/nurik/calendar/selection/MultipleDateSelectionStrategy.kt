package com.nurik.calendar.selection

import android.os.Bundle
import com.nurik.calendar.CalendarDate
import com.nurik.calendar.adapter.manager.AdapterDataManager
import com.nurik.calendar.utils.DateInfoProvider

internal class MultipleDateSelectionStrategy(
    private val adapterDataManager: AdapterDataManager,
    private val dateInfoProvider: DateInfoProvider

) : DateSelectionStrategy {

    companion object {
        private const val BUNDLE_SELECTED_DATES = "com.nurik.calendar.selected_items"
    }

    private val selectedDates = linkedSetOf<CalendarDate>()

    override fun onDateSelected(date: CalendarDate) {
        if (dateInfoProvider.isDateSelectable(date).not()) {
            return
        }

        val dateWasRemoved = selectedDates.remove(date)
        if (dateWasRemoved.not()) {
            selectedDates.add(date)
        }

        val datePosition = adapterDataManager.findDatePosition(date)
        if (datePosition != -1) {
            adapterDataManager.notifyDateItemChanged(datePosition)
        }
    }

    override fun getSelectedDates(): List<CalendarDate> {
        return selectedDates.toList()
    }

    override fun isDateSelected(date: CalendarDate): Boolean {
        return selectedDates.contains(date)
    }

    override fun saveSelectedDates(bundle: Bundle) {
        bundle.putParcelableArray(BUNDLE_SELECTED_DATES, selectedDates.toTypedArray())
    }

    override fun restoreSelectedDates(bundle: Bundle) {
        val selectedDatesArray = bundle.getParcelableArray(BUNDLE_SELECTED_DATES)
        selectedDatesArray?.mapTo(selectedDates) { it as CalendarDate }
    }

    override fun clear() {
        selectedDates.clear()
        adapterDataManager.notifyDateItemsChanged()
    }

}