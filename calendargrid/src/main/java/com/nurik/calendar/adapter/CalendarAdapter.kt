package com.nurik.calendar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nurik.calendar.R
import com.nurik.calendar.CalendarDate
import com.nurik.calendar.CalendarDateView
import com.nurik.calendar.adapter.CalendarAdapter.Companion.DATE_VIEW_TYPE
import com.nurik.calendar.adapter.CalendarAdapter.Companion.EMPTY_VIEW_TYPE
import com.nurik.calendar.adapter.CalendarAdapter.Companion.MONTH_VIEW_TYPE
import com.nurik.calendar.adapter.item.CalendarItem
import com.nurik.calendar.adapter.item.DateItem
import com.nurik.calendar.adapter.item.EmptyItem
import com.nurik.calendar.adapter.item.MonthItem
import com.nurik.calendar.style.CalendarStyleAttributes
import com.nurik.calendar.utils.DateInfoProvider
import java.text.SimpleDateFormat
import java.util.*

/**
 * This internal class provides view items for the Calendar.
 *
 * There are three types of items:
 *
 * [DATE_VIEW_TYPE] - single date cell.
 * [MONTH_VIEW_TYPE] - name of month.
 * [EMPTY_VIEW_TYPE] - empty view that represents start and end offset for each month
 */
class CalendarAdapter(
    private val styleAttributes: CalendarStyleAttributes,
    private val dateInfoProvider: DateInfoProvider,
    private val onDateClickListener: (CalendarDate, Boolean) -> Unit

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val MONTH_FORMAT = "LLLL yyyy"
        const val DAY_FORMAT = "d"
        const val DATE_VIEW_TYPE = 0
        const val MONTH_VIEW_TYPE = 1
        const val EMPTY_VIEW_TYPE = 2
    }

    private val calendarItems = mutableListOf<CalendarItem>()
    private val monthFormatter = SimpleDateFormat(MONTH_FORMAT, Locale.getDefault())
    private val dayFormatter = SimpleDateFormat(DAY_FORMAT, Locale.getDefault())

    override fun getItemViewType(position: Int): Int {
        return when (calendarItems[position]) {
            is DateItem -> DATE_VIEW_TYPE
            is MonthItem -> MONTH_VIEW_TYPE
            is EmptyItem -> EMPTY_VIEW_TYPE
            else -> throw IllegalStateException("Unknown item at position $position")
        }
    }

    override fun getItemCount(): Int {
        return calendarItems.size
    }


    fun getItemAtPosition(position: Int): CalendarItem{
        return calendarItems[position]
    }

    // region View Holders creation
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            DATE_VIEW_TYPE -> createDateItemViewHolder(parent.context)
            MONTH_VIEW_TYPE -> createMonthItemViewHolder(parent)
            EMPTY_VIEW_TYPE -> createEmptyItemViewHolder(parent.context)
            else -> throw IllegalStateException("Unknown view type: $viewType")
        }
    }

    private fun createDateItemViewHolder(context: Context): DateItemViewHolder {
        val dateView = CalendarDateView(context)
        val dayItemViewHolder = DateItemViewHolder(dateView)
        dateView.setOnClickListener {
            val adapterPosition = dayItemViewHolder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                val dateItem = calendarItems[adapterPosition] as DateItem
                onDateClickListener.invoke(dateItem.date, false)
            }
        }
        dateView.setOnLongClickListener {
            val adapterPosition = dayItemViewHolder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                val dateItem = calendarItems[adapterPosition] as DateItem
                onDateClickListener.invoke(dateItem.date, true)
            }
            return@setOnLongClickListener true
        }
        return dayItemViewHolder
    }

    private fun createMonthItemViewHolder(parent: ViewGroup): MonthItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_item_month, parent, false)
        return MonthItemViewHolder(view as TextView)
    }

    private fun createEmptyItemViewHolder(context: Context): EmptyItemViewHolder {
        val view = View(context)
        return EmptyItemViewHolder(view)
    }
    // endregion View Holders creation

    // region View Binding
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            DATE_VIEW_TYPE -> {
                val dateItemViewHolder = holder as DateItemViewHolder
                val dateItem = calendarItems[position] as DateItem
                bindDateItemViewHolder(dateItemViewHolder, dateItem)
            }
            MONTH_VIEW_TYPE -> {
                val monthItemViewHolder = holder as MonthItemViewHolder
                val monthItem = calendarItems[position] as MonthItem
                bindMonthItemViewHolder(monthItemViewHolder, monthItem)
            }
        }
    }

    private fun bindDateItemViewHolder(holder: DateItemViewHolder, dateItem: DateItem) {
        val date = dateItem.date
        val dateView = holder.dateView

        dateView.isToday = dateInfoProvider.isToday(date)
        dateView.isDateSelected = dateInfoProvider.isDateSelected(date)

        dateView.isDateDisabled =
            dateInfoProvider.isDateOutOfRange(date) || dateInfoProvider.isDateSelectable(date).not()


        /*if (date.calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
            date.calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            dateView.isWeekend = true
        }else {
            dateView.isWeekend = false
        }*/

        dateView.dateIndicators = dateInfoProvider.getDateIndicators(date)

        dateView.dayNumber = dayFormatter.format(date.date)

        dateView.setBackgroundResource(styleAttributes.dateCellBackgroundColorRes)
        dateView.textColorStateList = styleAttributes.dateCellTextColorStateList
    }

    private fun bindMonthItemViewHolder(holder: MonthItemViewHolder, monthItem: MonthItem) {
        val monthName = monthFormatter.format(monthItem.date.date)
        holder.textView.text = monthName.capitalize()
        holder.textView.setTextColor(styleAttributes.monthTextColor)
    }
    // endregion View Binding

    fun findMonthPosition(date: CalendarDate): Int {
        val year = date.year
        val month = date.month
        return calendarItems.indexOfFirst { item ->
            if (item is MonthItem) {
                if (item.date.year == year && item.date.month == month) {
                    return@indexOfFirst true
                }
            }
            return@indexOfFirst false
        }
    }

    fun findDatePosition(date: CalendarDate): Int {
        return calendarItems.indexOfFirst { item ->
            if (item is DateItem) {
                if (item.date == date) {
                    return@indexOfFirst true
                }
            }
            return@indexOfFirst false
        }
    }

    fun getCalendarItemAt(position: Int): CalendarItem {
        return calendarItems[position]
    }

    fun getDatesRange(dateFrom: CalendarDate, dateTo: CalendarDate): List<CalendarDate> {
        return calendarItems
            .mapNotNull { item ->
                if (item !is DateItem) {
                    return@mapNotNull null
                }
                if (item.date in dateFrom..dateTo) {
                    return@mapNotNull item.date
                } else {
                    null
                }
            }
    }

    fun setCalendarItems(calendarItems: List<CalendarItem>) {
        this.calendarItems.clear()
        this.calendarItems.addAll(calendarItems)
        notifyDataSetChanged()
    }

    fun addNextCalendarItems(nextCalendarItems: List<CalendarItem>) {
        calendarItems.addAll(nextCalendarItems)
        notifyItemRangeInserted(calendarItems.size - nextCalendarItems.size, nextCalendarItems.size)
    }

    fun addPrevCalendarItems(prevCalendarItems: List<CalendarItem>) {
        calendarItems.addAll(0, prevCalendarItems)
        notifyItemRangeInserted(0, prevCalendarItems.size)
    }

    class DateItemViewHolder(val dateView: CalendarDateView) : RecyclerView.ViewHolder(dateView)

    class MonthItemViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    class EmptyItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}