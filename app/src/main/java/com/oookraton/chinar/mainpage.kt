package com.oookraton.chinar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Mainpage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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