package com.oookraton.chinar

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
class Menu_mainpage : AppCompatActivity() {
    private val categories = listOf("Всё", "Горячее", "Салаты", "Супы", "Напитки", "Мороженное", "Эчпочмаки")
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MenuAdapter
    private lateinit var categoryContainer: LinearLayout
    private val menuItems = MenuData.menuItems
    private var currentCategory = "Всё"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_mainpage)
        recyclerView = findViewById(R.id.recyclerView)
        categoryContainer = findViewById(R.id.categoryContainer)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MenuAdapter(this, filterItemsByCategory().toMutableList())
        recyclerView.adapter = adapter
        setupCategories()
        val backButton = findViewById<Button>(R.id.buttonBack)
        backButton.setOnClickListener {
            val intent = Intent(this, Mainpage::class.java)
            startActivity(intent)
            finish()
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
                    // Update checked state
                    (0 until categoryContainer.childCount).forEach { i ->
                        (categoryContainer.getChildAt(i) as? Chip)?.isChecked = (i == categories.indexOf(category))
                    }
                    // Filter and refresh list
                    adapter.updateItems(filterItemsByCategory())
                }
            }
            categoryContainer.addView(chip)
        }
    }

    private fun filterItemsByCategory(): List<MenuItem> {
        return if (currentCategory == "Всё") {
            menuItems
                .filter { it.availability }
                .sortedWith { item1, item2 ->
                val index1 = categories.indexOf(item1.category)
                val index2 = categories.indexOf(item2.category)
                val finalIndex1 = if (index1 == -1) Int.MAX_VALUE else index1
                val finalIndex2 = if (index2 == -1) Int.MAX_VALUE else index2
                when (val categoryDiff = finalIndex1.compareTo(finalIndex2)) {
                    0 -> item1.title.compareTo(item2.title, ignoreCase = true) // Same category → sort by name
                    else -> categoryDiff // Different category → sort by category order
                }
            }
        } else {
            // Show only items in the selected category, sorted by title
            menuItems
                .filter { it.category == currentCategory && it.availability }
                .sortedBy { it.title.lowercase() }
        }
    }
}