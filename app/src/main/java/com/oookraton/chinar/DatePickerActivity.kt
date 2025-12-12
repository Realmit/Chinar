package com.oookraton.chinar

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.*
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.LinearLayout
import java.time.LocalDate
data class DateItem(
    val year: Int,
    val month: Int,
    val day: Int
)
class DatePickerActivity : AppCompatActivity() {
    private var toast: Toast? = null
    private lateinit var eventTypeLayout: LinearLayout
    private lateinit var peopleInputLayout: LinearLayout
    private lateinit var spinnerEventType: Spinner
    private lateinit var inputOtherLayout: TextInputLayout
    private lateinit var editOtherEvent: TextInputEditText
    private lateinit var decorTypeLayout: LinearLayout
    private lateinit var spinnerDecorType: Spinner
    private lateinit var editOtherDecor: TextInputEditText
    private lateinit var inputDecorLayout: TextInputLayout
    private lateinit var decorCheckboxLayout: LinearLayout
    private lateinit var checkPhotobooth: CheckBox
    private lateinit var checkPhotozone: CheckBox
    private lateinit var checkBallons: CheckBox
    private lateinit var checkMinions: CheckBox
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datepicker)
        val calendarView = findViewById<MaterialCalendarView>(R.id.calendarView)
        val buttonBack = findViewById<Button>(R.id.buttonBack)
        val buttonNext = findViewById<Button>(R.id.buttonNext)
        val editNumberOfPeople = findViewById<EditText>(R.id.editNumberOfPeople)
        // Event
        spinnerEventType = findViewById(R.id.spinnerEventType)
        inputOtherLayout = findViewById(R.id.inputOtherLayout)
        editOtherEvent = findViewById(R.id.editOtherEvent)
        eventTypeLayout = findViewById(R.id.eventTypeLayout)
        peopleInputLayout = findViewById(R.id.peopleInputLayout)
        // Decor
        spinnerDecorType = findViewById(R.id.spinnerDecorType)
        inputDecorLayout = findViewById(R.id.inputDecorLayout)
        editOtherDecor = findViewById(R.id.editOtherDecor)
        decorTypeLayout = findViewById(R.id.decorTypeLayout)
        decorCheckboxLayout = findViewById(R.id.decorCheckboxLayout)
        checkPhotobooth = findViewById(R.id.checkPhotobooth)
        checkPhotozone = findViewById(R.id.checkPhotozone)
        checkBallons = findViewById(R.id.checkBallons)
        checkMinions = findViewById(R.id.checkMinions)
        // Setup event dropdown
        val eventTypes = arrayOf("Поминальные", "Свадьба", "День рождения", "Иное")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, eventTypes).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerEventType.adapter = adapter

        // Setup decor dropdown
        val decorTypes = arrayOf("Синие хлопковые", "Красные льняные", "Черные полиэстровые", "Иное")
        val adapterDecor = ArrayAdapter(this, android.R.layout.simple_spinner_item, decorTypes).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerDecorType.adapter = adapterDecor
        // Handle event selection
        spinnerEventType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 3) { // "Иное"
                    inputOtherLayout.visibility = View.VISIBLE
                    setMarginBottom(eventTypeLayout, 80f)
                } else {
                    inputOtherLayout.visibility = View.GONE
                    editOtherEvent.setText("") // Clear when hidden
                    setMarginBottom(eventTypeLayout, 12f)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                inputOtherLayout.visibility = View.GONE
                editOtherEvent.setText("")
                setMarginBottom(eventTypeLayout, 12f)
            }
        }
        // Limit input to 20 chars (reinforce maxLength) to other event input
        editOtherEvent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s?.length ?: 0 > 20) {
                    s?.let {
                        it.replace(20, it.length, "")
                    }
                }
            }
        })
        // Handle decoration selection
        spinnerDecorType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 3) { // "Иное"
                    inputDecorLayout.visibility = View.VISIBLE
                    setMarginBottom(decorTypeLayout, 60f)
                } else {
                    inputDecorLayout.visibility = View.GONE
                    editOtherDecor.setText("") // Clear when hidden
                    setMarginBottom(decorTypeLayout, 4f)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                inputOtherLayout.visibility = View.GONE
                editOtherDecor.setText("")
                setMarginBottom(decorTypeLayout, 4f)
            }
        }
        // Limit decors input to 20 chars (reinforce maxLength) to decor input
        editOtherDecor.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s?.length ?: 0 > 20) {
                    s?.let {
                        it.replace(20, it.length, "")
                    }
                }
            }
        })
        // Set min and max dates
        val minCalendar: org.threeten.bp.LocalDate = org.threeten.bp.LocalDate.of(LocalDate.now().year, LocalDate.now().monthValue, LocalDate.now().dayOfMonth)
        val maxCalendar: org.threeten.bp.LocalDate = org.threeten.bp.LocalDate.of(LocalDate.now().year+1, LocalDate.now().monthValue, LocalDate.now().dayOfMonth)
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
        // Load reserved dates
        syncReservedDatesFromOrders()
        // Disable specific dates and reserved dates
        val disabledDates = (DateData.unavailableDates + DateData.reservedDates)
            .map { CalendarDay.from(it.year, it.month, it.day) }
            .toMutableSet()

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
                peopleInputLayout.visibility = View.VISIBLE
                eventTypeLayout.visibility = View.VISIBLE
                decorTypeLayout.visibility = View.VISIBLE
                decorCheckboxLayout.visibility = View.VISIBLE
            } else {
                toast?.cancel()
                toast = null
                toast = Toast.makeText(this, "Дата недоступна", Toast.LENGTH_SHORT)
                toast?.show()
                peopleInputLayout.visibility = View.GONE
                eventTypeLayout.visibility = View.GONE
                decorTypeLayout.visibility = View.GONE
                decorCheckboxLayout.visibility = View.GONE
            }
        }
        editNumberOfPeople.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString().trim()
                if (input.isNotEmpty()) {
                    val num = input.toIntOrNull()
                    if (num != null && (num < 10 || num > 100)) {
                        editNumberOfPeople.error = "От 10 до 100 человек"
                    }
                }
            }
        })
        buttonBack.setOnClickListener {
            val intent = Intent(this, Mainpage::class.java)
            startActivity(intent)
            finish()
        }
        buttonNext.setOnClickListener {
            toast?.cancel()
            toast = null
            // 1. Validate selected date
            if (selectedDate == null) {
                toast = Toast.makeText(this, "Пожалуйста, выберите дату", Toast.LENGTH_SHORT)
                toast?.show()
                return@setOnClickListener
            }

            // 2. Get date components
            val year = selectedDate!!.year
            val month = selectedDate!!.month
            val day = selectedDate!!.day

            // 3. Get number of people
            val numberOfPeople = editNumberOfPeople.text.toString().trim()
            if (numberOfPeople.isEmpty() || numberOfPeople.toInt() < 10 || numberOfPeople.toInt() > 100) {
                toast = Toast.makeText(this, "Укажите корректное количество человек (10-100)", Toast.LENGTH_SHORT)
                toast?.show()
                return@setOnClickListener
            }

            // 4. Get event type (with special handling for "Иное")
            val eventPosition = spinnerEventType.selectedItemPosition
            val eventType = when {
                eventPosition == 3 && inputOtherLayout.visibility == View.VISIBLE -> {
                    val customEvent = editOtherEvent.text.toString().trim()
                    if (customEvent.isEmpty()) "Иное" else customEvent
                }
                else -> spinnerEventType.selectedItem.toString()
            }

            // 5. Get decor type (with special handling for "Иное")
            val decorPosition = spinnerDecorType.selectedItemPosition
            val decorType = when {
                decorPosition == 3 && inputDecorLayout.visibility == View.VISIBLE -> {
                    val customDecor = editOtherDecor.text.toString().trim()
                    if (customDecor.isEmpty()) "Иное" else customDecor
                }
                else -> spinnerDecorType.selectedItem.toString()
            }

            // 6. Get selected checkboxes
            val selectedOptions = mutableListOf<String>()
            if (checkPhotobooth.isChecked) selectedOptions.add("Фотобудка")
            if (checkPhotozone.isChecked) selectedOptions.add("Фотозона")
            if (checkBallons.isChecked) selectedOptions.add("Воздушные шары")
            if (checkMinions.isChecked) selectedOptions.add("Миньоны")

            // 7. Format data in the requested format
            val checkboxList = selectedOptions.joinToString(";")
            val dataString = "$year,$month,$day,$numberOfPeople,$eventType,$decorType,$checkboxList"

            // 8. Show confirmation toast
            val displayString = "$day.$month.$year, $numberOfPeople чел., $eventType, $decorType" +
                    if (selectedOptions.isNotEmpty()) ", ${selectedOptions.joinToString(", ")}" else ""
            toast = Toast.makeText(this, "Подтверждено:\n$displayString", Toast.LENGTH_LONG)
            toast?.show()

            // 9. Pass to next activity
            val intent = Intent(this, Order_mainpage::class.java)
            intent.putExtra("booking_data", dataString)
            startActivity(intent)
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
    private fun setMarginBottom(view: View, dp: Float) {
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
        val params = view.layoutParams as ViewGroup.MarginLayoutParams
        params.bottomMargin = px.toInt()
        view.layoutParams = params
    }
    private fun syncReservedDatesFromOrders() {
        val currentDates = DateData.reservedDates.toSet()
        val newDates = OrderData.orders.map { it.date }.filter { it !in currentDates }
        DateData.reservedDates.addAll(newDates)
    }
}