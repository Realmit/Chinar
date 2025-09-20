package com.oookraton.chinar

import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs
import android.widget.Toast
import androidx.core.net.toUri

class Mainpage : AppCompatActivity() {
    private lateinit var viewPager2: ViewPager2
    var images = intArrayOf(R.drawable.main_image1,R.drawable.main_image2,R.drawable.main_image3)
    var AdapterViewPageMain: AdapterViewPageMain? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        // Count screen aspect ratio
        val aspectratio = resources.displayMetrics.heightPixels.toFloat()/resources.displayMetrics.widthPixels.toFloat()

        // ViewPager setup
        viewPager2=findViewById<ViewPager2>(R.id.viewPager) as ViewPager2
        AdapterViewPageMain = AdapterViewPageMain(this, images)
        viewPager2.adapter = AdapterViewPageMain
        viewPager2.offscreenPageLimit = 2
        // Automatically go to 2nd picture
        viewPager2.postDelayed({
            viewPager2.setCurrentItem(1, true)
        }, 500)

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
        val button2gis = findViewById<Button>(R.id.buttonGoToReviews2gis)
        button2gis.setOnClickListener {
            val url = "https://go.2gis.com/XfrWt"
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            try {
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Не получилось открыть ссылку, ошибка: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
        val buttonyamaps = findViewById<Button>(R.id.buttonGoToReviewsYandex)
        buttonyamaps.setOnClickListener {
            val url = "https://yandex.ru/maps/-/CLUK44IT"
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            try {
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Не удалось открыть Яндекс Карты", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Ошибка при открытии карт: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
        val buttonorder = findViewById<Button>(R.id.buttonGoToOrder)
        buttonorder.setOnClickListener {
            val intent = Intent(this, Order_mainpage::class.java)
            startActivity(intent)
        }
        val buttonGoToContacts = findViewById<Button>(R.id.buttonGoToContacts)
        buttonGoToContacts.setOnClickListener {
            try {
                showContactsDialog()
            } catch (e: Exception) {
                Toast.makeText(this, "Ошибка при открытии контактов: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
    
    private fun showContactsDialog() {
        try {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_contacts)
            
            // Close button
            val closeButton = dialog.findViewById<Button>(R.id.buttonCloseContacts)
            closeButton?.setOnClickListener {
                dialog.dismiss()
            }
            
            // WhatsApp click handler
            val whatsappRow = dialog.findViewById<LinearLayout>(R.id.whatsappRow)
            whatsappRow?.setOnClickListener {
                try {
                    val url = "https://api.whatsapp.com/send?phone=79132180307"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Не удалось открыть WhatsApp", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Ошибка при открытии WhatsApp: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            
            // Set dialog properties
            dialog.window?.let { window ->
                val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
                window.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)
                window.setGravity(Gravity.CENTER)
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.show()
            
        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка при создании диалога: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}