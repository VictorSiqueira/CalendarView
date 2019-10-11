package com.nurik.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.nurik.calendar.adapter.CalendarAdapter
import com.nurik.calendar.adapter.item.EmptyItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var setupcalendar = false
    var lastmonth = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(!setupcalendar){
            mycalendar.onDateClickListener = { date ->

            }
            mycalendar.onScrollListener = {
                    var i = (mycalendar.getRecycler().layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
                    getItemFromMonth(i)
            }
            if (savedInstanceState == null) {
                setupcalendar = true
                mycalendar.setupCalendar(list= mutableListOf())
            }
        }
    }

    private fun getItemFromMonth(i: Int) {
        var day = (mycalendar.getRecycler().adapter as CalendarAdapter).getItemAtPosition(i + 7)
        if(day !is EmptyItem){
            /*var item = day as DateItem
            if(lastmonth!=(item.date.month + 1)) {
                getSchedules((item.date.month + 1).toString(), item.date.year.toString())
            }*/
        }
    }
}
