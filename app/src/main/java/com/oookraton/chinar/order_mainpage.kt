package com.oookraton.chinar

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText

data class OrderItem(
    val date: DateItem,
    val peopleAmount: Int,
    val eventType: String,
    val clothColor: String,
    val photobooth: Boolean,
    val baloons: Boolean,
    val photozone: Boolean,
    val option4: Boolean,
    val foods: List<Pair<Int, Int>>
)
class Order_mainpage : AppCompatActivity() {
    private val cart = mutableMapOf<MenuItem, Int>()
    private val categories = listOf("Всё", "Горячее", "Салаты", "Супы", "Напитки", "Мороженное", "Эчпочмаки")
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MenuAdapterOrder
    private lateinit var textCartTotal: TextView
    private lateinit var categoryContainer: LinearLayout

    private var currentCategory = "Всё"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_mainpage)
        recyclerView = findViewById(R.id.recyclerView)
        categoryContainer = findViewById(R.id.categoryContainer)
        textCartTotal = findViewById(R.id.textCartTotal)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MenuAdapterOrder(this, filterItemsByCategory().toMutableList(), cart) {updateCartTotal() }
        recyclerView.adapter = adapter
        // Get booking data
        val textBookingSummary = findViewById<TextView>(R.id.textBookingSummary)
        val bookingData = intent.getStringExtra("booking_data") ?: ""

        if (bookingData.isNotEmpty()) {
            val parts = bookingData.split(",")
            if (parts.size >= 7) {
                val year = parts[0]
                val month = parts[1]
                val day = parts[2]
                val numberOfPeople = parts[3]
                val eventType = parts[4]
                val decorType = parts[5]
                val checkboxList = parts.getOrNull(6) ?: ""

                // 1. Create booking summary
                val bookingSummary = "Бронирование:\n" +
                        "Дата: $day.$month.$year\n" +
                        "Количество человек: $numberOfPeople\n" +
                        "Мероприятие: $eventType\n" +
                        "Скатерти: $decorType" +
                        (if (checkboxList.isNotEmpty()) "\nДополнительно: ${checkboxList.replace(";", ", ")}" else "")

                // 2. Create cart items summary
                val cartItemsSummary = if (cart.isNotEmpty()) {
                    val items = cart.entries.joinToString("\n") { (item, quantity) ->
                        "• ${item.title}: $quantity шт. (${parsePrice(item.price) * quantity}₽)"
                    }
                    "\n\nВаш заказ:\n$items"
                } else {
                    "\n\nВаш заказ:\n(корзина пуста)"
                }

                // 3. Calculate and format total
                val itemCount = cart.values.sum()
                val totalPrice = cart.entries.sumOf { (item, quantity) ->
                    parsePrice(item.price) * quantity
                }
                val suffix = getSuffix(itemCount)

                // 4. Combine everything into one superb string
                val fullSummary = "$bookingSummary$cartItemsSummary\n\nИтого: $itemCount позиц$suffix • $totalPrice₽"

                textBookingSummary.text = bookingSummary
            }
        }

        setupCategories()
        updateCartTotal() // Make sure total is updated on creation

        setupCategories()

        val backButton = findViewById<Button>(R.id.buttonBack)
        backButton.setOnClickListener {
            finish()
        }
        val forwardButton = findViewById<Button>(R.id.buttonNext)
        forwardButton.setOnClickListener {
            // Create a detailed order string for next screen
            val orderDetails = createOrderDetailsString(bookingData)

            val intent = Intent(this, OrderFinal::class.java)
            intent.putExtra("order_details", orderDetails)
            startActivity(intent)
        }
    }
    private fun setupCategories() {
        for (category in categories) {
            val chip = Chip(this).apply {
                text = category
                isCheckable = true
                isChecked = (category == currentCategory)
                setTextColor(ContextCompat.getColor(context, R.color.background))
                chipBackgroundColor = ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.maincolorred)
                )
                setOnClickListener {
                    currentCategory = category

                    // Update checked state of all chips
                    (0 until categoryContainer.childCount).forEach { i ->
                        val chip = categoryContainer.getChildAt(i) as? Chip
                        chip?.isChecked = (i == categories.indexOf(category))
                    }
                    adapter.updateItems(filterItemsByCategory())
                    updateCartTotal()
                }
            }
            categoryContainer.addView(chip)
        }
    }

    private fun filterItemsByCategory(): List<MenuItem> {
        return if (currentCategory == "Всё") {
            MenuData.menuItems
                .filter { it.availability }
                .sortedWith { item1, item2 ->
                val idx1 = categories.indexOf(item1.category).let { if (it == -1) Int.MAX_VALUE else it }
                val idx2 = categories.indexOf(item2.category).let { if (it == -1) Int.MAX_VALUE else it }
                when (val diff = idx1.compareTo(idx2)) {
                    0 -> item1.title.compareTo(item2.title, ignoreCase = true)
                    else -> diff
                }
            }
        } else {
            MenuData.menuItems
                .filter { it.category == currentCategory && it.availability }
                .sortedBy { it.title.lowercase() }
        }
    }
    private fun getSuffix(n: Int): String {
        val lastDigit = n % 10
        return if(n >= 11 && n <= 14) "ий"
            else
                when (lastDigit) {
                    1 -> "ия"
                    2, 3, 4 -> "ии"
                    else -> "ий"
                }
    }
    private fun updateCartTotal() {
        val itemCount = cart.values.sum()
        val totalPrice = cart.entries.sumOf { (item, quantity) ->
            parsePrice(item.price) * quantity
        }
        textCartTotal.text = "$itemCount позиц${getSuffix(itemCount)} • ${totalPrice}₽"
    }
    private fun parsePrice(price: String): Int {
        return price.filter { it.isDigit() || it == '.' }.toIntOrNull() ?: 0
    }
    private fun createOrderDetailsString(bookingData: String): String {
        val parts = bookingData.split(",")
        val orderDetails = StringBuilder()
        val AdditionalInfoInput = findViewById<TextInputEditText>(R.id.editAdditionalOrderEvent)
        // Add booking data
        if (parts.size >= 7) {
            val year = parts[0]
            val month = parts[1]
            val day = parts[2]
            val numberOfPeople = parts[3]
            val eventType = parts[4]
            val decorType = parts[5]
            val checkboxList = parts.getOrNull(6)
            orderDetails.append("$year,$month,$day|$eventType|$numberOfPeople|$decorType|$checkboxList|${AdditionalInfoInput.text}|${cart.entries.size}")
        }
        // Add cart items
        cart.entries.forEach { (item, quantity) ->
            orderDetails.append("|${item.id}|${item.title}|$quantity|${parsePrice(item.price)}")
        }

        // Add total
        val totalPrice = cart.entries.sumOf { (item, quantity) -> parsePrice(item.price) * quantity }
        orderDetails.append("|${cart.values.sum()}|${totalPrice}")
        return orderDetails.toString()
    }
}