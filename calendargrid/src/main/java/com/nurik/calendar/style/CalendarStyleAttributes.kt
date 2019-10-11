package com.nurik.calendar.style

import android.content.Context
import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.nurik.calendar.R
import com.nurik.calendar.extension.getColorInt

/**
 * This class represent a holder for Calendar's style XML attributes.
 */
class CalendarStyleAttributes(
    context: Context,

    var drawGridOnSelectedDates: Boolean = true,

    @ColorInt
    var gridColor: Int = context.getColorInt(R.color.calendar_grid_color),

    @ColorInt
    var yearSelectionBackground: Int = context.getColorInt(R.color.calendar_year_selection_background),

    @ColorInt
    var yearSelectionArrowsColor: Int = context.getColorInt(R.color.calendar_year_selection_arrows_color),

    @ColorInt
    var yearSelectionTextColor: Int = context.getColorInt(R.color.calendar_year_selection_text_color),

    @ColorInt
    var daysBarBackground: Int = context.getColorInt(R.color.calendar_days_bar_background),

    @ColorInt
    var daysBarTextColor: Int = context.getColorInt(R.color.calendar_days_bar_text_color),

    @ColorInt
    var monthTextColor: Int = context.getColorInt(R.color.calendar_month_text_color),

    @DrawableRes
    var dateCellBackgroundColorRes: Int = R.drawable.calendar_date_bg_selector,

    var dateCellTextColorStateList: ColorStateList = requireNotNull(
        ContextCompat.getColorStateList(context, R.color.calendar_date_text_selector)
    )
)