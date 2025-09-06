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
    val category: String,
    val calories: Double,
    val protein: Double,
    val fat: Double,
    val carbs: Double,
    val mass: Int
)
class Menu_mainpage : AppCompatActivity() {
    private val categories = listOf("Всё", "Горячее", "Салаты", "Супы", "Напитки", "Мороженное", "Эчпочмаки")
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MenuAdapter
    private lateinit var categoryContainer: LinearLayout
    // Лист автоматически сортируется, объекты добавлять в любом порядке, категории смотреть выше!
    private val menuItems = listOf(
        MenuItem("Бургер", "Говяжая котлета с листьями салата", "245₽", R.drawable.burger, "Бургер специально для фоток! Булочка насыщенного розового цвета, словно созданного для сторис… Нежная куриная котлета в хрустящей панировке, ломтик помидора, сочный свежий салат и два вида сыра — чеддер и эмменталь, — а ещё специальный сырный соус с едва ощутимой копчёной ноткой и лёгким послевкусием чёрного перчика. Всё это — в лицензионной упаковке с Hello Kitty, вдохновлённой модным азиатским стилем. Это больше чем бургер. Это — стиль, который хочется фотографировать. Выглядит супер, на вкус — ещё круче.", "Горячее", 538.0, 27.0, 53.0, 21.0, 220),
        MenuItem("Пицца", "Маргарита с помидорами и мацареллой", "759₽", R.drawable.pizza,"Цыпленок , сладкий перец , красный лук , моцарелла, соус терияки, фирменный соус альфредо", "Горячее", 258.4, 10.2, 8.6, 35.1, 980),
        MenuItem("Салат", "Свежая зелень в связке с сочной курицей", "169₽", R.drawable.salad, "Хрустящие листья салата айсберг, сочные томаты черри, тертый сыр пармезан и жареные креветки в панировке.", "Салаты", 186.0, 11.0, 9.7, 13.0, 210),
        MenuItem("Бабл Милк Банан-карамель", "Молочный чай с шариками бабл ти и бананово-карамельным топпингом", "259₽", R.drawable.bananaballtea, "Молочный напиток в сочетании с насыщенным сиропом со вкусом банана и карамели. Главная изюминка напитка - жевательные банановые шарики баблс, которые добавляют напитку необычную текстуру.", "Напитки", 288.0, 7.8, 8.2, 46.0, 500)
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
            menuItems.sortedWith { item1, item2 ->
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
                .filter { it.category == currentCategory }
                .sortedBy { it.title.lowercase() }
        }
    }
}