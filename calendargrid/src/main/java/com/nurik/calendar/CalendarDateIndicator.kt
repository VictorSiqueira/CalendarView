package com.nurik.calendar

class CalendarDateIndicator(
            override val date: CalendarDate,
            override val color: Int,
            val eventName: String
    ) : CalendarView.DateIndicator