package com.nurik.calendar

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.core.widget.ImageViewCompat
import com.nurik.calendar.extension.dpToPix
import com.nurik.calendar.extension.spToPix
import com.nurik.calendar.style.CalendarStyleAttributes
import getColorInt

/**
 * This internal view class represents a single date cell of the Calendar with optional
 * colored indicators.
 *
 * This view class control its drawable state with [isToday], [isDateSelected], [isDateDisabled]
 * and [isWeekend] properties.
 */
class CalendarDateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    companion object {
        private const val DEFAULT_TEXT_SIZE = 14.0f
        private const val INDICATOR_RADIUS = 3.0f
        private const val SPACE_BETWEEN_INDICATORS = 4.0f
        private const val MAX_INDICATORS_COUNT = 40
        private val stateToday = intArrayOf(R.attr.calendar_state_today)
        private val stateDateSelected = intArrayOf(R.attr.calendar_state_selected)
        private val stateDateDisabled = intArrayOf(R.attr.calendar_state_disabled)
        private val stateWeekend = intArrayOf(R.attr.calendar_state_weekend)
    }

    private val radiusPx = context.dpToPix(INDICATOR_RADIUS)
    private val spacePx = context.dpToPix(SPACE_BETWEEN_INDICATORS)

    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = context.spToPix(DEFAULT_TEXT_SIZE)
    }

    private val indicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private var dayNumberCalculatedWidth = 0.0f
    private var currentStateTextColor: Int = getColorInt(R.color.calendar_date_text_color)
    private var todayBackgroundColor: Int = getColorInt(R.color.calendar_date_today_day_bg)


    var textColorStateList: ColorStateList? = null

    var isToday: Boolean = false
        set(value) {
            if (value != field) {
                field = value
                refreshDrawableState()
            }
        }
    var isDateSelected: Boolean = false
        set(value) {
            if (value != field) {
                field = value
                refreshDrawableState()
            }
        }
    var isDateDisabled: Boolean = false
        set(value) {
            if (value != field) {
                field = value
                refreshDrawableState()
            }
            isClickable = value.not()
            isLongClickable = value.not()
        }
    var isWeekend: Boolean = false
        set(value) {
            if (value != field) {
                field = value
                refreshDrawableState()
            }
        }
    var dayNumber: String = ""
        set(value) {
            field = value
            dayNumberCalculatedWidth = textPaint.measureText(value)
        }
    var dateIndicators: List<CalendarView.DateIndicator> = emptyList()
        set(indicators) {
            field = indicators.take(MAX_INDICATORS_COUNT)
        }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBG()
        canvas.drawDayNumber()
        canvas.drawIndicators()
        canvas.drawSeparator()
    }

    private fun Canvas.drawBG() {
        var p2 = Paint()
        p2.color = todayBackgroundColor
        drawRect(0f, height/2.2f, width*1.0f, height*1f, p2);
    }

    private fun Canvas.drawDayNumber() {
        textPaint.color = currentStateTextColor
        val xPos = width / 2.0f
        val yPos = height / 4.0f - (textPaint.descent() + textPaint.ascent()) / 2.0f
        drawText(dayNumber, xPos - (dayNumberCalculatedWidth / 2.0f), yPos, textPaint)
    }

    private fun Canvas.drawIndicators() {
        if (dateIndicators.isEmpty()) {
            return
        }
        val indicatorsCount = dateIndicators.size
        val drawableAreaWidth = radiusPx * 2.0f * indicatorsCount + spacePx * (indicatorsCount - 1)
        var xPos = ((width - drawableAreaWidth) / 2.0f) + radiusPx
        val yPos = height - height / 2.5f
        dateIndicators.forEach { indicator ->
            indicatorPaint.color = indicator.color
            drawCircle(xPos, yPos, radiusPx, indicatorPaint)
            xPos += radiusPx * 2.0f + spacePx
        }
    }

    private fun Canvas.drawSeparator() {
        var p = Paint()
        p.color = getColorInt(R.color.separator)
        p.textSize  = 32f
        drawRect(0f, height/2.2f, width*1.0f, height/2.1f, p);
        drawPostsText(p)
    }

    private fun Canvas.drawPostsText(p: Paint) {
        if (!dateIndicators.isEmpty()) {
            val xPos = width / 2.0f
            val yPos = height * 0.9f

            var t = "" /*+ dateIndicators.size + " "+legend
            if (dateIndicators.size > 1) {
                t = "" + dateIndicators.size + " "+plural
            }*/
            drawText(t, xPos - (p.measureText(t) / 2.0f), yPos, p)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        setMeasuredDimension(size, size)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 4)
        if (isToday) {
            mergeDrawableStates(drawableState, stateToday)
        }
        if (isDateSelected) {
            mergeDrawableStates(drawableState, stateDateSelected)
        }
        if (isDateDisabled) {
            mergeDrawableStates(drawableState, stateDateDisabled)
        }
        if (isWeekend) {
            mergeDrawableStates(drawableState, stateWeekend)
        }
        return drawableState
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        val stateList = textColorStateList
        if (stateList != null) {
            currentStateTextColor = stateList.getColorForState(drawableState, currentStateTextColor)
        }
    }

    fun applyStyle(styleAttributes: CalendarStyleAttributes) {

    }
}