package com.nurik.calendar

class CalendarDayModel (var id: String,
                        var date: String,
                        var weekOfYear: Int?,
                        var child: ArrayList<CalendarItemModel>?,
                        var status: String){

}