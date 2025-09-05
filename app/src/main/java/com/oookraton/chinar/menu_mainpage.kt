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

data class MenuItem(
    val title: String,
    val description: String,
    val price: String,
    val iconRes: Int,
    val fullDescription: String,
    val category: String
)
class Menu_mainpage : AppCompatActivity() {
    private val categories = listOf("Всё", "Горячее", "Салаты", "Поздняков", "Напитки", "Мороженное", "Эчпочмаки")
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MenuAdapter
    private lateinit var categoryContainer: LinearLayout
    private val menuItems = listOf(
        MenuItem("Бургер", "Говяжая котлета с листьями салата", "225₽", R.mipmap.ic_launcher, "Блабла", "Горячее"),
        MenuItem("Пицца", "Маргарита с помидорами и мацареллой", "859₽", R.mipmap.ic_launcher,"Блабла", "Горячее"),
        MenuItem("Салат", "Свежая зелень в связке с сочной курицей", "169₽", R.mipmap.ic_launcher, "Блабла", "Салаты"),
        MenuItem("Поздняков 1.0.", "Подписаться.", "1488₽", R.mipmap.ic_launcher, "Обдристаться.", "Поздняков"),
        MenuItem("Поздняков 2.0.", "Подписаться.", "1488₽", R.mipmap.ic_launcher, "Обдристаться.", "Поздняков"),
        MenuItem("Поздняков 3.0.", "Подписаться.", "1488₽", R.mipmap.ic_launcher, "Обдристаться.", "Поздняков"),
        MenuItem("Поздняков 4.0.", "Подписаться.", "1488₽", R.mipmap.ic_launcher, "Обдристаться.", "Поздняков"),
        MenuItem("Поздняков 5.0.", "Подписаться.", "1488₽", R.mipmap.ic_launcher, "Обдристаться.", "Поздняков"),
        MenuItem("Поздняков 6.0.", "Подписаться.", "1488₽", R.mipmap.ic_launcher, "Обдристаться.", "Поздняков"),
        MenuItem("Поздняков 7.0.", "Подписаться.", "1488₽", R.mipmap.ic_launcher, "Обдристаться.", "Поздняков"),
        MenuItem("Поздняков 8.0.", "Подписаться.", "1488₽", R.mipmap.ic_launcher, "Обдристаться.", "Поздняков")
    )
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
        } else {
            menuItems.filter { it.category == currentCategory }
        }
    }
}