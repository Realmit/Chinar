package com.oookraton.chinar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class Mainpage : AppCompatActivity() {
    private lateinit var viewPager2: ViewPager2
    var images = intArrayOf(R.drawable.main_image1,R.drawable.main_image2,R.drawable.main_image3)
    var AdapterViewPageMain: AdapterViewPageMain? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // ViewPager setup
        viewPager2=findViewById<ViewPager2>(R.id.viewPager) as ViewPager2;
        AdapterViewPageMain = AdapterViewPageMain(this, images)
        viewPager2.adapter = AdapterViewPageMain
        viewPager2.offscreenPageLimit = 2
        // Better animation
        viewPager2.setPageTransformer { page, position ->
            val offset = position * -page.width * 0.2f
            page.translationX = offset
            val scale = 0.85f + (1 - 0.85f) * (1 - Math.abs(position))
            page.scaleX = scale
            page.scaleY = scale
            page.alpha = if (Math.abs(position) > 1) 0f else 1f
        }
        // Buttons
        val buttonmenu = findViewById<Button>(R.id.buttonGoToMenu)
        buttonmenu.setOnClickListener {
            val intent = Intent(this, Menu_mainpage::class.java)
            startActivity(intent)
        }
        val buttoncafe = findViewById<Button>(R.id.buttonGoToCafe)
        buttoncafe.setOnClickListener {
            val intent = Intent(this, Cafe_mainpage::class.java)
            startActivity(intent)
        }
        val buttonreviews = findViewById<Button>(R.id.buttonGoToReviews)
        buttonreviews.setOnClickListener {
            val intent = Intent(this, Review_mainpage::class.java)
            startActivity(intent)
        }
        val buttonorder = findViewById<Button>(R.id.buttonGoToOrder)
        buttonorder.setOnClickListener {
            val intent = Intent(this, Order_mainpage::class.java)
            startActivity(intent)
        }
    }
}