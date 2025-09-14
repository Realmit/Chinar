package com.oookraton.chinar

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.time.DayOfWeek
import java.time.ZoneId
import java.util.*

class DatePickerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datepicker)

        val calendarView = findViewById<MaterialCalendarView>(R.id.calendarView)
        val buttonBack = findViewById<Button>(R.id.buttonBack)

        // Set min and max dates
        val minCalendar = Calendar.getInstance().apply {
            set(2024, Calendar.JANUARY, 1)
        }
        val maxCalendar = Calendar.getInstance().apply {
            set(2025, Calendar.DECEMBER, 31)
        }
        val minDate = minCalendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

        calendarView.state().edit()
            .setMinimumDate(CalendarDay.from(minDate))
            .setMaximumDate(CalendarDay.from(maxDate))
            .commit()

        // Disable weekends (example)
        calendarView.addDecorator(object : DayViewDecorator {
            override fun shouldDecorate(day: CalendarDay): Boolean {
                val weekDay = day.date.dayOfWeek
                return weekDay == DayOfWeek.SATURDAY || weekDay == DayOfWeek.SUNDAY
            }

            override fun decorate(view: DayViewFacade) {
                view.isDisabled = true
                view.addSpan(StrikethroughSpan()) // Optional: line through
                view.setTextAppearance(R.style.DisabledText)
            }
        })

        // Disable specific dates (e.g., holidays)
        val disabledDates = mutableSetOf<CalendarDay>()
        disabledDates.add(CalendarDay.from(2025, 1, 1))  // New Year
        disabledDates.add(CalendarDay.from(2025, 2, 23)) // Defender of Fatherland Day

        calendarView.addDecorator(object : DayViewDecorator {
            override fun shouldDecorate(day: CalendarDay) = day in disabledDates
            override fun decorate(view: DayViewFacade) {
                view.isDisabled = true
                view.addSpan(ForegroundColorSpan(Color.GRAY))
                view.setTextAppearance(R.style.DisabledText)
            }
        })

        // Handle selection
        var selectedDate: CalendarDay? = null

        calendarView.setOnDateChangedListener { _, date, _ ->
            if (date !in disabledDates && date.isInRange(minDate.time, maxDate.time)) {
                selectedDate = date
                // Optionally highlight or show toast
            } else {
                Toast.makeText(this, "Дата недоступна", Toast.LENGTH_SHORT).show()
            }
        }

        buttonBack.setOnClickListener {
            if (selectedDate != null) {
                val year = selectedDate!!.year
                val month = selectedDate!!.month + 1
                val day = selectedDate!!.day
                // Pass this date to another screen or save it
                Toast.makeText(this, "Выбрано: $day.$month.$year", Toast.LENGTH_LONG).show()
            }
            finish()
        }
    }

    private fun Date.isInRange(start: Date, end: Date): Boolean {
        return this >= start && this <= end
    }
}