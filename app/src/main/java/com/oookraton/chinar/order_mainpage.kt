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
        setupCategories()
        val backButton = findViewById<Button>(R.id.buttonBack)
        backButton.setOnClickListener {
            val intent = Intent(this, Mainpage::class.java)
            startActivity(intent)
            finish()
        }
        val forwardButton = findViewById<Button>(R.id.buttonNext)
        forwardButton.setOnClickListener {
            // Forward to next menu WIP
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
        textCartTotal.text = "$itemCount позиц${getSuffix(itemCount)} • ${formatPrice(totalPrice)}₽"
    }
    private fun parsePrice(price: String): Double {
        return price.filter { it.isDigit() || it == '.' }.toDoubleOrNull() ?: 0.0
    }
    private fun formatPrice(value: Double): String {
        return if (value % 1 == 0.0) value.toInt().toString() else String.format("%.2f", value)
    }
}