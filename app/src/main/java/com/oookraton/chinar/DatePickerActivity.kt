package com.oookraton.chinar

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.*

class DatePickerActivity : AppCompatActivity() {
    private var toast: Toast? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datepicker)
        val calendarView = findViewById<MaterialCalendarView>(R.id.calendarView)
        val buttonBack = findViewById<Button>(R.id.buttonBack)
        val buttonNext = findViewById<Button>(R.id.buttonNext)
        // Set min and max dates
        val minCalendar: org.threeten.bp.LocalDate = org.threeten.bp.LocalDate.of(2024, 1, 1)
        val maxCalendar: org.threeten.bp.LocalDate = org.threeten.bp.LocalDate.of(2025, 12, 31)
        val minDate = CalendarDay.from(minCalendar)
        val maxDate = CalendarDay.from(maxCalendar)

        calendarView.state().edit()
            .setMinimumDate(minDate)
            .setMaximumDate(maxDate)
            .commit()

        // Disable weekends (sun)
        calendarView.addDecorator(object : DayViewDecorator {
            override fun shouldDecorate(day: CalendarDay): Boolean {
                val calendar = Calendar.getInstance().apply {
                    set(day.year, day.month, day.day)
                }
                val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                return dayOfWeek == 3
            }

            override fun decorate(view: DayViewFacade) {
                view.setDaysDisabled(true)
                view.addSpan(StrikethroughSpan())
                view.addSpan(ForegroundColorSpan(Color.GRAY))
            }
        })

        // Disable specific dates (e.g., holidays)
        val disabledDates = mutableSetOf<CalendarDay>()
        disabledDates.add(CalendarDay.from(2025, 12, 31))  // New Year
        disabledDates.add(CalendarDay.from(2025, 2, 23)) // Defender of Fatherland Day

        calendarView.addDecorator(object : DayViewDecorator {
            override fun shouldDecorate(day: CalendarDay) = day in disabledDates
            override fun decorate(view: DayViewFacade) {
                view.setDaysDisabled(true)
                view.addSpan(StrikethroughSpan())
                view.addSpan(ForegroundColorSpan(Color.GRAY))
            }
        })

        // Handle selection
        var selectedDate: CalendarDay? = null

        calendarView.setOnDateChangedListener { _, date, _ ->
            val calendar = Calendar.getInstance().apply {
                set(date.year, date.month, date.day)
            }
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val isWeekend = dayOfWeek == 3

            if (date !in disabledDates && !isWeekend && date.isInRange(minDate, maxDate)) {
                selectedDate = date
                toast?.cancel()
                toast = null
                toast = Toast.makeText(this, "Дата выбрана: ${date.day}.${date.month + 1}.${date.year}", Toast.LENGTH_SHORT)
                toast?.show()
            } else {
                toast?.cancel()
                toast = null
                toast = Toast.makeText(this, "Дата недоступна", Toast.LENGTH_SHORT)
                toast?.show()
            }
        }

        buttonBack.setOnClickListener {
            val intent = Intent(this, Order_mainpage::class.java)
            startActivity(intent)
            finish()
        }
        buttonNext.setOnClickListener {
            if (selectedDate != null) {
                val year = selectedDate!!.year
                val month = selectedDate!!.month + 1
                val day = selectedDate!!.day
                // Pass this date to another screen or save it
                toast = Toast.makeText(this, "Выбрано: $day.$month.$year", Toast.LENGTH_SHORT)
                toast?.show()
            }
        }
    }

    private fun CalendarDay.isInRange(start: CalendarDay, end: CalendarDay): Boolean {
        val thisCalendar = Calendar.getInstance().apply {
            set(this@isInRange.year, this@isInRange.month, this@isInRange.day)
        }
        val startCalendar = Calendar.getInstance().apply {
            set(start.year, start.month, start.day)
        }
        val endCalendar = Calendar.getInstance().apply {
            set(end.year, end.month, end.day)
        }

        return thisCalendar.timeInMillis >= startCalendar.timeInMillis &&
                thisCalendar.timeInMillis <= endCalendar.timeInMillis
    }
}