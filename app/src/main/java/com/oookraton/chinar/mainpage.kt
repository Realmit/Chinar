package com.oookraton.chinar

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class Mainpage : AppCompatActivity() {
    private lateinit var viewPager2: ViewPager2
    private lateinit var cardView: CardView
    var images = intArrayOf(R.drawable.main_image1,R.drawable.main_image2,R.drawable.main_image3)
    var AdapterViewPageMain: AdapterViewPageMain? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Count screen aspect ratio
        val aspectratio = resources.displayMetrics.heightPixels.toFloat()/resources.displayMetrics.widthPixels.toFloat()

        // ViewPager setup
        viewPager2=findViewById<ViewPager2>(R.id.viewPager) as ViewPager2;
        AdapterViewPageMain = AdapterViewPageMain(this, images)
        viewPager2.adapter = AdapterViewPageMain
        viewPager2.offscreenPageLimit = 2
        // Automatically go to 2nd picture
        viewPager2.postDelayed({
            viewPager2.setCurrentItem(1, true)
        }, 100)

        // Better animation
        viewPager2.setPageTransformer { page, position ->
            val offset = position * -page.width * 0.2f
            page.translationX = offset
            val scale = 0.85f + (1 - 0.85f) * (1 - Math.abs(position))
            page.scaleX = scale
            page.scaleY = scale
            page.alpha = if (Math.abs(position) > 1) 0f else 1f
        }
        // Scaling side panels
        viewPager2.setPageTransformer { page, position ->
            page.translationZ = if (position == 0f) 10f else 0f
            val pageWidth = page.width
            val offset: Float
            val scale: Float
            when {
                // Tablet and old phones
                aspectratio < 1.8 -> {
                    offset = position * pageWidth * (0.55f)
                    scale = 1f - (0.25f * abs(position))
                }
                // Thin phone
                aspectratio > 2.30 -> {
                    offset = position * pageWidth * (0.25f)
                    scale = 1f - (0.45f * abs(position))
                }
                // Normal phones
                else -> {
                    offset = position * pageWidth * 0.35f
                    scale = 1f - (0.50f * abs(position))
                }
            }
            page.translationX = -offset
            page.scaleX = scale
            page.scaleY = scale
            page.alpha = 1f
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